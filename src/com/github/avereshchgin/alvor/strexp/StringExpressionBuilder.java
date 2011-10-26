package com.github.avereshchgin.alvor.strexp;

import com.intellij.psi.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StringExpressionBuilder {

    private final StrexpRoot rootNode = new StrexpRoot();

    private final List<StrexpRoot> modifiedVariables = new ArrayList<StrexpRoot>();

    public StringExpressionBuilder() {
    }

    public StringExpressionBuilder(PsiExpression expression) {
        log("StringExpressionBuilder");
        processExpression(expression, rootNode);
        log(toString());
        log("");
    }

    public StringExpressionBuilder(PsiDeclarationStatement declarationStatement) {
        log("Declaration statement");
        for (PsiElement element : declarationStatement.getDeclaredElements()) {
            element.accept(new JavaElementVisitor() {
                @Override
                public void visitLocalVariable(PsiLocalVariable variable) {
                    PsiType type = variable.getType();
                    if ("java.lang.String".equals(type.getCanonicalText())) {
                        StrexpRoot variableRoot = new StrexpRoot();
                        variableRoot.setVariableName(variable.getName());
                        modifiedVariables.add(variableRoot);
                        rootNode.joinNode(variableRoot);
                        processExpression(variable.getInitializer(), variableRoot);
                    } else {
                        log("Unknown type: " + type.getCanonicalText());
                    }
                }
            });
        }
    }

    private void processReferenceExpression(PsiReferenceExpression referenceExpression, final StrexpNode parentNode) {
        if (referenceExpression == null) {
            return;
        }

        PsiElement reference = referenceExpression.resolve();
        if (reference != null) {
            reference.accept(new JavaElementVisitor() {
                @Override
                public void visitLocalVariable(PsiLocalVariable variable) {
                    PsiType type = variable.getType();
                    if ("java.lang.String".equals(type.getCanonicalText())) {
                        parentNode.joinNode(new StrexpVariable(variable.getName()));
                    } else {
                        log("Unknown type: " + type.getCanonicalText());
                    }
                }

                @Override
                public void visitField(PsiField field) {
                    log("Assignment to field: " + field.getName());
                }

                @Override
                public void visitElement(PsiElement element) {
                    log("Unknown element: " + element);
                }
            });
        }
    }

    private void processAssignmentExpression(final PsiAssignmentExpression assignmentExpression, final StrexpNode parentNode) {
        final StrexpRoot variableRoot = new StrexpRoot();
        parentNode.joinNode(variableRoot);
        final StrexpNode joinedNode = new StrexpConcatenation();
        variableRoot.joinNode(joinedNode);

        PsiExpression leftExpression = assignmentExpression.getLExpression();
        leftExpression.accept(new JavaElementVisitor() {
            @Override
            public void visitReferenceExpression(PsiReferenceExpression expression) {
                PsiElement reference = expression.resolve();
                if (reference != null) {
                    reference.accept(new JavaElementVisitor() {
                        @Override
                        public void visitLocalVariable(PsiLocalVariable variable) {
                            PsiType type = variable.getType();
                            if ("java.lang.String".equals(type.getCanonicalText())) {
                                log("Assignment to local variable: " + variable.getName());
                                variableRoot.setVariableName(variable.getName());
                                modifiedVariables.add(variableRoot);
                                if (assignmentExpression.getOperationTokenType().equals(JavaTokenType.PLUSEQ)) {
                                    joinedNode.joinNode(new StrexpVariable(variable.getName()));
                                }
                            } else {
                                log("Unknown type: " + type.getCanonicalText());
                            }
                        }

                        @Override
                        public void visitField(PsiField field) {
                            log("Assignment to field: " + field.getName());
                        }

                        @Override
                        public void visitElement(PsiElement element) {
                            log("Unknown element: " + element);
                        }
                    });
                }
            }
        });
        processExpression(assignmentExpression.getRExpression(), joinedNode);
    }

    private void processExpression(PsiExpression expression, final StrexpNode parentNode) {
        if (expression == null) {
            return;
        }
        log("Expression: " + expression.getText());

        expression.accept(new JavaElementVisitor() {
            @Override
            public void visitAssignmentExpression(PsiAssignmentExpression expression) {
                log("Assignment expression: " + expression);
                processAssignmentExpression(expression, parentNode);
            }

            @Override
            public void visitConditionalExpression(PsiConditionalExpression expression) {
                log("Conditional expression: " + expression);
            }

            @Override
            public void visitLiteralExpression(PsiLiteralExpression expression) {
                log("Literal expression: " + expression.getText());
                Object value = expression.getValue();
                if (value != null) {
                    parentNode.joinNode(new StrexpLiteral(value.toString()));
                }
            }

            @Override
            public void visitMethodCallExpression(PsiMethodCallExpression expression) {
                log("Method call expression: " + expression);
                // TODO: process (s = "ccc").equals("vvv")
                // TODO: process strings in arguments
                // TODO: process return value
            }

            @Override
            public void visitNewExpression(PsiNewExpression expression) {
                log("New expression: " + expression);
            }

            @Override
            public void visitReferenceExpression(PsiReferenceExpression expression) {
                log("Reference expression: " + expression);
                processReferenceExpression(expression, parentNode);
            }

            @Override
            public void visitPolyadicExpression(PsiPolyadicExpression expression) {
                log("Polyadic expression: " + expression);
                StrexpNode concatenationNode = new StrexpConcatenation();
                for (PsiExpression operand : expression.getOperands()) {
                    processExpression(operand, concatenationNode);
                }
                parentNode.joinNode(concatenationNode);
            }

            @Override
            public void visitParenthesizedExpression(PsiParenthesizedExpression expression) {
                log("Parenthesized expression: " + expression);
                processExpression(expression.getExpression(), parentNode);
            }

            @Override
            public void visitExpression(PsiExpression expression) {
                log("Unknown expression: " + expression);
            }

            @Override
            public void visitElement(PsiElement element) {
                log("Unknown element: " + element);
            }
        });
    }

    public StrexpRoot getRootNode() {
        return rootNode;
    }

    public List<StrexpRoot> getModifiedVariables() {
        return Collections.unmodifiableList(modifiedVariables);
    }

    public void log(String message) {
//        System.out.println(message);
    }

    public String toString() {
        String result = "";
        for (StrexpRoot variable : modifiedVariables) {
            result += variable.toString() + ";";
        }
        return result;
    }
}

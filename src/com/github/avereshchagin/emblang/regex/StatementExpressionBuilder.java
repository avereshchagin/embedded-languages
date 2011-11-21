package com.github.avereshchagin.emblang.regex;

import com.intellij.psi.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StatementExpressionBuilder {

    private final RegexExpression expressionNode = new RegexExpression();

    private final List<RegexAssignment> modifiedVariables = new ArrayList<RegexAssignment>();

    public StatementExpressionBuilder(PsiExpression expression) {
        log("StatementExpressionBuilder");
        processExpression(expression, expressionNode);
        log(toString());
        log("");
    }

    public StatementExpressionBuilder(PsiDeclarationStatement declarationStatement) {
        log("Declaration statement");
        for (PsiElement element : declarationStatement.getDeclaredElements()) {
            element.accept(new JavaElementVisitor() {
//                @Override
//                public void visitLocalVariable(PsiLocalVariable variable) {
//                    PsiType type = variable.getType();
//                    if ("java.lang.String".equals(type.getCanonicalText())) {
//                        RegexAssignment variableAssignment = new RegexAssignment();
//                        variableAssignment.setVariable(new RegexVariable(variable.getName(), System.identityHashCode(variable)));
//                        modifiedVariables.add(variableAssignment);
//                        expressionNode.connectNode(variableAssignment);
//                        processExpression(variable.getInitializer(), variableAssignment);
//                    } else {
//                        log("Unknown type: " + type.getCanonicalText());
//                    }
//                }
            });
        }
    }

    private void processReferenceExpression(PsiReferenceExpression referenceExpression, final RegexNode parentNode) {
        if (referenceExpression == null) {
            return;
        }

        PsiElement reference = referenceExpression.resolve();
        if (reference != null) {
            reference.accept(new JavaElementVisitor() {
//                @Override
//                public void visitLocalVariable(PsiLocalVariable variable) {
//                    PsiType type = variable.getType();
//                    if ("java.lang.String".equals(type.getCanonicalText())) {
//                        parentNode.connectNode(new RegexVariable(variable.getName(), System.identityHashCode(variable)));
//                    } else if ("int".equals(type.getCanonicalText()) || "java.lang.Integer".equals(type.getCanonicalText())) {
//                        parentNode.connectNode(
//                                new RegexConcatenation(new RegexAlternation(new RegexLiteral("-"), new RegexEmpty()),
//                                        new RegexPlus(new RegexRange('0', '9'))));
//                    } else {
//                        // TODO: add support for other numeric types
//                        log("Unknown type: " + type.getCanonicalText());
//                    }
//                }
//
//                @Override
//                public void visitField(PsiField field) {
//                    PsiType type = field.getType();
//                    if ("java.lang.String".equals(type.getCanonicalText())) {
//                        parentNode.connectNode(new RegexVariable(field.getName(), System.identityHashCode(field)));
//                    } else if ("int".equals(type.getCanonicalText()) || "java.lang.Integer".equals(type.getCanonicalText())) {
//                        parentNode.connectNode(
//                                new RegexConcatenation(new RegexAlternation(new RegexLiteral("-"), new RegexEmpty()),
//                                        new RegexPlus(new RegexRange('0', '9'))));
//                    } else {
//                        // TODO: add support for other numeric types
//                        log("Unknown type: " + type.getCanonicalText());
//                    }
//                }

                @Override
                public void visitElement(PsiElement element) {
                    log("Unknown element: " + element);
                }
            });
        }
    }

    private void processAssignmentExpression(final PsiAssignmentExpression assignmentExpression, final RegexNode parentNode) {
//        final RegexAssignment assignment = new RegexAssignment();
//        parentNode.connectNode(assignment);
//        final RegexNode assignedConcatenation = new RegexConcatenation();
//        assignment.connectNode(assignedConcatenation);
//
//        PsiExpression leftExpression = assignmentExpression.getLExpression();
//        leftExpression.accept(new JavaElementVisitor() {
//            @Override
//            public void visitReferenceExpression(PsiReferenceExpression expression) {
//                PsiElement reference = expression.resolve();
//                if (reference != null) {
//                    reference.accept(new JavaElementVisitor() {
//                        @Override
//                        public void visitLocalVariable(PsiLocalVariable variable) {
//                            PsiType type = variable.getType();
//                            if ("java.lang.String".equals(type.getCanonicalText())) {
//                                log("Assignment to local variable: " + variable.getName() + ", identity: " + System.identityHashCode(variable));
//                                assignment.setVariable(new RegexVariable(variable.getName(), System.identityHashCode(variable)));
//                                modifiedVariables.add(assignment);
//                                if (assignmentExpression.getOperationTokenType().equals(JavaTokenType.PLUSEQ)) {
//                                    assignedConcatenation.connectNode(new RegexVariable(variable.getName(), System.identityHashCode(variable)));
//                                }
//                            } else {
//                                log("Unknown type: " + type.getCanonicalText());
//                            }
//                        }
//
//                        @Override
//                        public void visitField(PsiField field) {
//                            PsiType type = field.getType();
//                            if ("java.lang.String".equals(type.getCanonicalText())) {
//                                log("Assignment to field: " + field.getName() + ", identity: " + System.identityHashCode(field));
//                                assignment.setVariable(new RegexVariable(field.getName(), System.identityHashCode(field)));
//                                modifiedVariables.add(assignment);
//                                if (assignmentExpression.getOperationTokenType().equals(JavaTokenType.PLUSEQ)) {
//                                    assignedConcatenation.connectNode(new RegexVariable(field.getName(), System.identityHashCode(field)));
//                                }
//                            } else {
//                                log("Unknown type: " + type.getCanonicalText());
//                            }
//                        }
//
//                        @Override
//                        public void visitElement(PsiElement element) {
//                            log("Unknown element: " + element);
//                        }
//                    });
//                }
//            }
//        });
//        processExpression(assignmentExpression.getRExpression(), assignedConcatenation);
    }

    private void processExpression(PsiExpression expression, final RegexNode parentNode) {
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
                RegexConcatenation concatenation = new RegexConcatenation();
                processExpression(expression.getCondition(), concatenation);
                RegexAlternation alternation = new RegexAlternation();
                processExpression(expression.getThenExpression(), alternation);
                processExpression(expression.getElseExpression(), alternation);
                concatenation.connectNode(alternation);
                parentNode.connectNode(concatenation);
            }

            @Override
            public void visitLiteralExpression(PsiLiteralExpression expression) {
                log("Literal expression: " + expression.getText());
                Object value = expression.getValue();
                if (value != null) {
                    parentNode.connectNode(new RegexLiteral(value.toString()));
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
                PsiMethod constructor = expression.resolveConstructor();
                if (constructor != null) {
                    PsiClass containingClass = constructor.getContainingClass();
                    if (containingClass != null) {
                        if ("java.lang.String".equals(containingClass.getQualifiedName())) {
                            PsiExpressionList arguments = expression.getArgumentList();
                            if (arguments != null) {
                                switch (arguments.getExpressions().length) {
                                    case 0:
                                        break;
                                    case 1:
                                        if (arguments.getExpressions().length == 1) {
                                            PsiExpression firstArgument = arguments.getExpressions()[0];
                                            PsiType type = firstArgument.getType();
                                            if (type != null && "java.lang.String".equals(type.getCanonicalText())) {
                                                processExpression(firstArgument, parentNode);
                                            }
                                        }
                                        break;
                                    default:
                                        log("Unsupported String constructor");
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void visitReferenceExpression(PsiReferenceExpression expression) {
                log("Reference expression: " + expression);
                processReferenceExpression(expression, parentNode);
            }

            @Override
            public void visitPolyadicExpression(PsiPolyadicExpression expression) {
                log("Polyadic expression: " + expression);
                RegexConcatenation concatenation = new RegexConcatenation();
                for (PsiExpression operand : expression.getOperands()) {
                    processExpression(operand, concatenation);
                }
                parentNode.connectNode(concatenation);
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

    public RegexExpression getExpressionNode() {
        return expressionNode;
    }

    public List<RegexAssignment> getModifiedVariables() {
        return Collections.unmodifiableList(modifiedVariables);
    }

    public void log(String message) {
//        System.out.println(message);
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        for (RegexAssignment variable : modifiedVariables) {
            result.append(variable);
            result.append(";");
        }
        return result.toString();
    }
}

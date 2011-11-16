package com.github.avereshchgin.alvor.controlflow;

import com.github.avereshchgin.alvor.regex.*;
import com.intellij.psi.*;

import java.util.IdentityHashMap;
import java.util.Map;

public class ControlFlowBuilder extends JavaElementVisitor {

    private final ControlFlow controlFlow = new ControlFlow();

    private final Map<PsiElement, Instruction> elements = new IdentityHashMap<PsiElement, Instruction>();

    private final Map<PsiVariable, RegexVariable> variables = new IdentityHashMap<PsiVariable, RegexVariable>();

    public static ControlFlowBuilder processMethod(PsiMethod method) {
        ControlFlowBuilder generator = new ControlFlowBuilder();
        PsiCodeBlock codeBlock = method.getBody();
        if (codeBlock != null) {
            codeBlock.accept(generator);
        }
        generator.controlFlow.addLast(new ReturnInstruction());
        return generator;
    }

    @Override
    public void visitCodeBlock(PsiCodeBlock block) {
        log("Code block");
        for (PsiStatement statement : block.getStatements()) {
            statement.accept(this);
        }
    }

    @Override
    public void visitBlockStatement(PsiBlockStatement statement) {
        log("Block statement");
        for (PsiStatement innerStatement : statement.getCodeBlock().getStatements()) {
            innerStatement.accept(this);
        }
    }

    @Override
    public void visitEmptyStatement(PsiEmptyStatement statement) {
        log("Empty statement");
    }

    @Override
    public void visitReturnStatement(PsiReturnStatement statement) {
        log("Return statement");
        addInstruction(statement, new ReturnInstruction());
    }

    @Override
    public void visitDeclarationStatement(final PsiDeclarationStatement statement) {
        log("Declaration statement");
        for (PsiElement element : statement.getDeclaredElements()) {
            element.accept(new JavaElementVisitor() {

                @Override
                public void visitLocalVariable(PsiLocalVariable variable) {
                    PsiType type = variable.getType();
                    if ("java.lang.String".equals(type.getCanonicalText())) {
                        RegexVariable declaredVariable = new RegexVariable(variable.getName());
                        variables.put(variable, declaredVariable);
                        RegexNode assignedRegex = new RegexConcatenation();
                        processExpression(variable.getInitializer(), assignedRegex);
                        addInstruction(statement, new AssignmentInstruction(declaredVariable, assignedRegex));
                    } else {
                        log("Unknown type: " + type.getCanonicalText());
                    }
                }
            });
        }
    }

    @Override
    public void visitExpressionStatement(PsiExpressionStatement statement) {
        log("Expression statement");
        processExpression(statement.getExpression(), new RegexEmpty());
    }

    // TODO: check if a verifiable method is called
    private void processExpression(PsiExpression expression, final RegexNode parentNode) {
        if (expression == null) {
            return;
        }
        expression.accept(new JavaElementVisitor() {

            @Override
            public void visitAssignmentExpression(final PsiAssignmentExpression expression) {
                log("Assignment expression: " + expression);
                final PsiExpression rightExpression = expression.getRExpression();
                expression.getLExpression().accept(new JavaElementVisitor() {

                    @Override
                    public void visitReferenceExpression(PsiReferenceExpression referenceExpression) {
                        PsiElement reference = referenceExpression.resolve();
                        if (reference == null) {
                            return;
                        }
                        reference.accept(new JavaElementVisitor() {

                            @Override
                            public void visitLocalVariable(PsiLocalVariable variable) {
                                PsiType type = variable.getType();
                                if ("java.lang.String".equals(type.getCanonicalText())) {
                                    log("Assignment to local variable: " + variable.getName() + ", identity: " + System.identityHashCode(variable));

                                    RegexVariable modifiedVariable = variables.get(variable);
                                    if (modifiedVariable == null) {
                                        modifiedVariable = new RegexVariable(variable.getName());
                                        variables.put(variable, modifiedVariable);
                                    }
                                    parentNode.connectNode(modifiedVariable);
                                    RegexNode assignedRegex = new RegexConcatenation();
                                    if (expression.getOperationTokenType().equals(JavaTokenType.PLUSEQ)) {
                                        assignedRegex.connectNode(modifiedVariable);
                                    }
                                    processExpression(rightExpression, assignedRegex);
                                    addInstruction(expression, new AssignmentInstruction(modifiedVariable, assignedRegex));
                                } else {
                                    log("Unknown type: " + type.getCanonicalText());
                                }
                            }

                            // TODO: add support for static fields

                            @Override
                            public void visitElement(PsiElement element) {
                                log("Unknown element: " + element);
                            }
                        });
                    }
                });
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
            }

            @Override
            public void visitReferenceExpression(PsiReferenceExpression expression) {
                log("Reference expression: " + expression);
                PsiElement reference = expression.resolve();
                if (reference == null) {
                    return;
                }
                reference.accept(new JavaElementVisitor() {

                    @Override
                    public void visitLocalVariable(PsiLocalVariable variable) {
                        PsiType type = variable.getType();
                        if ("java.lang.String".equals(type.getCanonicalText())) {
                            RegexVariable referencedVariable = variables.get(variable);
                            if (referencedVariable != null) {
                                parentNode.connectNode(referencedVariable);
                            }
                        } else if ("int".equals(type.getCanonicalText()) || "java.lang.Integer".equals(type.getCanonicalText())) {
                            parentNode.connectNode(
                                    new RegexConcatenation(new RegexAlternation(new RegexLiteral("-"), new RegexEmpty()),
                                            new RegexPlus(new RegexRange('0', '9'))));
                        } else {
                            // TODO: add support for other numeric types
                            log("Unknown type: " + type.getCanonicalText());
                        }
                    }

                    // TODO: add support for fields

                    @Override
                    public void visitElement(PsiElement element) {
                        log("Unknown element: " + element);
                    }
                });
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
                PsiExpression innerExpression = expression.getExpression();
                if (innerExpression != null) {
                    innerExpression.accept(this);
                }
            }

            @Override
            public void visitExpression(PsiExpression expression) {
                log("Unknown expression: " + expression);
            }
        });
    }

    @Override
    public void visitForStatement(PsiForStatement statement) {
        log("For statement");
    }

    @Override
    public void visitForeachStatement(PsiForeachStatement statement) {
        log("Foreach statement");
    }

    // TODO: check string operations in condition expression
    @Override
    public void visitIfStatement(PsiIfStatement statement) {
        log("If statement");
//        Label falseBranchLabel = new Label();
//        Label endIfLabel = new Label();
        ConditionalInstruction ifInstruction = new ConditionalInstruction();
        addInstruction(statement, ifInstruction);
        PsiStatement thenBranch = statement.getThenBranch();
        if (thenBranch != null) {
            thenBranch.accept(this);
        }
        addInstruction(null, new JumpInstruction(ifInstruction.getEndLabel()));
        addInstruction(null, new LabelTargetInstruction(ifInstruction.getFalseLabel()));
        PsiStatement elseBranch = statement.getElseBranch();
        if (elseBranch != null) {
            elseBranch.accept(this);
        }
        addInstruction(null, new LabelTargetInstruction(ifInstruction.getEndLabel()));
    }

    @Override
    public void visitWhileStatement(PsiWhileStatement statement) {
        log("While statement");

        LoopInstruction whileInstruction = new LoopInstruction();
        addInstruction(null, new LabelTargetInstruction(whileInstruction.getContinueLabel()));
        addInstruction(statement, whileInstruction);

        PsiStatement body = statement.getBody();
        if (body != null) {
            body.accept(this);
        }

        addInstruction(null, new JumpInstruction(whileInstruction.getContinueLabel()));
        addInstruction(null, new LabelTargetInstruction(whileInstruction.getBreakLabel()));

//        CfgStatement currentNode = new CfgRegularStatement(statement.getCondition());
//
//        log("Last label: " + (lastLabel != null ? lastLabel.getText() : "null"));
//
//        Map<PsiIdentifier, CfgStatement> labels = new IdentityHashMap<PsiIdentifier, CfgStatement>(externalLabels);
//        labels.put(lastLabel, currentNode);
//        lastLabel = null;
//
//        cfg.addNode(currentNode);
//        cfg.addEdges(previousNodes, currentNode);
//        previousNodes.clear();
//        previousNodes.addAll(CfgJavaElementVisitor.processStatement(
//                cfgBuilder, Collections.singletonList(currentNode), statement.getBody(), labels
//        ).getPreviousNodes());
//        // back edges
//        cfg.addEdges(previousNodes, currentNode);
    }

    @Override
    public void visitDoWhileStatement(PsiDoWhileStatement statement) {
        log("Do-while statement");

//        CfgStatement currentNode = new CfgRegularStatement(statement.getCondition());
//        currentNode.setAtLeastOnce(true);
//
//        log("Last label: " + (lastLabel != null ? lastLabel.getText() : "null"));
//
//        Map<PsiIdentifier, CfgStatement> labels = new IdentityHashMap<PsiIdentifier, CfgStatement>(externalLabels);
//        labels.put(lastLabel, currentNode);
//        lastLabel = null;
//
//        cfg.addNode(currentNode);
//        cfg.addEdges(previousNodes, currentNode);
//        previousNodes.clear();
//        previousNodes.addAll(CfgJavaElementVisitor.processStatement(
//                cfgBuilder, Collections.singletonList(currentNode), statement.getBody(), labels
//        ).getPreviousNodes());
//        // back edges
//        cfg.addEdges(previousNodes, currentNode);
    }

    @Override
    public void visitContinueStatement(PsiContinueStatement statement) {
        log("Continue statement: " + statement.getText());
        PsiStatement continuedStatement = statement.findContinuedStatement();
        Instruction continuedInstruction = elements.get(continuedStatement);
        if (continuedInstruction instanceof LoopInstruction) {
            addInstruction(null, new JumpInstruction(((LoopInstruction) continuedInstruction).getContinueLabel()));
        }
    }

    @Override
    public void visitBreakStatement(PsiBreakStatement statement) {
        log("Break statement: " + statement.getText());
        PsiStatement exitedStatement = statement.findExitedStatement();
        Instruction exitedInstruction = elements.get(exitedStatement);
        if (exitedInstruction instanceof LoopInstruction) {
            addInstruction(null, new JumpInstruction(((LoopInstruction) exitedInstruction).getBreakLabel()));
        }
    }

    @Override
    public void visitLabeledStatement(PsiLabeledStatement statement) {
        log("Labeled statement");
        PsiStatement childStatement = statement.getStatement();
        if (childStatement != null) {
            childStatement.accept(this);
        }
    }

    @Override
    public void visitSwitchStatement(PsiSwitchStatement statement) {
        log("Switch statement");
    }

    @Override
    public void visitSwitchLabelStatement(PsiSwitchLabelStatement statement) {
        log("Switch label statement: " + statement.getText());
    }

    @Override
    public void visitStatement(PsiStatement statement) {
        log("Unknown statement: " + statement);
    }

    private void addInstruction(PsiElement element, Instruction instruction) {
        if (element != null) {
            elements.put(element, instruction);
        }
        controlFlow.addLast(instruction);
    }

    public ControlFlow getControlFlow() {
        return controlFlow;
    }

    private static void log(String message) {
        System.out.println(message);
    }

    @Override
    public String toString() {
        return controlFlow.toString();
    }
}

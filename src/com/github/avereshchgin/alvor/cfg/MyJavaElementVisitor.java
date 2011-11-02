package com.github.avereshchgin.alvor.cfg;

import com.github.avereshchgin.alvor.verification.VerifiableMethodsFinder;
import com.intellij.psi.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MyJavaElementVisitor extends JavaElementVisitor {

    private final List<CfgNode> prevNodes;

    private final ControlFlowGraphBuilder cfgBuilder;

    private final ControlFlowGraph cfg;

    private final VerifiableMethodsFinder finder;

    private MyJavaElementVisitor(ControlFlowGraphBuilder cfgBuilder, List<CfgNode> prevNodes) {
        this.cfgBuilder = cfgBuilder;
        this.cfg = cfgBuilder.getControlFlowGraph();
        this.finder = cfgBuilder.getFinder();
        this.prevNodes = new ArrayList<CfgNode>(prevNodes);
    }

    public static MyJavaElementVisitor processCodeBlock(ControlFlowGraphBuilder cfgBuilder,
                                                        List<CfgNode> prevNodes,
                                                        PsiCodeBlock codeBlock) {
        MyJavaElementVisitor elementVisitor = new MyJavaElementVisitor(cfgBuilder, prevNodes);
        if (codeBlock != null) {
            for (PsiStatement statement : codeBlock.getStatements()) {
                statement.accept(elementVisitor);
            }
        }
        return elementVisitor;
    }

    public static MyJavaElementVisitor processStatement(ControlFlowGraphBuilder cfgBuilder,
                                                        List<CfgNode> prevNodes,
                                                        PsiStatement statement) {
        MyJavaElementVisitor elementVisitor = new MyJavaElementVisitor(cfgBuilder, prevNodes);
        if (statement != null) {
            statement.accept(elementVisitor);
        }
        return elementVisitor;
    }

    public List<CfgNode> getPrevNodes() {
        return Collections.unmodifiableList(prevNodes);
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
        CfgNode currentNode = new CfgReturnStatement();
        cfg.addNode(currentNode);
        for (CfgNode prevNode : prevNodes) {
            prevNode.connectNext(currentNode);
        }
        prevNodes.clear();
    }

    @Override
    public void visitDeclarationStatement(PsiDeclarationStatement statement) {
        log("Declaration statement");
        CfgNode currentNode = new CfgDeclarationStatement(statement);
        cfg.addNode(currentNode);
        for (CfgNode prevNode : prevNodes) {
            prevNode.connectNext(currentNode);
        }
        prevNodes.clear();
        prevNodes.add(currentNode);
    }

    @Override
    public void visitDoWhileStatement(PsiDoWhileStatement statement) {
        log("Do-while statement");
    }

    @Override
    public void visitExpressionStatement(PsiExpressionStatement statement) {
        log("Expression statement");
        statement.getExpression().accept(new JavaElementVisitor() {
            @Override
            public void visitMethodCallExpression(PsiMethodCallExpression expression) {
                PsiExpression[] argumentsExpressions = expression.getArgumentList().getExpressions();
                if (argumentsExpressions.length > 0) {
                    // TODO: check if expression has String type
                    CfgExpressionStatement currentNode = new CfgExpressionStatement(argumentsExpressions[0]);
                    currentNode.setVerificationRequired(finder.isVerificationRequired(expression.resolveMethod()));
                    cfg.addNode(currentNode);
                    for (CfgNode prevNode : prevNodes) {
                        prevNode.connectNext(currentNode);
                    }
                    prevNodes.clear();
                    prevNodes.add(currentNode);
                }
            }

            @Override
            public void visitExpression(PsiExpression expression) {
                CfgExpressionStatement currentNode = new CfgExpressionStatement(expression);
                cfg.addNode(currentNode);
                for (CfgNode prevNode : prevNodes) {
                    prevNode.connectNext(currentNode);
                }
                prevNodes.clear();
                prevNodes.add(currentNode);
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

    @Override
    public void visitIfStatement(PsiIfStatement statement) {
        log("If statement");
        CfgNode currentNode = new CfgIfStatement(statement.getCondition());
        cfg.addNode(currentNode);
        for (CfgNode prevNode : prevNodes) {
            prevNode.connectNext(currentNode);
        }
        prevNodes.clear();
        prevNodes.addAll(MyJavaElementVisitor.processStatement(
                cfgBuilder, Collections.singletonList(currentNode), statement.getThenBranch()).getPrevNodes());
        prevNodes.addAll(MyJavaElementVisitor.processStatement(
                cfgBuilder, Collections.singletonList(currentNode), statement.getElseBranch()).getPrevNodes());
    }

    @Override
    public void visitWhileStatement(PsiWhileStatement statement) {
        log("While statement");
    }

    @Override
    public void visitStatement(PsiStatement statement) {
        log("Unknown statement: " + statement);
    }

    private void log(String message) {
//        System.out.println(message);
    }
}
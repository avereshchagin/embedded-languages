package com.github.avereshchgin.alvor.cfg;

import com.intellij.psi.*;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ControlFlowGraphBuilder {

    private final ControlFlowGraph cfg = new ControlFlowGraph();

    private final OutflushingMethodsFinder finder;

    public ControlFlowGraphBuilder(OutflushingMethodsFinder finder) {
        this.finder = finder;
    }

    public void addMethod(PsiMethod psiMethod) {
        List<CfgNode> prevNodes = new ArrayList<CfgNode>();
        CfgNode rootNode = new CfgRootNode(psiMethod);
        cfg.addNode(rootNode);
        prevNodes.add(rootNode);

        new MyJavaElementVisitor(prevNodes, psiMethod.getBody());
    }

    public void showGraph() {
        try {
            Process process = Runtime.getRuntime().exec("/usr/local/bin/dot -Tpng -o/tmp/graph.png");
            PrintStream out = new PrintStream(process.getOutputStream());
            cfg.printDotGraph(out);
            out.flush();
            out.close();
            process.waitFor();

            Runtime.getRuntime().exec("open /tmp/graph.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ControlFlowGraph getControlFlowGraph() {
        return cfg;
    }

    private void log(String message) {
        System.out.println(message);
    }

    private class MyJavaElementVisitor extends JavaElementVisitor {

        private final List<CfgNode> prevNodes;

        public MyJavaElementVisitor(List<CfgNode> prevNodes, PsiCodeBlock codeBlock) {
            this.prevNodes = new ArrayList<CfgNode>(prevNodes);
            if (codeBlock != null) {
                for (PsiStatement statement : codeBlock.getStatements()) {
                    statement.accept(this);
                }
            }
        }

        public MyJavaElementVisitor(List<CfgNode> prevNodes, PsiStatement statement) {
            this.prevNodes = new ArrayList<CfgNode>(prevNodes);
            if (statement != null) {
                statement.accept(this);
            }
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
                prevNode.joinNext(currentNode);
            }
            prevNodes.clear();
        }

        @Override
        public void visitDeclarationStatement(PsiDeclarationStatement statement) {
            log("Declaration statement");
            CfgNode currentNode = new CfgDeclarationStatementNode(statement);
            cfg.addNode(currentNode);
            for (CfgNode prevNode : prevNodes) {
                prevNode.joinNext(currentNode);
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
                    PsiExpression[] expressions = expression.getArgumentList().getExpressions();
                    if (expressions.length > 0) {
                        // TODO: check if expression has String type
                        CfgExpressionStatementNode currentNode = new CfgExpressionStatementNode(expressions[0]);
                        currentNode.setOutflushingMethodCall(finder.isOutflushingMethod(expression));
                        cfg.addNode(currentNode);
                        for (CfgNode prevNode : prevNodes) {
                            prevNode.joinNext(currentNode);
                        }
                        prevNodes.clear();
                        prevNodes.add(currentNode);
                    }
                }

                @Override
                public void visitExpression(PsiExpression expression) {
                    CfgExpressionStatementNode currentNode = new CfgExpressionStatementNode(expression);
                    cfg.addNode(currentNode);
                    for (CfgNode prevNode : prevNodes) {
                        prevNode.joinNext(currentNode);
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
            CfgNode currentNode = new CfgIfStatementNode(statement.getCondition());
            cfg.addNode(currentNode);
            for (CfgNode prevNode : prevNodes) {
                prevNode.joinNext(currentNode);
            }
            List<CfgNode> conditionNode = new ArrayList<CfgNode>();
            conditionNode.add(currentNode);
            prevNodes.clear();
            prevNodes.addAll(new MyJavaElementVisitor(conditionNode, statement.getThenBranch()).getPrevNodes());
            prevNodes.addAll(new MyJavaElementVisitor(conditionNode, statement.getElseBranch()).getPrevNodes());
        }

        @Override
        public void visitWhileStatement(PsiWhileStatement statement) {
            log("While statement");
        }

        @Override
        public void visitStatement(PsiStatement statement) {
            log("Unknown statement: " + statement);
        }
    }
}

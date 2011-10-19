package com.github.avereshchgin.alvor.cfg;

import com.intellij.psi.*;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ControlFlowGraphBuilder {

    private final ControlFlowGraph cfg = new ControlFlowGraph();

    public void addMethod(PsiMethod psiMethod) {
        List<CfgNode> prevNodes = new ArrayList<CfgNode>();
        CfgNode rootNode = new CfgRootNode(psiMethod);
        cfg.addNode(rootNode);
        prevNodes.add(rootNode);

        processCodeBlock(prevNodes, psiMethod.getBody());
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

//    public void buildRegularExpression() {
//        cfg.findSQLExpressions();
//    }

    private List<CfgNode> processIfBranch(PsiStatement psiStatement, List<CfgNode> prevNodes, CfgNode currentNode) {
        if (psiStatement != null) {

            if (psiStatement instanceof PsiBlockStatement) {
                return processCodeBlock(prevNodes, ((PsiBlockStatement) psiStatement).getCodeBlock());

            } else if (psiStatement instanceof PsiExpressionStatement) {
                CfgNode expressionNode = new CfgExpressionStatementNode((PsiExpressionStatement) psiStatement);
                cfg.addNode(expressionNode);
                currentNode.addOutgoingEdgeTo(expressionNode);

                List<CfgNode> ret = new ArrayList<CfgNode>();
                ret.add(expressionNode);
                return ret;

            } else if (psiStatement instanceof PsiDeclarationStatement) {
                CfgNode expressionNode = new CfgDeclarationStatementNode((PsiDeclarationStatement) psiStatement);
                cfg.addNode(expressionNode);
                currentNode.addOutgoingEdgeTo(expressionNode);

                List<CfgNode> ret = new ArrayList<CfgNode>();
                ret.add(expressionNode);
                return ret;

            } else if (psiStatement instanceof PsiEmptyStatement) {
                List<CfgNode> ret = new ArrayList<CfgNode>();
                ret.add(currentNode);
                return ret;
            }
        }
        return Collections.emptyList();
    }

    private List<CfgNode> processIfStatement(List<CfgNode> prevNodes, PsiIfStatement psiIfStatement) {
        CfgNode currentNode = new CfgIfStatementNode(psiIfStatement.getCondition());

        cfg.addNode(currentNode);
        for (CfgNode prevNode : prevNodes) {
            prevNode.addOutgoingEdgeTo(currentNode);
        }

        prevNodes = new ArrayList<CfgNode>();
        prevNodes.add(currentNode);

        List<CfgNode> tailNodes = new ArrayList<CfgNode>();

        tailNodes.addAll(processIfBranch(psiIfStatement.getThenBranch(), prevNodes, currentNode));
        tailNodes.addAll(processIfBranch(psiIfStatement.getElseBranch(), prevNodes, currentNode));

        if (tailNodes.isEmpty()) {
            tailNodes.add(currentNode);
        }
        return tailNodes;
    }

    private List<CfgNode> processCodeBlock(List<CfgNode> prevNodes, PsiCodeBlock codeBlock) {
        if (codeBlock == null) {
            return prevNodes;
        }

        CfgNode currentNode;
        for (PsiStatement statement : codeBlock.getStatements()) {

            if (statement instanceof PsiIfStatement) {
                prevNodes = processIfStatement(prevNodes, (PsiIfStatement) statement);

            } else if (statement instanceof PsiReturnStatement) {
                currentNode = new CfgReturnStatementNode((PsiReturnStatement) statement);
                cfg.addNode(currentNode);

                for (CfgNode prevNode : prevNodes) {
                    prevNode.addOutgoingEdgeTo(currentNode);
                }

                return new ArrayList<CfgNode>();

            } else if (statement instanceof PsiExpressionStatement) {
                currentNode = new CfgExpressionStatementNode((PsiExpressionStatement) statement);
                cfg.addNode(currentNode);

                for (CfgNode prevNode : prevNodes) {
                    prevNode.addOutgoingEdgeTo(currentNode);
                }

                prevNodes = new ArrayList<CfgNode>();
                prevNodes.add(currentNode);

            } else if (statement instanceof PsiDeclarationStatement) {
                currentNode = new CfgDeclarationStatementNode((PsiDeclarationStatement) statement);
                cfg.addNode(currentNode);

                for (CfgNode prevNode : prevNodes) {
                    prevNode.addOutgoingEdgeTo(currentNode);
                }

                prevNodes = new ArrayList<CfgNode>();
                prevNodes.add(currentNode);

            } else {
                System.out.println("Unknown statement: " + statement);
            }
        }
        return prevNodes;
    }

}

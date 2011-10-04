package com.github.avereshchgin.alvor.cfg;

import com.intellij.psi.*;

import java.io.PrintStream;
import java.util.ArrayList;

public class ControlFlowGraph {

    public ControlFlowGraph() {
        nodes = new ArrayList<CfgNode>();
    }

    public ArrayList<CfgNode> processIfStatement(ArrayList<CfgNode> prevNodes, PsiIfStatement psiIfStatement) {
        CfgNode currentNode;
        currentNode = new CfgIfStatementNode(psiIfStatement.getCondition());

        addNode(currentNode);
        for (CfgNode prevNode : prevNodes) {
            prevNode.addOutgoingEdge(currentNode);
        }

        prevNodes = new ArrayList<CfgNode>();
        prevNodes.add(currentNode);

        ArrayList<CfgNode> tailNodes = new ArrayList<CfgNode>();

        PsiStatement thenStatement = psiIfStatement.getThenBranch();
        if (thenStatement != null) {

            if (thenStatement instanceof PsiBlockStatement) {
                tailNodes.addAll(processCodeBlock(prevNodes, ((PsiBlockStatement) thenStatement).getCodeBlock()));

            } else if (thenStatement instanceof PsiExpressionStatement) {
                CfgNode thenNode = new CfgStatementNode(thenStatement);
                addNode(thenNode);
                currentNode.addOutgoingEdge(thenNode);
                tailNodes.add(thenNode);

            } else if (thenStatement instanceof PsiEmptyStatement) {
                tailNodes.add(currentNode);
            }
        }

        PsiStatement elseStatement = psiIfStatement.getElseBranch();
        if (elseStatement != null) {

            if (elseStatement instanceof PsiBlockStatement) {
                tailNodes.addAll(processCodeBlock(prevNodes, ((PsiBlockStatement) elseStatement).getCodeBlock()));

            } else if (elseStatement instanceof PsiExpressionStatement) {
                CfgNode elseNode = new CfgStatementNode(elseStatement);
                addNode(elseNode);
                currentNode.addOutgoingEdge(elseNode);
                tailNodes.add(elseNode);

            } else if (elseStatement instanceof PsiEmptyStatement) {
                tailNodes.add(currentNode);
            }
        }

        if (tailNodes.isEmpty()) {
            tailNodes.add(currentNode);
        }
        return tailNodes;
    }

    public ArrayList<CfgNode> processCodeBlock(ArrayList<CfgNode> prevNodes, PsiCodeBlock psiCodeBlock) {
        if (psiCodeBlock == null) {
            return prevNodes;
        }

        CfgNode currentNode;
        for (PsiStatement psiStatement : psiCodeBlock.getStatements()) {

            if (psiStatement instanceof PsiIfStatement) {
                prevNodes = processIfStatement(prevNodes, (PsiIfStatement) psiStatement);

            } else if (psiStatement instanceof PsiReturnStatement) {
                currentNode = new CfgReturnStatementNode((PsiReturnStatement) psiStatement);
                addNode(currentNode);

                for (CfgNode prevNode : prevNodes) {
                    prevNode.addOutgoingEdge(currentNode);
                }

                return new ArrayList<CfgNode>();
            } else {
                currentNode = new CfgStatementNode(psiStatement);
                addNode(currentNode);

                for (CfgNode prevNode : prevNodes) {
                    prevNode.addOutgoingEdge(currentNode);
                }

                prevNodes = new ArrayList<CfgNode>();
                prevNodes.add(currentNode);
            }
        }
        return prevNodes;
    }

    public void addMethod(PsiMethod psiMethod) {
        ArrayList<CfgNode> prevNodes = new ArrayList<CfgNode>();
        CfgNode rootNode = new CfgRootNode(psiMethod);
        addNode(rootNode);
        prevNodes.add(rootNode);

        processCodeBlock(prevNodes, psiMethod.getBody());
    }

    public void addNode(CfgNode node) {
        node.setKey(nodes.size());
        nodes.add(node);
    }

    private void printDotGraph(PrintStream out) {
        out.println("digraph G {");
        for (CfgNode node : nodes) {
            out.println(node.getKey() + " [label=\"" + node.toString().replaceAll("\"", "\\\\\"") + "\"];");
        }
        for (CfgNode srcNode : nodes) {
            for (CfgNode destNode : srcNode.getOutgoingEdges()) {
                out.println(srcNode.getKey() + " -> " + destNode.getKey() + ";");
            }
        }
        out.println("}");
    }

    public void showGraph() {
        try {
            Process process = Runtime.getRuntime().exec("/usr/local/bin/dot -Tpng -o/tmp/graph.png");
            PrintStream out = new PrintStream(process.getOutputStream());
            printDotGraph(out);
            out.flush();
            out.close();
            process.waitFor();

            Runtime.getRuntime().exec("open /tmp/graph.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final ArrayList<CfgNode> nodes;
}

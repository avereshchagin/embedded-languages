package com.github.avereshchgin.alvor.cfg;

import com.github.avereshchgin.alvor.regex.RegularExpression;
import com.github.avereshchgin.alvor.regex.SQLExpressionFinder;
import com.intellij.psi.PsiExpression;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class ControlFlowGraph {

    private final List<CfgNode> nodes = new ArrayList<CfgNode>();

    public void addNode(CfgNode node) {
        nodes.add(node);
    }

    public void printDotGraph(PrintStream out) {
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

    public void findSQLExpressions() {
        SQLExpressionFinder expressionFinder = new SQLExpressionFinder();
        for (CfgNode node : nodes) {
            PsiExpression expression = node.getSQLExpression(expressionFinder);
            if (expression != null) {
                System.out.println("Expression found: " + expression.getText());

                RegularExpression regularExpression = new RegularExpression();
                regularExpression.buildRegularExpression(expression, node);
                System.out.println("Regular expression: " + regularExpression);
            }
        }
    }

}

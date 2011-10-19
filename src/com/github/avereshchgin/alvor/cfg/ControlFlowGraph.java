package com.github.avereshchgin.alvor.cfg;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ControlFlowGraph {

    private final List<CfgNode> nodes = new ArrayList<CfgNode>();

    private final List<CfgNode> sqlMethodCallNodes = new ArrayList<CfgNode>();

    public void addNode(CfgNode node) {
        if (node.getHasSqlMethodCall()) {
            System.out.println("Node " + node + " contains SQL method call");
            sqlMethodCallNodes.add(node);
        }
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

    public List<CfgNode> getSqlMethodCallNodes() {
        return Collections.unmodifiableList(sqlMethodCallNodes);
    }
}

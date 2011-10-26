package com.github.avereshchgin.alvor.cfg;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ControlFlowGraph {

    private final List<CfgNode> nodes = new ArrayList<CfgNode>();

    private final List<CfgNode> outflushingMethodCallNodes = new ArrayList<CfgNode>();

    public void addNode(CfgNode node) {
        if (node.isOutflushingMethodCall()) {
            outflushingMethodCallNodes.add(node);
        }
        nodes.add(node);
    }

    public void printDotGraph(PrintStream out) {
        out.println("digraph G {");
        for (CfgNode node : nodes) {
            out.println(System.identityHashCode(node) + " [label=\"" + node.toString().replaceAll("\"", "\\\\\"") + "\"];");
        }
        for (CfgNode srcNode : nodes) {
            for (CfgNode destNode : srcNode.getNextNodes()) {
                out.println(System.identityHashCode(srcNode) + " -> " + System.identityHashCode(destNode) + ";");
            }
        }
        out.println("}");
    }

    public List<CfgNode> getOutflushingMethodCallNodes() {
        return Collections.unmodifiableList(outflushingMethodCallNodes);
    }
}

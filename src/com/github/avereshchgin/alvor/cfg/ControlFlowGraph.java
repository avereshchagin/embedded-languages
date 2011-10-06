package com.github.avereshchgin.alvor.cfg;

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

}

package com.github.avereshchgin.alvor.cfg;

import java.util.List;

public class DepthFirstSearcher {

    private static int clock;

    private static void explore(CfgStatement node) {
        node.setVisited(true);
        node.setEnterValue(clock++);
        for (CfgEdge edge : node.getIncomingEdges()) {
            if (!edge.getSource().isVisited()) {
                explore(edge.getSource());
            }
        }
        node.setLeaveValue(clock++);
    }

    public static void processGraph(ControlFlowGraph controlFlowGraph) {
        List<CfgStatement> startNodes = controlFlowGraph.getVerifiableMethodCallNodes();
        clock = 0;
        for (CfgStatement node : startNodes) {
            if (!node.isVisited()) {
                explore(node);
            }
        }
        for (CfgEdge edge : controlFlowGraph.getEdges()) {
            CfgStatement u = edge.getDestination();
            CfgStatement v = edge.getSource();
            if (u.isVisited() && v.isVisited()) {
                if (u.getEnterValue() < v.getEnterValue() && v.getLeaveValue() < u.getLeaveValue()) {
                    edge.setType(CfgEdge.Type.FORWARD);
                } else if (v.getEnterValue() < u.getEnterValue() && u.getLeaveValue() < v.getLeaveValue()) {
                    edge.setType(CfgEdge.Type.BACK);
                } else if (v.getLeaveValue() < u.getEnterValue()) {
                    edge.setType(CfgEdge.Type.CROSS);
                }
            }
        }
    }
}

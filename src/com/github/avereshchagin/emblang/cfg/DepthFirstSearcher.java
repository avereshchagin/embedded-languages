package com.github.avereshchagin.emblang.cfg;

import java.util.ArrayList;
import java.util.List;

public class DepthFirstSearcher {

    private int clock;

    private List<CfgStatement> topOrdering = new ArrayList<CfgStatement>();

    private void explore(CfgStatement node) {
        node.setVisited(true);
        node.setEnterValue(clock++);
        for (CfgEdge edge : node.getIncomingEdges()) {
            if (!edge.getSource().isVisited()) {
                explore(edge.getSource());
            }
        }
        node.setLeaveValue(clock++);
        topOrdering.add(node);
    }

    public void processGraph(ControlFlowGraph controlFlowGraph) {
        List<CfgStatement> startNodes = controlFlowGraph.getVerifiableMethodCallNodes();
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

    public List<CfgStatement> getTopOrdering() {
        return topOrdering;
    }
}

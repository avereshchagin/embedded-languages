package com.github.avereshchgin.alvor.cfg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ControlFlowGraph {

    private final List<CfgNode> nodes = new ArrayList<CfgNode>();

    private final List<CfgNode> outflushingMethodCallNodes = new ArrayList<CfgNode>();

    public void addNode(CfgNode node) {
        if (node.isVerificationRequired()) {
            outflushingMethodCallNodes.add(node);
        }
        nodes.add(node);
    }

    public List<CfgNode> getNodes() {
        return Collections.unmodifiableList(nodes);
    }

    public List<CfgNode> getOutflushingMethodCallNodes() {
        return Collections.unmodifiableList(outflushingMethodCallNodes);
    }
}

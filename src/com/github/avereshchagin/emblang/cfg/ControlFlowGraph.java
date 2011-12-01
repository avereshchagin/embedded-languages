package com.github.avereshchagin.emblang.cfg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ControlFlowGraph {

    private final List<CfgStatement> nodes = new ArrayList<CfgStatement>();

    private final List<CfgEdge> edges = new ArrayList<CfgEdge>();

    private final List<CfgStatement> verifiableMethodCallNodes = new ArrayList<CfgStatement>();

    public void addNode(CfgStatement node) {
        if (node.isVerificationRequired()) {
            verifiableMethodCallNodes.add(node);
        }
        nodes.add(node);
    }

    public void addEdge(CfgStatement source, CfgStatement destination) {
        edges.add(new CfgEdge(source, destination));
    }

    public List<CfgStatement> getNodes() {
        return Collections.unmodifiableList(nodes);
    }

    public List<CfgEdge> getEdges() {
        return Collections.unmodifiableList(edges);
    }

    public List<CfgStatement> getVerifiableMethodCallNodes() {
        return Collections.unmodifiableList(verifiableMethodCallNodes);
    }
}

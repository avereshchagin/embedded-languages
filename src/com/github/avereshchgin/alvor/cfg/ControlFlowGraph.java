package com.github.avereshchgin.alvor.cfg;

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

    public void addEdges(List<CfgStatement> sources, CfgStatement destination) {
        for (CfgStatement source : sources) {
            addEdge(source, destination);
        }
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

package com.github.avereshchgin.alvor.cfg;

import java.util.ArrayList;

public abstract class CfgNode {

    public int getKey() {
        return System.identityHashCode(this);
    }

    public abstract void addOutgoingEdgeTo(CfgNode node);

    public abstract ArrayList<CfgNode> getOutgoingEdges();

}

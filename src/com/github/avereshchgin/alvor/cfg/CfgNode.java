package com.github.avereshchgin.alvor.cfg;

import java.util.ArrayList;

public abstract class CfgNode {

    public void setKey(int key) {
        this.key = key;
    }

    public int getKey() {
        return key;
    }

    public abstract void addOutgoingEdge(CfgNode node);

    public abstract ArrayList<CfgNode> getOutgoingEdges();

    protected int key;

}

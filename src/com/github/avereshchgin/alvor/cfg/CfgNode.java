package com.github.avereshchgin.alvor.cfg;

import java.util.List;

public abstract class CfgNode {

    public int getKey() {
        return System.identityHashCode(this);
    }

    public abstract void addOutgoingEdgeTo(CfgNode node);

    public abstract List<CfgNode> getOutgoingEdges();

    protected abstract void addIncommingEdgeFrom(CfgNode node);

    public abstract List<CfgNode> getIncommingEdges();

    public StringExpression getStringExpression() {
        return null;
    }

    public boolean getHasSqlMethodCall() {
        return false;
    }

}

package com.github.avereshchgin.alvor.cfg;

import com.github.avereshchgin.alvor.strexp.StrexpRoot;

import java.util.List;

public abstract class CfgNode {

    public abstract void joinNext(CfgNode node);

    public abstract List<CfgNode> getNextNodes();

    protected abstract void joinPrevious(CfgNode node);

    public abstract List<CfgNode> getPreviousNodes();

    public boolean isOutflushingMethodCall() {
        return false;
    }

    public StrexpRoot getRootForVariable(String name) {
        return null;
    }
}

package com.github.avereshchgin.alvor.cfg;

import com.github.avereshchgin.alvor.strexp.StrexpAssignment;

import java.util.List;

public abstract class CfgNode {

    public abstract void connectNext(CfgNode node);

    public abstract List<CfgNode> getNextNodes();

    protected abstract void connectPrevious(CfgNode node);

    public abstract List<CfgNode> getPreviousNodes();

    public boolean isVerificationRequired() {
        return false;
    }

    public StrexpAssignment getRootForVariable(String name) {
        return null;
    }
}

package com.github.avereshchgin.alvor.cfg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CfgReturnStatement extends CfgNode {
    private final List<CfgNode> prev = new ArrayList<CfgNode>();

    @Override
    public String toString() {
        return "return";
    }

    public void joinNext(CfgNode node) {
    }

    public List<CfgNode> getNextNodes() {
        return Collections.emptyList();
    }

    protected void joinPrevious(CfgNode node) {
        prev.add(node);
    }

    public List<CfgNode> getPreviousNodes() {
        return prev;
    }

}

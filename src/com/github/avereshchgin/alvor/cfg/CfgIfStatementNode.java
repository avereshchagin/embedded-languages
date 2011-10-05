package com.github.avereshchgin.alvor.cfg;

import com.intellij.psi.PsiExpression;

import java.util.ArrayList;

public class CfgIfStatementNode extends CfgNode {

    private final ArrayList<CfgNode> nodes = new ArrayList<CfgNode>();

    private final PsiExpression condition;

    public CfgIfStatementNode(PsiExpression condition) {
        this.condition = condition;
    }

    public void addOutgoingEdgeTo(CfgNode node) {
        nodes.add(node);
    }

    public ArrayList<CfgNode> getOutgoingEdges() {
        return nodes;
    }

    public String toString() {
        return condition.getText();
    }

}

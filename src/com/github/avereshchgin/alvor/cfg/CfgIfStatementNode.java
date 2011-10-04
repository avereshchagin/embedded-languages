package com.github.avereshchgin.alvor.cfg;

import com.intellij.psi.PsiExpression;

import java.util.ArrayList;

public class CfgIfStatementNode extends CfgNode {

    public CfgIfStatementNode(PsiExpression condition) {
        this.condition = condition;
        nodes = new ArrayList<CfgNode>();
    }

    public void addOutgoingEdge(CfgNode node) {
        nodes.add(node);
    }

    public ArrayList<CfgNode> getOutgoingEdges() {
        return nodes;
    }

    public String toString() {
        return condition.getText();
    }

    private ArrayList<CfgNode> nodes;

    private final PsiExpression condition;
}

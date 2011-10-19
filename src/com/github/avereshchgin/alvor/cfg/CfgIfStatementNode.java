package com.github.avereshchgin.alvor.cfg;

import com.intellij.psi.PsiExpression;

import java.util.ArrayList;
import java.util.List;

public class CfgIfStatementNode extends CfgNode {

    private final List<CfgNode> nodes = new ArrayList<CfgNode>();

    private final List<CfgNode> prev = new ArrayList<CfgNode>();

//    private final PsiExpression condition;

    private final StringExpression stringExpression;

    public CfgIfStatementNode(PsiExpression expression) {
        stringExpression = new StringExpression(expression);
    }

    public void addOutgoingEdgeTo(CfgNode node) {
        nodes.add(node);
        node.addIncommingEdgeFrom(this);
    }

    public List<CfgNode> getOutgoingEdges() {
        return nodes;
    }

    protected void addIncommingEdgeFrom(CfgNode node) {
        prev.add(node);
    }

    public List<CfgNode> getIncommingEdges() {
        return prev;
    }

    public String toString() {
        return stringExpression.toString();
    }

    public StringExpression getStringExpression() {
        return stringExpression;
    }

}

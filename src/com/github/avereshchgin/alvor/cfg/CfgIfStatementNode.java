package com.github.avereshchgin.alvor.cfg;

import com.github.avereshchgin.alvor.regex.SQLExpressionFinder;
import com.intellij.psi.PsiExpression;

import java.util.ArrayList;
import java.util.List;

public class CfgIfStatementNode extends CfgNode {

    private final List<CfgNode> nodes = new ArrayList<CfgNode>();

    private final List<CfgNode> prev = new ArrayList<CfgNode>();

    private final PsiExpression condition;

    public CfgIfStatementNode(PsiExpression condition) {
        this.condition = condition;
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
        return condition.getText();
    }

    public PsiExpression getSQLExpression(SQLExpressionFinder finder) {
        return finder.getSQLExpression(condition);
    }

}

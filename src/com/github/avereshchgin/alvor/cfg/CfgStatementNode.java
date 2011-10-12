package com.github.avereshchgin.alvor.cfg;

import com.github.avereshchgin.alvor.regex.SQLExpressionFinder;
import com.intellij.psi.PsiExpression;
import com.intellij.psi.PsiStatement;

import java.util.ArrayList;
import java.util.List;

public class CfgStatementNode extends CfgNode {

    private CfgNode next;

    private final List<CfgNode> prev = new ArrayList<CfgNode>();

    private final PsiStatement psiStatement;

    public CfgStatementNode(PsiStatement psiStatement) {
        this.psiStatement = psiStatement;
    }

    @Override
    public String toString() {
        return psiStatement.getText();
    }

    public void addOutgoingEdgeTo(CfgNode node) {
        next = node;
        node.addIncommingEdgeFrom(this);
    }

    public List<CfgNode> getOutgoingEdges() {
        List<CfgNode> ret = new ArrayList<CfgNode>();
        if (next != null) {
            ret.add(next);
        }
        return ret;
    }

    protected void addIncommingEdgeFrom(CfgNode node) {
        prev.add(node);
    }

    public List<CfgNode> getIncommingEdges() {
        return prev;
    }

    public PsiExpression getSQLExpression(SQLExpressionFinder finder) {
        return finder.getSQLExpression(psiStatement);
    }

}

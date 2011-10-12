package com.github.avereshchgin.alvor.cfg;

import com.github.avereshchgin.alvor.regex.SQLExpressionFinder;
import com.intellij.psi.PsiExpression;
import com.intellij.psi.PsiMethod;

import java.util.ArrayList;
import java.util.List;

public class CfgRootNode extends CfgNode {

    private CfgNode next;

    private final PsiMethod psiMethod;

    public CfgRootNode(PsiMethod psiMethod) {
        this.psiMethod = psiMethod;
    }

    @Override
    public String toString() {
        return psiMethod.getName();
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
    }

    public List<CfgNode> getIncommingEdges() {
        return new ArrayList<CfgNode>();
    }

    public PsiExpression getSQLExpression(SQLExpressionFinder finder) {
        return null;
    }

}

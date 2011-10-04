package com.github.avereshchgin.alvor.cfg;

import com.intellij.psi.PsiStatement;

import java.util.ArrayList;

public class CfgStatementNode extends CfgNode {

    public CfgStatementNode(PsiStatement psiStatement) {
        this.psiStatement = psiStatement;
    }

    @Override
    public String toString() {
        return psiStatement.getText();
    }

    public void addOutgoingEdge(CfgNode node) {
        next = node;
    }

    public ArrayList<CfgNode> getOutgoingEdges() {
        ArrayList<CfgNode> ret = new ArrayList<CfgNode>();
        if (next != null) {
            ret.add(next);
        }
        return ret;
    }

    private CfgNode next;

    private final PsiStatement psiStatement;
}

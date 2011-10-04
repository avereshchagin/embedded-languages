package com.github.avereshchgin.alvor.cfg;

import com.intellij.psi.PsiMethod;

import java.util.ArrayList;

public class CfgRootNode extends CfgNode {

    public CfgRootNode(PsiMethod psiMethod) {
        this.psiMethod = psiMethod;
    }

    @Override
    public String toString() {
        return psiMethod.getName();
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

    private final PsiMethod psiMethod;
}

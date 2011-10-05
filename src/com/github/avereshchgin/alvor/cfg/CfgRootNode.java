package com.github.avereshchgin.alvor.cfg;

import com.intellij.psi.PsiMethod;

import java.util.ArrayList;

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
    }

    public ArrayList<CfgNode> getOutgoingEdges() {
        ArrayList<CfgNode> ret = new ArrayList<CfgNode>();
        if (next != null) {
            ret.add(next);
        }
        return ret;
    }

}

package com.github.avereshchgin.alvor.cfg;

import com.intellij.psi.PsiMethod;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CfgRoot extends CfgNode {

    private CfgNode next;

    private final PsiMethod psiMethod;

    public CfgRoot(PsiMethod psiMethod) {
        this.psiMethod = psiMethod;
    }

    @Override
    public String toString() {
        return psiMethod.getName();
    }

    public void connectNext(CfgNode node) {
        next = node;
        node.connectPrevious(this);
    }

    public List<CfgNode> getNextNodes() {
        List<CfgNode> ret = new ArrayList<CfgNode>();
        if (next != null) {
            ret.add(next);
        }
        return ret;
    }

    protected void connectPrevious(CfgNode node) {
    }

    public List<CfgNode> getPreviousNodes() {
        return Collections.emptyList();
    }

}

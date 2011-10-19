package com.github.avereshchgin.alvor.cfg;

import com.intellij.psi.PsiReturnStatement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CfgReturnStatementNode extends CfgNode {

    private final PsiReturnStatement psiReturnStatement;

    private final List<CfgNode> prev = new ArrayList<CfgNode>();

    public CfgReturnStatementNode(PsiReturnStatement psiReturnStatement) {
        this.psiReturnStatement = psiReturnStatement;
    }

    @Override
    public String toString() {
        return psiReturnStatement.getText();
    }

    public void addOutgoingEdgeTo(CfgNode node) {
    }

    public List<CfgNode> getOutgoingEdges() {
        return Collections.emptyList();
    }

    protected void addIncommingEdgeFrom(CfgNode node) {
        prev.add(node);
    }

    public List<CfgNode> getIncommingEdges() {
        return prev;
    }

}

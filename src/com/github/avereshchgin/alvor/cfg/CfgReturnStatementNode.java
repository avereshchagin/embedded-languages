package com.github.avereshchgin.alvor.cfg;

import com.intellij.psi.PsiReturnStatement;

import java.util.ArrayList;

public class CfgReturnStatementNode extends CfgNode {

    public CfgReturnStatementNode(PsiReturnStatement psiReturnStatement) {
        this.psiReturnStatement = psiReturnStatement;
    }

    @Override
    public String toString() {
        return psiReturnStatement.getText();
    }

    public void addOutgoingEdge(CfgNode node) {
    }

    public ArrayList<CfgNode> getOutgoingEdges() {
        return new ArrayList<CfgNode>();
    }

    private final PsiReturnStatement psiReturnStatement;
}

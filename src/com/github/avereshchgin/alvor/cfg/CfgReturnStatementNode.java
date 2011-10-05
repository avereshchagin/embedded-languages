package com.github.avereshchgin.alvor.cfg;

import com.intellij.psi.PsiReturnStatement;

import java.util.ArrayList;

public class CfgReturnStatementNode extends CfgNode {

    private final PsiReturnStatement psiReturnStatement;

    public CfgReturnStatementNode(PsiReturnStatement psiReturnStatement) {
        this.psiReturnStatement = psiReturnStatement;
    }

    @Override
    public String toString() {
        return psiReturnStatement.getText();
    }

    public void addOutgoingEdgeTo(CfgNode node) {
    }

    public ArrayList<CfgNode> getOutgoingEdges() {
        return new ArrayList<CfgNode>();
    }

}

package com.github.avereshchgin.alvor.cfg;

import com.intellij.psi.PsiDeclarationStatement;

import java.util.ArrayList;
import java.util.List;

public class CfgDeclarationStatementNode extends CfgNode {

    private CfgNode next;

    private final List<CfgNode> prev = new ArrayList<CfgNode>();

    private final StringExpression stringExpression;

    public CfgDeclarationStatementNode(PsiDeclarationStatement declarationStatement) {
        stringExpression = new StringExpression(declarationStatement);
    }

    @Override
    public String toString() {
        return stringExpression.toString();
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

    public StringExpression getStringExpression() {
        return stringExpression;
    }

}

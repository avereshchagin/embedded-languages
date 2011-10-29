package com.github.avereshchgin.alvor.cfg;

import com.github.avereshchgin.alvor.strexp.StrexpAssignment;
import com.github.avereshchgin.alvor.strexp.StringExpressionBuilder;
import com.intellij.psi.PsiDeclarationStatement;

import java.util.ArrayList;
import java.util.List;

public class CfgDeclarationStatement extends CfgNode {

    private CfgNode next;

    private final List<CfgNode> prev = new ArrayList<CfgNode>();

    private final StringExpressionBuilder stringExpressionBuilder;

    public CfgDeclarationStatement(PsiDeclarationStatement declarationStatement) {
        stringExpressionBuilder = new StringExpressionBuilder(declarationStatement);
    }

    @Override
    public String toString() {
        return stringExpressionBuilder.toString();
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
        prev.add(node);
    }

    public List<CfgNode> getPreviousNodes() {
        return prev;
    }

    public StrexpAssignment getRootForVariable(String name) {
        if ("".equals(name)) {
            return stringExpressionBuilder.getAssignmentNode();
        }
        for (StrexpAssignment variable : stringExpressionBuilder.getModifiedVariables()) {
            if (name.equals(variable.getVariableName())) {
                return variable;
            }
        }
        return null;
    }
}

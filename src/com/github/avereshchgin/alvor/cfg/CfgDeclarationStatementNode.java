package com.github.avereshchgin.alvor.cfg;

import com.github.avereshchgin.alvor.strexp.StrexpRoot;
import com.github.avereshchgin.alvor.strexp.StringExpressionBuilder;
import com.intellij.psi.PsiDeclarationStatement;

import java.util.ArrayList;
import java.util.List;

public class CfgDeclarationStatementNode extends CfgNode {

    private CfgNode next;

    private final List<CfgNode> prev = new ArrayList<CfgNode>();

    private final StringExpressionBuilder stringExpressionBuilder;

    public CfgDeclarationStatementNode(PsiDeclarationStatement declarationStatement) {
        stringExpressionBuilder = new StringExpressionBuilder(declarationStatement);
    }

    @Override
    public String toString() {
        return stringExpressionBuilder.toString();
    }

    public void joinNext(CfgNode node) {
        next = node;
        node.joinPrevious(this);
    }

    public List<CfgNode> getNextNodes() {
        List<CfgNode> ret = new ArrayList<CfgNode>();
        if (next != null) {
            ret.add(next);
        }
        return ret;
    }

    protected void joinPrevious(CfgNode node) {
        prev.add(node);
    }

    public List<CfgNode> getPreviousNodes() {
        return prev;
    }

    public StrexpRoot getRootForVariable(String name) {
        if ("".equals(name)) {
            return stringExpressionBuilder.getRootNode();
        }
        for (StrexpRoot variable : stringExpressionBuilder.getModifiedVariables()) {
            if (name.equals(variable.getVariableName())) {
                return variable;
            }
        }
        return null;
    }
}

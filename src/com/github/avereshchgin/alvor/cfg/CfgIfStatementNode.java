package com.github.avereshchgin.alvor.cfg;

import com.github.avereshchgin.alvor.strexp.StrexpRoot;
import com.github.avereshchgin.alvor.strexp.StringExpressionBuilder;
import com.intellij.psi.PsiExpression;

import java.util.ArrayList;
import java.util.List;

public class CfgIfStatementNode extends CfgNode {

    private final List<CfgNode> nodes = new ArrayList<CfgNode>();

    private final List<CfgNode> prev = new ArrayList<CfgNode>();

    private final StringExpressionBuilder stringExpressionBuilder;

    public CfgIfStatementNode(PsiExpression expression) {
        stringExpressionBuilder = new StringExpressionBuilder(expression);
    }

    public void joinNext(CfgNode node) {
        nodes.add(node);
        node.joinPrevious(this);
    }

    public List<CfgNode> getNextNodes() {
        return nodes;
    }

    protected void joinPrevious(CfgNode node) {
        prev.add(node);
    }

    public List<CfgNode> getPreviousNodes() {
        return prev;
    }

    public String toString() {
        return stringExpressionBuilder.toString();
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

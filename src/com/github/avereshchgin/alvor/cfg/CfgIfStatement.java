package com.github.avereshchgin.alvor.cfg;

import com.github.avereshchgin.alvor.strexp.StrexpAssignment;
import com.github.avereshchgin.alvor.strexp.StringExpressionBuilder;
import com.intellij.psi.PsiExpression;

import java.util.ArrayList;
import java.util.List;

public class CfgIfStatement extends CfgNode {

    private final List<CfgNode> nodes = new ArrayList<CfgNode>();

    private final List<CfgNode> prev = new ArrayList<CfgNode>();

    private final StringExpressionBuilder stringExpressionBuilder;

    public CfgIfStatement(PsiExpression expression) {
        stringExpressionBuilder = new StringExpressionBuilder(expression);
    }

    public void connectNext(CfgNode node) {
        nodes.add(node);
        node.connectPrevious(this);
    }

    public List<CfgNode> getNextNodes() {
        return nodes;
    }

    protected void connectPrevious(CfgNode node) {
        prev.add(node);
    }

    public List<CfgNode> getPreviousNodes() {
        return prev;
    }

    public String toString() {
        return stringExpressionBuilder.toString();
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

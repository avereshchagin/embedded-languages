package com.github.avereshchgin.alvor.cfg;

import com.github.avereshchgin.alvor.regex.RegexAssignment;
import com.github.avereshchgin.alvor.regex.RegexExpression;
import com.github.avereshchgin.alvor.regex.RegexVariable;
import com.github.avereshchgin.alvor.regex.StatementExpressionBuilder;
import com.intellij.psi.PsiExpression;

import java.util.ArrayList;
import java.util.List;

public class CfgIfStatement extends CfgNode {

    private final List<CfgNode> nodes = new ArrayList<CfgNode>();

    private final List<CfgNode> prev = new ArrayList<CfgNode>();

    private final StatementExpressionBuilder statementExpressionBuilder;

    public CfgIfStatement(PsiExpression expression) {
        statementExpressionBuilder = new StatementExpressionBuilder(expression);
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
        return statementExpressionBuilder.toString();
    }

    public RegexExpression getRegexExpression() {
        return statementExpressionBuilder.getExpressionNode();
    }

    public RegexAssignment getAssignment(RegexVariable variable) {
        for (RegexAssignment assignment : statementExpressionBuilder.getModifiedVariables()) {
            if (variable.equals(assignment.getVariable())) {
                return assignment;
            }
        }
        return null;
    }
}

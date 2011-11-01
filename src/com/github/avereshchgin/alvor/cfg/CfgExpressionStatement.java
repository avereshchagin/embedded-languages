package com.github.avereshchgin.alvor.cfg;

import com.github.avereshchgin.alvor.regex.RegexAssignment;
import com.github.avereshchgin.alvor.regex.RegexExpression;
import com.github.avereshchgin.alvor.regex.RegexVariable;
import com.github.avereshchgin.alvor.regex.StatementExpressionBuilder;
import com.intellij.psi.PsiExpression;

import java.util.ArrayList;
import java.util.List;

public class CfgExpressionStatement extends CfgNode {

    private CfgNode next;

    private final List<CfgNode> prev = new ArrayList<CfgNode>();

    private final StatementExpressionBuilder statementExpressionBuilder;

    private boolean verificationRequired;

    public CfgExpressionStatement(PsiExpression expression) {
        statementExpressionBuilder = new StatementExpressionBuilder(expression);
    }

    public boolean isVerificationRequired() {
        return verificationRequired;
    }

    public void setVerificationRequired(boolean verificationRequired) {
        this.verificationRequired = verificationRequired;
    }

    @Override
    public String toString() {
        return statementExpressionBuilder.toString();
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

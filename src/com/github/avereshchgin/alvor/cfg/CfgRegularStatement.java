package com.github.avereshchgin.alvor.cfg;

import com.github.avereshchgin.alvor.regex.RegexAssignment;
import com.github.avereshchgin.alvor.regex.RegexExpression;
import com.github.avereshchgin.alvor.regex.RegexVariable;
import com.github.avereshchgin.alvor.regex.StatementExpressionBuilder;
import com.intellij.psi.PsiExpression;

public class CfgRegularStatement extends CfgStatement {

    private final StatementExpressionBuilder statementExpressionBuilder;

    private boolean verificationRequired;

    public CfgRegularStatement(PsiExpression expression) {
        statementExpressionBuilder = new StatementExpressionBuilder(expression);
    }

    @Override
    public boolean isVerificationRequired() {
        return verificationRequired;
    }

    @Override
    public void setVerificationRequired(boolean verificationRequired) {
        this.verificationRequired = verificationRequired;
    }

    @Override
    public RegexExpression getRegexExpression() {
        return statementExpressionBuilder.getExpressionNode();
    }

    @Override
    public RegexAssignment getAssignment(RegexVariable variable) {
        for (RegexAssignment assignment : statementExpressionBuilder.getModifiedVariables()) {
            if (variable.equals(assignment.getVariable())) {
                return assignment;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return statementExpressionBuilder.toString();
    }
}

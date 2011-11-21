package com.github.avereshchagin.emblang.cfg;

import com.github.avereshchagin.emblang.regex.RegexAssignment;
import com.github.avereshchagin.emblang.regex.RegexExpression;
import com.github.avereshchagin.emblang.regex.RegexVariable;

public class CfgRegularStatement extends CfgStatement {

//    private final StatementExpressionBuilder statementExpressionBuilder;

    private String what;

    private boolean verificationRequired;

    public CfgRegularStatement(String what) {
//        statementExpressionBuilder = new StatementExpressionBuilder(expression);
        this.what = what;
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
//        return statementExpressionBuilder.getExpressionNode();
        return null;
    }

    @Override
    public RegexAssignment getAssignment(RegexVariable variable) {
//        for (RegexAssignment assignment : statementExpressionBuilder.getModifiedVariables()) {
//            if (variable.equals(assignment.getVariable())) {
//                return assignment;
//            }
//        }
        return null;
    }

    @Override
    public String toString() {
        return what;//statementExpressionBuilder.toString();
    }
}
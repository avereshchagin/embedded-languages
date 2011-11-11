package com.github.avereshchgin.alvor.cfg;

import com.github.avereshchgin.alvor.regex.RegexAssignment;
import com.github.avereshchgin.alvor.regex.RegexExpression;
import com.github.avereshchgin.alvor.regex.RegexVariable;

public class CfgRootStatement extends CfgStatement {

    private final String methodName;

    public CfgRootStatement(String methodName) {
        this.methodName = methodName;
    }

    @Override
    public boolean isVerificationRequired() {
        return false;
    }

    @Override
    public void setVerificationRequired(boolean verificationRequired) {

    }

    @Override
    public RegexExpression getRegexExpression() {
        return null;
    }

    @Override
    public RegexAssignment getAssignment(RegexVariable variable) {
        return null;
    }

    @Override
    public String toString() {
        return methodName;
    }
}

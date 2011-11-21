package com.github.avereshchagin.emblang.cfg;

import com.github.avereshchagin.emblang.regex.RegexAssignment;
import com.github.avereshchagin.emblang.regex.RegexExpression;
import com.github.avereshchagin.emblang.regex.RegexVariable;

public class CfgReturnStatement extends CfgStatement {

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
        return "return";
    }
}

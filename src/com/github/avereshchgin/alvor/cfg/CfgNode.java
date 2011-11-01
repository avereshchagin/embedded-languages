package com.github.avereshchgin.alvor.cfg;

import com.github.avereshchgin.alvor.regex.RegexAssignment;
import com.github.avereshchgin.alvor.regex.RegexExpression;
import com.github.avereshchgin.alvor.regex.RegexVariable;

import java.util.List;

public abstract class CfgNode {

    public abstract void connectNext(CfgNode node);

    public abstract List<CfgNode> getNextNodes();

    protected abstract void connectPrevious(CfgNode node);

    public abstract List<CfgNode> getPreviousNodes();

    public boolean isVerificationRequired() {
        return false;
    }

    public RegexExpression getRegexExpression() {
        return null;
    }

    public RegexAssignment getAssignment(RegexVariable variable) {
        return null;
    }
}

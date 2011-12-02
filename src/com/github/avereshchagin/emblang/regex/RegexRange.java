package com.github.avereshchagin.emblang.regex;

import java.util.Collections;
import java.util.Set;

public class RegexRange implements RegexNode {

    private final char start;

    private final char end;

    public RegexRange(char start, char end) {
        assert start <= end;
        this.start = start;
        this.end = end;
    }

    public void connectNode(RegexNode node) {

    }

    public <E> E accept(RegularExpressionVisitor<E> visitor) {
        return visitor.visitRange(this);
    }

    public Set<RegexVariable> findUsedVariables() {
        return Collections.emptySet();
    }

    @Override
    public String toString() {
        return "[" + start + "-" + end + "]";
    }
}

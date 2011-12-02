package com.github.avereshchagin.emblang.regex;

import java.util.Collections;
import java.util.Set;

public class RegexEmpty implements RegexNode {

    public void connectNode(RegexNode node) {

    }

    public <E> E accept(RegularExpressionVisitor<E> visitor) {
        return visitor.visitEmpty(this);
    }

    public Set<RegexVariable> findUsedVariables() {
        return Collections.emptySet();
    }

    @Override
    public String toString() {
        return "\"\"";
    }
}

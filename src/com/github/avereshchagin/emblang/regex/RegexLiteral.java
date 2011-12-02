package com.github.avereshchagin.emblang.regex;

import java.util.Collections;
import java.util.Set;

public class RegexLiteral implements RegexNode {

    private final String literal;

    public RegexLiteral(String literal) {
        this.literal = literal;
    }

    public void connectNode(RegexNode node) {

    }

    public <E> E accept(RegularExpressionVisitor<E> visitor) {
        return visitor.visitLiteral(this);
    }

    public Set<RegexVariable> findUsedVariables() {
        return Collections.emptySet();
    }

    public String getLiteral() {
        return literal;
    }

    public String toString() {
        return "\"" + literal + "\"";
    }
}

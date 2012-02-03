package com.github.avereshchagin.emblang.regex;

import java.util.Collections;
import java.util.Set;

public class RegexVariable implements RegexNode {

    private final String name;

    public RegexVariable(String name) {
        if (name != null && !name.isEmpty()) {
            this.name = name;
        } else {
            this.name = "tmp_" + System.identityHashCode(this);
        }
    }

    public void connectNode(RegexNode node) {
    }

    public <E> E accept(RegularExpressionVisitor<E> visitor) {
        return visitor.visitVariable(this);
    }

    public Set<RegexVariable> findUsedVariables() {
        return Collections.singleton(this);
    }

//    public String getName() {
//        return name;
//    }

    @Override
    public String toString() {
        return "<" + name + ">";
    }
}

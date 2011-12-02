package com.github.avereshchagin.emblang.regex;

import java.util.Set;

public interface RegexNode {

    public void connectNode(RegexNode node);

    public <E> E accept(RegularExpressionVisitor<E> visitor);

    public Set<RegexVariable> findUsedVariables();
}

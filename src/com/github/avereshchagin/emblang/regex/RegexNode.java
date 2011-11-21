package com.github.avereshchagin.emblang.regex;

public interface RegexNode {

    public void connectNode(RegexNode node);

    public <E> E accept(RegularExpressionVisitor<E> visitor);
}

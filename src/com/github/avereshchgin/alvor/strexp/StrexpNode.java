package com.github.avereshchgin.alvor.strexp;

public interface StrexpNode {

    public void joinNode(StrexpNode node);

    public <E> E accept(StringExpressionVisitor<E> visitor);
}

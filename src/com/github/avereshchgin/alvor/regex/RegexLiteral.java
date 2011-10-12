package com.github.avereshchgin.alvor.regex;

import com.intellij.psi.PsiLiteralExpression;

import java.util.ArrayList;
import java.util.List;

public class RegexLiteral extends RegexNode {

    private final PsiLiteralExpression literalExpression;

    public RegexLiteral(PsiLiteralExpression literalExpression) {
        this.literalExpression = literalExpression;
    }

    public List<String> getReferences() {
        return new ArrayList<String>();
    }

    public String toString() {
        return literalExpression.getText();
    }
}

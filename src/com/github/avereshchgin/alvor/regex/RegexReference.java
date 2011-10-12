package com.github.avereshchgin.alvor.regex;

import com.intellij.psi.PsiReference;

import java.util.ArrayList;
import java.util.List;

public class RegexReference extends RegexNode {

    private final PsiReference reference;

    public RegexReference(PsiReference reference) {
        this.reference = reference;
    }

    public List<String> getReferences() {
        List<String> references = new ArrayList<String>();
        references.add(reference.getCanonicalText());
        return references;
    }

    public String toString() {
        return "<" + reference.getCanonicalText() + ">";
    }
}

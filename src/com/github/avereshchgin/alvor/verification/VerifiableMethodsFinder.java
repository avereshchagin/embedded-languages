package com.github.avereshchgin.alvor.verification;

import com.intellij.psi.PsiMethod;

public interface VerifiableMethodsFinder {

    public boolean isVerificationRequired(PsiMethod method);
}

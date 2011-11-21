package com.github.avereshchagin.emblang.verification;

import com.intellij.psi.PsiMethod;

public interface VerifiableMethodsFinder {

    public boolean isVerificationRequired(PsiMethod method);
}

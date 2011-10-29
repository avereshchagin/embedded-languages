package com.github.avereshchgin.alvor.verification;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class JDBCMethodsFinder implements VerifiableMethodsFinder {

    private final String CONNECTION_CLASS_NAME = "java.sql.Connection";

    private final Set<String> SQL_METHODS = new HashSet<String>(Arrays.asList(
            "prepareStatement",
            "prepareCall",
            "executeQuery",
            "executeUpdate"
    ));

    public boolean isVerificationRequired(PsiMethod method) {
        if (method != null) {
            PsiClass containingClass = method.getContainingClass();
            if (containingClass != null) {
                if (CONNECTION_CLASS_NAME.equals(containingClass.getQualifiedName())) {
                    return SQL_METHODS.contains(method.getName());
                }
            }
        }
        return false;
    }
}

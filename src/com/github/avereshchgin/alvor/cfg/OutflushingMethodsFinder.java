package com.github.avereshchgin.alvor.cfg;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiMethodCallExpression;

import java.util.Arrays;

public class OutflushingMethodsFinder {

    final String CONNECTION_CLASS_NAME = "java.sql.Connection";

    final String[] SQL_METHODS = {"prepareStatement", "prepareCall", "executeQuery", "executeUpdate"};

    public boolean isOutflushingMethod(PsiMethodCallExpression methodCallExpression) {
        PsiMethod method = methodCallExpression.resolveMethod();
        if (method != null) {
            PsiClass containingClass = method.getContainingClass();
            if (containingClass != null) {
                if (CONNECTION_CLASS_NAME.equals(containingClass.getQualifiedName())) {
                    if (Arrays.asList(SQL_METHODS).indexOf(method.getName()) != -1) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}

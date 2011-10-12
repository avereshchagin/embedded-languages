package com.github.avereshchgin.alvor.regex;

import com.intellij.psi.*;

public class SQLExpressionFinder {

    private class MethodCallVisitor extends JavaRecursiveElementWalkingVisitor {

        private final String methodName;

        private PsiExpression expression;

        public MethodCallVisitor(String methodName) {
            this.methodName = methodName;
        }

        public PsiExpression getExpression() {
            return expression;
        }

        @Override
        public void visitElement(PsiElement element) {
            if (element instanceof PsiMethodCallExpression) {
                PsiMethodCallExpression psiMethodCallExpression = (PsiMethodCallExpression) element;
                PsiReferenceExpression psiReferenceExpression = psiMethodCallExpression.getMethodExpression();

                boolean isRightIdentifier = false;
                for (PsiElement referenceChild : psiReferenceExpression.getChildren()) {
                    if ((referenceChild instanceof PsiIdentifier) && (referenceChild.getText().equals(methodName))) {
                        isRightIdentifier = true;
                        break;
                    }
                }

                if (isRightIdentifier) {
                    PsiExpression[] expressions = psiMethodCallExpression.getArgumentList().getExpressions();
                    if (expressions.length == 1) {
                        expression = expressions[0];
                    }
                }
            }
            super.visitElement(element);
        }
    }

    private PsiExpression getMethodArgumentExpression(PsiElement element, String methodName) {
        MethodCallVisitor visitor = new MethodCallVisitor(methodName);
        element.accept(visitor);
        return visitor.getExpression();
    }

    public PsiExpression getSQLExpression(PsiElement element) {
        String[] methodNames = {"prepareStatement", "prepareCall", "executeQuery", "executeUpdate"};
        for (String methodName : methodNames) {
            PsiExpression expression = getMethodArgumentExpression(element, methodName);
            if (expression != null) {
                return expression;
            }
        }
        return null;
    }
}

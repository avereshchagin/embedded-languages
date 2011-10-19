package com.github.avereshchgin.alvor.cfg;

import com.intellij.psi.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CfgExpressionStatementNode extends CfgNode {

    private CfgNode next;

    private final List<CfgNode> prev = new ArrayList<CfgNode>();

    private final StringExpression stringExpression;

    private final boolean hasSqlMethodCall;

    public CfgExpressionStatementNode(PsiExpressionStatement expressionStatement) {
        boolean localHasSqlMethodCall = false;

        PsiExpression expression = expressionStatement.getExpression();
        if (expression instanceof PsiMethodCallExpression) {
            PsiMethodCallExpression methodCallExpression = ((PsiMethodCallExpression) expression);

            final String CONNECTION_CLASS_NAME = "java.sql.Connection";

            final String[] SQL_METHODS = {"prepareStatement", "prepareCall", "executeQuery", "executeUpdate"};

            PsiMethod method = methodCallExpression.resolveMethod();
            if (method != null) {
                PsiClass containingClass = method.getContainingClass();
                if (containingClass != null) {
                    System.out.println("Method: " + containingClass.getQualifiedName() + "." + method.getName());
                    if (CONNECTION_CLASS_NAME.equals(containingClass.getQualifiedName())) {
                        if (Arrays.asList(SQL_METHODS).indexOf(method.getName()) != -1) {
                            System.out.println("SQL method found");
                            localHasSqlMethodCall = true;
                        }
                    }
                }
                PsiExpression[] expressions = methodCallExpression.getArgumentList().getExpressions();
                if (expressions.length > 0) {
                    System.out.println(expressions[0].getText());
                    stringExpression = new StringExpression(expressions[0]);
                    System.out.println(stringExpression.toString());
                } else {
                    stringExpression = new StringExpression();
                }
            } else {
                stringExpression = new StringExpression();
            }
        } else {
            stringExpression = new StringExpression(expressionStatement);
        }
        hasSqlMethodCall = localHasSqlMethodCall;
    }

    @Override
    public String toString() {
        return stringExpression.toString();
    }

    public void addOutgoingEdgeTo(CfgNode node) {
        next = node;
        node.addIncommingEdgeFrom(this);
    }

    public List<CfgNode> getOutgoingEdges() {
        List<CfgNode> ret = new ArrayList<CfgNode>();
        if (next != null) {
            ret.add(next);
        }
        return ret;
    }

    protected void addIncommingEdgeFrom(CfgNode node) {
        prev.add(node);
    }

    public List<CfgNode> getIncommingEdges() {
        return prev;
    }

    public StringExpression getStringExpression() {
        return stringExpression;
    }

    public boolean getHasSqlMethodCall() {
        return hasSqlMethodCall;
    }

}

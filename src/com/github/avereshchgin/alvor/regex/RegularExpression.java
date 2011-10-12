package com.github.avereshchgin.alvor.regex;

import com.github.avereshchgin.alvor.cfg.CfgNode;
import com.intellij.psi.PsiBinaryExpression;
import com.intellij.psi.PsiExpression;
import com.intellij.psi.PsiLiteralExpression;
import com.intellij.psi.PsiReferenceExpression;

import java.util.List;

public class RegularExpression {

    private RegexNode rootNode;

    public RegexNode processExpression(PsiExpression expression) {
        if (expression instanceof PsiLiteralExpression) {
            return new RegexLiteral((PsiLiteralExpression) expression);
        } else if (expression instanceof PsiReferenceExpression) {
            return new RegexReference((PsiReferenceExpression) expression);
        } else if (expression instanceof PsiBinaryExpression) {
            PsiBinaryExpression binaryExpression = (PsiBinaryExpression) expression;
            if (binaryExpression.getOperationSign().getText().equals("+")) {
                return new RegexConcatenation(processExpression(binaryExpression.getLOperand()),
                        processExpression(binaryExpression.getROperand()));
            } else {
                System.out.println("Invalid string operation.");
            }
//        } else if (expression instanceof PsiPolyadicExpression) {
//            PsiPolyadicExpression polyadicExpression = (PsiPolyadicExpression) expression;
//            for (PsiExpression operand : polyadicExpression.getOperands()) {
//                PsiJavaToken javaToken = polyadicExpression.getTokenBeforeOperand(operand);
//                if (javaToken != null) {
//                    System.out.println("Token: " + javaToken.getText());
//                }
//                System.out.println("Operand: " + operand.getText());
//            }
        }
        return new RegexEmpty();
    }

    private void update(CfgNode cfgNode) {
        List<String> references = rootNode.getReferences();
        if ((references.size() == 0) || (cfgNode == null)) {
            return;
        }
    }

    public void buildRegularExpression(PsiExpression expression, CfgNode cfgNode) {
        rootNode = processExpression(expression);
        update(cfgNode.getIncommingEdges().get(0));
    }

    public String toString() {
        return (rootNode != null) ? rootNode.toString() : "";
    }
}

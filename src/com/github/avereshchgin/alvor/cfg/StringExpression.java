package com.github.avereshchgin.alvor.cfg;

import com.intellij.psi.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StringExpression {

    public interface StringExpressionElement {

    }

    public class StringVariable implements StringExpressionElement {

        private final String name;

        private final boolean isDeclaration;

        public StringVariable(String name) {
            this.name = name;
            this.isDeclaration = declaredVariables.indexOf(name) != -1;
        }

        public String toString() {
            return "<" + name + ">";
        }

        public boolean equals(Object o) {
            if ((o == null) || !(o instanceof StringVariable)) {
                return false;
            }
            return name.equals(((StringVariable) o).name);
        }
    }

    public class StringLiteral implements StringExpressionElement {

        private final String literal;

        public StringLiteral(String literal) {
            this.literal = literal.substring(1, literal.length() - 1);
        }

        public String getLiteral() {
            return literal;
        }

        public String toString() {
            return "\"" + literal + "\"";
        }
    }

    private final List<StringExpressionElement> elements = new ArrayList<StringExpressionElement>();

    private final List<StringVariable> modifiedVariables = new ArrayList<StringVariable>();

    private final List<String> declaredVariables = new ArrayList<String>();

    private void processLiteralExpression(PsiLiteralExpression literalExpression) {
        if (literalExpression == null) {
            return;
        }
        System.out.println("Literal expression: " + literalExpression.getText());
        PsiType type = literalExpression.getType();
        if ("java.lang.String".equals(type.getCanonicalText())) {
            elements.add(new StringLiteral(literalExpression.getText()));
        } else {
            System.out.println("Unknown type: " + type.getCanonicalText());
        }
    }

    private void processReferenceExpression(PsiReferenceExpression referenceExpression) {
        if (referenceExpression == null) {
            return;
        }
        System.out.println("Reference expression: " + referenceExpression.getText());
        PsiElement reference = referenceExpression.resolve();
        if (reference instanceof PsiLocalVariable) {
            PsiLocalVariable localVariable = (PsiLocalVariable) reference;
            PsiType type = localVariable.getType();
            if ("java.lang.String".equals(type.getCanonicalText())) {
                elements.add(new StringVariable(localVariable.getName()));
            } else {
                System.out.println("Unknown type: " + type.getCanonicalText());
            }
        } else {
            System.out.println("Unknown reference: " + reference);
        }
    }

    private void processExpressionOperand(PsiExpression operand) {
        if (operand == null) {
            return;
        }
        System.out.println("Expression operand: " + operand.getText());
        if (operand instanceof PsiLiteralExpression) {
            processLiteralExpression((PsiLiteralExpression) operand);
        } else if (operand instanceof PsiReferenceExpression) {
            processReferenceExpression((PsiReferenceExpression) operand);
        } else {
            System.out.println("Unknown operand: " + operand);
        }
    }

    private void processBinaryExpression(PsiBinaryExpression binaryExpression) {
        if (binaryExpression == null) {
            return;
        }
        System.out.println("Binary expression: " + binaryExpression.getText());
        System.out.println("Expression type: " + binaryExpression.getType().getCanonicalText());
        processExpressionOperand(binaryExpression.getLOperand());
        processExpressionOperand(binaryExpression.getROperand());
    }

    private void processPolyadicExpression(PsiPolyadicExpression polyadicExpression) {
        if (polyadicExpression == null) {
            return;
        }
        System.out.println("Polyadic expression: " + polyadicExpression.getText());
        System.out.println("Expression type: " + polyadicExpression.getType().getCanonicalText());
        for (PsiExpression operand : polyadicExpression.getOperands()) {
            processExpressionOperand(operand);
        }
    }

    private void processAssignmentExpression(PsiAssignmentExpression assignmentExpression) {
        if (assignmentExpression == null) {
            return;
        }
        System.out.println("Assignment expression: " + assignmentExpression.getText());
        PsiExpression leftExpression = assignmentExpression.getLExpression();
        if (leftExpression instanceof PsiReferenceExpression) {
            PsiElement reference = ((PsiReferenceExpression) leftExpression).resolve();
            if (reference instanceof PsiLocalVariable) {
                PsiLocalVariable localVariable = (PsiLocalVariable) reference;
                PsiType type = localVariable.getType();
                if ("java.lang.String".equals(type.getCanonicalText())) {
                    modifiedVariables.add(new StringVariable(localVariable.getName()));
                } else {
                    System.out.println("Unknown type: " + type.getCanonicalText());
                }
            } else {
                System.out.println("Unknown reference: " + reference);
            }
        }
        processExpression(assignmentExpression.getRExpression());
    }

    private void processExpression(PsiExpression expression) {
        if (expression == null) {
            return;
        }
        System.out.println("Expression: " + expression.getText());
        if (expression instanceof PsiAssignmentExpression) {
            processAssignmentExpression((PsiAssignmentExpression) expression);
        } else if (expression instanceof PsiLiteralExpression) {
            processLiteralExpression((PsiLiteralExpression) expression);
        } else if (expression instanceof PsiBinaryExpression) {
            processBinaryExpression((PsiBinaryExpression) expression);
        } else if (expression instanceof PsiPolyadicExpression) {
            processPolyadicExpression((PsiPolyadicExpression) expression);
        } else if (expression instanceof PsiReferenceExpression) {
            processReferenceExpression((PsiReferenceExpression) expression);
        } else {
            System.out.println("Unknown expression: " + expression);
        }
    }

    public StringExpression(PsiExpression expression) {
        processExpression(expression);
        System.out.println();
    }

    public StringExpression() {

    }

    public StringExpression(PsiExpressionStatement expressionStatement) {
        System.out.println("Expression statement: " + expressionStatement.getText());
        processExpression(expressionStatement.getExpression());
        System.out.println();
    }

    public StringExpression(PsiDeclarationStatement declarationStatement) {
        System.out.println("Declaration statement: " + declarationStatement.getText());
        for (PsiElement element : declarationStatement.getDeclaredElements()) {
            if (element instanceof PsiLocalVariable) {
                PsiLocalVariable localVariable = (PsiLocalVariable) element;
                PsiType type = localVariable.getType();
                if ("java.lang.String".equals(type.getCanonicalText())) {
                    declaredVariables.add(localVariable.getName());
                    modifiedVariables.add(new StringVariable(localVariable.getName()));
                } else {
                    System.out.println("Unknown type: " + type.getCanonicalText());
                }
                processExpression(localVariable.getInitializer());
            } else {
                System.out.println("Unknown element: " + element);
            }
        }
        System.out.println();
    }

    public String toString() {
        String result = "";

        result += "[";
        for (StringVariable stringVariable : modifiedVariables) {
            if (stringVariable.isDeclaration) {
                result += "dec:";
            }
            result += stringVariable + ",";
        }
        result += "] = ";

        for (StringExpressionElement element : elements) {
            result += element + ".";
        }

        return result;
    }

    public List<StringVariable> getModifiedVariables() {
        return Collections.unmodifiableList(modifiedVariables);
    }

    public List<StringExpressionElement> getElements() {
        return Collections.unmodifiableList(elements);
    }
}

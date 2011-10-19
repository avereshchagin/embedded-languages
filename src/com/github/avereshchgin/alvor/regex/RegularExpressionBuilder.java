package com.github.avereshchgin.alvor.regex;

import com.github.avereshchgin.alvor.cfg.CfgNode;
import com.github.avereshchgin.alvor.cfg.StringExpression;

import java.util.List;

public class RegularExpressionBuilder {

    private static RegexNode buildNodeByStringExpression(StringExpression stringExpression, CfgNode statement) {
        RegexNode oldNode = new RegexEmpty();
        for (StringExpression.StringExpressionElement element : stringExpression.getElements()) {
            if (element instanceof StringExpression.StringLiteral) {
                oldNode = new RegexConcatenation(oldNode,
                        new RegexLiteral(((StringExpression.StringLiteral) element).getLiteral()));

            } else if (element instanceof StringExpression.StringVariable) {
                oldNode = new RegexConcatenation(oldNode,
                        getNodeForVariable((StringExpression.StringVariable) element, statement));
            }
        }
        return oldNode;
    }

    private static RegexNode getNodeForVariable(StringExpression.StringVariable variable, CfgNode statement) {
        List<CfgNode> statements = statement.getIncommingEdges();
        statement = statements.get(0);
        while (statements.size() == 1) {
            StringExpression stringExpression = statement.getStringExpression();
            List<StringExpression.StringVariable> variables = stringExpression.getModifiedVariables();
            if (variables.indexOf(variable) != -1) {
                return buildNodeByStringExpression(stringExpression, statement);
            }
            statements = statement.getIncommingEdges();
        }

//        if (statements.size() > 1) {
//            RegexNode oldNode = new RegexAlternation(buidNodeForLinearBlock(variable, statements.get(0)),
//                    buidNodeForLinearBlock(variable, statements.get(1)));
//            for (int i = 2; i < statements.size(); i++) {
//                oldNode = new RegexAlternation(oldNode, buidNodeForLinearBlock(variable, statements.get(i)));
//            }
//            return oldNode;
//        }
        return new RegexEmpty();
    }

    public static RegexNode buildRegularException(CfgNode statement) {
        if (!statement.getHasSqlMethodCall()) {
            return null;
        }
        return buildNodeByStringExpression(statement.getStringExpression(), statement);
    }
}

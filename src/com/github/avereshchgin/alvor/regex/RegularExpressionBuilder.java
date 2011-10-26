package com.github.avereshchgin.alvor.regex;

import com.github.avereshchgin.alvor.cfg.CfgNode;
import com.github.avereshchgin.alvor.strexp.*;

import java.util.List;

public class RegularExpressionBuilder {

    public static RegexNode processStrexpNode(StrexpNode node, final CfgNode startStatement) {
        log("processStrexpNode");
        return node.accept(new StringExpressionVisitor<RegexNode>() {
            @Override
            public RegexNode visitConcatenation(StrexpConcatenation concatenation) {
                log("Concatenation: " + concatenation);
                List<StrexpNode> childNodes = concatenation.getChildNodes();
                if (childNodes.size() == 0) {
                    return new RegexEmpty();
                }
                if (childNodes.size() == 1) {
                    return processStrexpNode(childNodes.get(0), startStatement);
                }
                RegexNode currentRegex = new RegexConcatenation(processStrexpNode(childNodes.get(0), startStatement),
                        processStrexpNode(childNodes.get(1), startStatement));
                for (int i = 2; i < childNodes.size(); i++) {
                    currentRegex = new RegexConcatenation(currentRegex, processStrexpNode(childNodes.get(i), startStatement));
                }
                return currentRegex;
            }

            @Override
            public RegexNode visitLiteral(StrexpLiteral literal) {
                log("Literal: " + literal);
                return new RegexLiteral(literal.getLiteral());
            }

            @Override
            public RegexNode visitVariable(StrexpVariable variable) {
                log("Variable: " + variable);
                return processBranchingNode(variable.getName(), startStatement);
            }

            @Override
            public RegexNode visitRoot(StrexpRoot root) {
                log("Root: " + root);
                List<StrexpNode> childNodes = root.getChildNodes();
                if (childNodes.size() > 0) {
                    return processStrexpNode(childNodes.get(0), startStatement);
                } else {
                    return new RegexEmpty();
                }
            }

            @Override
            public RegexNode visitAnyNode(StrexpNode node) {
                log("Any node: " + node);
                return new RegexEmpty();
            }
        });
    }

    public static RegexNode processLinearNode(String variable, CfgNode statement) {
        StrexpRoot variableRoot;
        log("currentRegex statement: " + statement);
        if ((variableRoot = statement.getRootForVariable(variable)) == null) {
            return processBranchingNode(variable, statement);
        }
        return processStrexpNode(variableRoot, statement);
    }

    public static RegexNode processBranchingNode(String variable, CfgNode startStatement) {
        log("processBranchingNode");
        List<CfgNode> previousStatements = startStatement.getPreviousNodes();
        if (previousStatements.size() == 0) {
            return new RegexEmpty();
        } else if (previousStatements.size() == 1) {
            return processLinearNode(variable, previousStatements.get(0));
        } else {
            RegexNode currentRegex = new RegexAlternation(processLinearNode(variable, previousStatements.get(0)),
                    processLinearNode(variable, previousStatements.get(1)));
            for (int i = 2; i < previousStatements.size(); i++) {
                currentRegex = new RegexConcatenation(currentRegex, processLinearNode(variable, previousStatements.get(i)));
            }
            return currentRegex;
        }
    }

    public static RegexNode buildRegularExpression(CfgNode statement) {
        return processStrexpNode(statement.getRootForVariable(""), statement);
    }

    public static void log(String message) {
//        System.out.println(message);
    }
}

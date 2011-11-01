package com.github.avereshchgin.alvor.regex;

import com.github.avereshchgin.alvor.cfg.CfgNode;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class RegularExpressionBuilder {

    public static RegexNode processRegexNode(final RegexNode node, final CfgNode startStatement) {
        log("processRegexNode");
        return node.accept(new RegularExpressionVisitor<RegexNode>() {

            @Override
            public RegexNode visitConcatenation(RegexConcatenation concatenation) {
                log("Concatenation: " + concatenation);
                List<RegexNode> childNodes = concatenation.getChildNodes();
                Iterator<RegexNode> iterator = childNodes.iterator();
                if (iterator.hasNext()) {
                    RegexNode currentNode = processRegexNode(iterator.next(), startStatement);
                    while (iterator.hasNext()) {
                        // TODO: rewrite as .connectNode
                        currentNode = new RegexConcatenation(currentNode, processRegexNode(iterator.next(), startStatement));
                    }
                    return currentNode;
                }
                return new RegexEmpty();
            }

            @Override
            public RegexNode visitLiteral(RegexLiteral literal) {
                log("Literal: " + literal);
                return literal;
            }

            @Override
            public RegexNode visitVariable(RegexVariable variable) {
                log("Variable: " + variable);
                return fork(startStatement.getPreviousNodes(), variable);
            }

            @Override
            public RegexNode visitAssignment(RegexAssignment assignment) {
                log("Assignment: " + assignment);
                return processRegexNode(assignment.getChildNode(), startStatement);
            }

            @Override
            public RegexNode visitAlternation(RegexAlternation alternation) {
                log("Alternation: " + alternation);
                List<RegexNode> childNodes = alternation.getChildNodes();
                Iterator<RegexNode> iterator = childNodes.iterator();
                if (iterator.hasNext()) {
                    RegexNode currentNode = processRegexNode(iterator.next(), startStatement);
                    while (iterator.hasNext()) {
                        // TODO: rewrite as .connectNode
                        currentNode = new RegexAlternation(currentNode, processRegexNode(iterator.next(), startStatement));
                    }
                    return currentNode;
                }
                return new RegexEmpty();
            }

            @Override
            public RegexNode visitEmpty(RegexEmpty empty) {
                return empty;
            }

            @Override
            public RegexNode visitRange(RegexRange range) {
                return range;
            }

            @Override
            public RegexNode visitExpression(RegexExpression expression) {
                log("Expression: " + expression);
                ListIterator<RegexNode> iterator = expression.getChildNodes().listIterator(expression.getChildNodes().size());
                if (iterator.hasPrevious()) {
                    return processRegexNode(iterator.previous(), startStatement);
                }
                return new RegexEmpty();
            }

            @Override
            public RegexNode visitStar(RegexStar star) {
                return star;
            }

            @Override
            public RegexNode visitPlus(RegexPlus plus) {
                return plus;
            }

            @Override
            public RegexNode visitAnyNode(RegexNode node) {
                log("Unsupported node: " + node);
                return new RegexEmpty();
            }
        });
    }

    public static RegexNode processStatement(CfgNode statement, RegexVariable variable) {
        log("Process statement: " + statement);
        RegexAssignment assignment = statement.getAssignment(variable);
        if (assignment != null) {
            return processRegexNode(assignment, statement);
        }
        return fork(statement.getPreviousNodes(), variable);
    }

    public static RegexNode fork(List<CfgNode> previousStatements, RegexVariable variable) {
        log("Fork");
        RegexNode currentRegex = new RegexEmpty();
        Iterator<CfgNode> iterator = previousStatements.iterator();
        if (iterator.hasNext()) {
            currentRegex = processStatement(iterator.next(), variable);
            while (iterator.hasNext()) {
                currentRegex = new RegexAlternation(currentRegex, processStatement(iterator.next(), variable));
            }
        }
        return currentRegex;
    }

    public static RegexNode buildRegularExpression(CfgNode statement) {
        return processRegexNode(statement.getRegexExpression(), statement);
    }

    public static void log(String message) {
//        System.out.println(message);
    }
}

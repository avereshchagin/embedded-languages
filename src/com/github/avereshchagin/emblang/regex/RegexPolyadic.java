package com.github.avereshchagin.emblang.regex;

import java.util.*;

public abstract class RegexPolyadic implements RegexNode {

    protected final List<RegexNode> childNodes = new ArrayList<RegexNode>();

    protected RegexPolyadic(RegexNode... nodes) {
        childNodes.addAll(Arrays.asList(nodes));
    }

    public void connectNode(RegexNode node) {
        childNodes.add(node);
    }

    public Set<RegexVariable> findUsedVariables() {
        Set<RegexVariable> result = new HashSet<RegexVariable>();
        for (RegexNode childNode : childNodes) {
            result.addAll(childNode.findUsedVariables());
        }
        return result;
    }

    public List<RegexNode> getChildNodes() {
        return Collections.unmodifiableList(childNodes);
    }

    public String toString(char separator) {
        StringBuilder result = new StringBuilder();
        result.append('(');
        Iterator<RegexNode> iterator = childNodes.iterator();
        if (iterator.hasNext()) {
            result.append(iterator.next());
            while (iterator.hasNext()) {
                result.append(separator);
                result.append(iterator.next());
            }
        }
        result.append(')');
        return result.toString();
    }
}

package com.github.avereshchagin.emblang.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Author: A. Vereshchagin
 * Date: 16.12.11
 */
public class Loop {

    private final List<Node<?>> nodes = new ArrayList<Node<?>>();

    public void addNode(Node<?> node) {
        nodes.add(node);
        node.addLoop(this);
    }

    public List<Node<?>> getNodes() {
        return Collections.unmodifiableList(nodes);
    }

    @Override
    public String toString() {
        return "Loop: " + nodes.toString();
    }
}

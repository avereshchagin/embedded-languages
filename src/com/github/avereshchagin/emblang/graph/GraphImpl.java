package com.github.avereshchagin.emblang.graph;

import java.util.*;

/**
 * @author A. Vereshchagin
 */
public class GraphImpl<E> implements Graph<E> {

    private final Map<GraphNode, E> nodeValues = new HashMap<GraphNode, E>();

    private final Map<GraphNode, Set<GraphNode>> outNodes = new LinkedHashMap<GraphNode, Set<GraphNode>>();

    private final Map<GraphNode, Set<GraphNode>> inNodes = new LinkedHashMap<GraphNode, Set<GraphNode>>();

    private static class GraphNodeImpl implements GraphNode {

    }

    public GraphNode addNode(E value) {
        GraphNode node = new GraphNodeImpl();
        nodeValues.put(node, value);
        inNodes.put(node, new LinkedHashSet<GraphNode>());
        outNodes.put(node, new LinkedHashSet<GraphNode>());
        return node;
    }

    public void addEdge(GraphNode source, GraphNode destination) {
        Set<GraphNode> sourceSet = outNodes.get(source);
        Set<GraphNode> destinationSet = inNodes.get(destination);
        if (sourceSet != null && destinationSet != null) {
            sourceSet.add(destination);
            destinationSet.add(source);
        }
    }

    public E getValue(GraphNode node) {
        return nodeValues.get(node);
    }

    public void putValue(GraphNode node, E value) {
        nodeValues.put(node, value);
    }

    public Set<GraphNode> getNodes() {
        return nodeValues.keySet();
    }

    public Set<GraphNode> getOutNodes(GraphNode node) {
        return Collections.unmodifiableSet(outNodes.get(node));
    }

    public Set<GraphNode> getInNodes(GraphNode node) {
        return Collections.unmodifiableSet(inNodes.get(node));
    }
}

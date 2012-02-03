package com.github.avereshchagin.emblang.graph;

import java.util.Set;

/**
 * @author A. Vereshchagin
 */
public class ReversedGraph<E> implements Graph<E> {

    private final Graph<E> graph;

    public ReversedGraph(Graph<E> graph) {
        this.graph = graph;
    }

    public GraphNode addNode(E value) {
        return graph.addNode(value);
    }

    public void addEdge(GraphNode source, GraphNode destination) {
        graph.addEdge(source, destination);
    }

    public E getValue(GraphNode node) {
        return graph.getValue(node);
    }

    public void putValue(GraphNode node, E value) {
        graph.putValue(node, value);
    }

    public Set<GraphNode> getNodes() {
        return graph.getNodes();
    }

    public Set<GraphNode> getOutNodes(GraphNode node) {
        return graph.getInNodes(node);
    }

    public Set<GraphNode> getInNodes(GraphNode node) {
        return graph.getOutNodes(node);
    }
}

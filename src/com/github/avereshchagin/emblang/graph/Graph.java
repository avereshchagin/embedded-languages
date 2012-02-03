package com.github.avereshchagin.emblang.graph;

import java.util.Set;

/**
 * Interface for graph.
 *
 * @author A. Vereshchagin
 */
public interface Graph<E> {

    /**
     * Adds new node to graph.
     *
     * @param value Value to be associated with new node.
     * @return Node which was added.
     */
    GraphNode addNode(E value);

    /**
     * Adds new directed edge to graph.
     * If edge is already exists this method does nothing.
     *
     * @param source      Source node of new edge.
     * @param destination Destination node of new edge.
     */
    void addEdge(GraphNode source, GraphNode destination);

    /**
     * Returns value associated with specified node or <code>null</code> if no value is associated.
     *
     * @param node Node whose associated value is to be returned.
     * @return value associated with specified node or <code>null</code> if no value is associated.
     */
    E getValue(GraphNode node);

    /**
     * Associates specified value with specified node.
     *
     * @param node  Node with which specified value is to be associated.
     * @param value Value to be associated with specified node.
     */
    void putValue(GraphNode node, E value);

    Set<GraphNode> getNodes();

    Set<GraphNode> getOutNodes(GraphNode node);

    Set<GraphNode> getInNodes(GraphNode node);
}

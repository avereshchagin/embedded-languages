package com.github.avereshchagin.emblang.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Author: A. Vereshchagin
 * Date: 15.12.11
 */
public class Graph<E> {

    private final List<Node<E>> nodes = new ArrayList<Node<E>>();
    private final List<Edge<E>> edges = new ArrayList<Edge<E>>();

    public void addNode(Node<E> node) {
        nodes.add(node);
    }

    public void addEdge(Node<E> source, Node<E> destination) {
        Edge<E> edge = new Edge<E>(source, destination);
        source.addOutgoingEdge(edge);
        destination.addIncomingEdge(edge);
        edges.add(edge);
    }

    public List<Node<E>> getNodes() {
        return Collections.unmodifiableList(nodes);
    }

    public List<Edge<E>> getEdges() {
        return Collections.unmodifiableList(edges);
    }

    @Override
    public String toString() {
        return "Nodes: " + nodes.toString() + "; Edges: " + edges.toString();
    }
}

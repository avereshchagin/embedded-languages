package com.github.avereshchagin.emblang.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Author: A. Vereshchagin
 * Date: 15.12.11
 */
public class ReversedDFS<E> {

    private final Graph<E> graph;
    private List<Node<E>> topOrdering = new ArrayList<Node<E>>();
    private int clock;

    public ReversedDFS(Graph<E> graph, List<Node<E>> nodes) {
        this.graph = graph;
        for (Node<E> node : graph.getNodes()) {
            node.setVisited(false);
        }
        for (Node<E> node : nodes) {
            if (!node.isVisited()) {
                explore(node);
            }
        }
        markEdges();
    }

    protected void explore(Node<E> node) {
        node.setVisited(true);
        node.setEnterValue(clock++);
        for (Edge<E> edge : node.getIncomingEdges()) {
            if (!edge.getSource().isVisited()) {
                explore(edge.getSource());
            }
        }
        node.setLeaveValue(clock++);
        topOrdering.add(node);
    }

    protected void markEdges() {
        for (Edge<E> edge : graph.getEdges()) {
            Node u = edge.getDestination();
            Node v = edge.getSource();
            if (u.isVisited() && v.isVisited()) {
                if (u.getEnterValue() < v.getEnterValue() && v.getLeaveValue() < u.getLeaveValue()) {
                    edge.setType(Edge.Type.FORWARD);
                } else if (v.getEnterValue() < u.getEnterValue() && u.getLeaveValue() < v.getLeaveValue()) {
                    edge.setType(Edge.Type.BACK);
                } else if (v.getLeaveValue() < u.getEnterValue()) {
                    edge.setType(Edge.Type.CROSS);
                }
            }
        }
    }

    public List<Node<E>> getTopOrdering() {
        return Collections.unmodifiableList(topOrdering);
    }

    public List<Loop> findLoops() {
        List<Loop> loops = new ArrayList<Loop>();
        for (Edge edge : graph.getEdges()) {
            if (Edge.Type.BACK.equals(edge.getType())) {

                Loop loop = new Loop();
                loops.add(loop);
                Node<?> currentNode = edge.getDestination();
                Node<?> lastNode = edge.getSource();
                loop.addNode(currentNode);
                do {
                    for (Edge<?> nextEdge : currentNode.getOutgoingEdges()) {
                        Node<?> nextNode = nextEdge.getDestination();
                        if (nextNode.isVisited() && nextNode.getEnterValue() == currentNode.getEnterValue() - 1) {
                            currentNode = nextEdge.getDestination();
                            break;
                        }
                    }
                    loop.addNode(currentNode);
                } while (currentNode != lastNode);
            }
        }
        return loops;
    }
}

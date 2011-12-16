package com.github.avereshchagin.emblang.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: A. Vereshchagin
 * Date: 16.12.11
 */
public class Warshall<E> {

    private final Graph<E> graph;
    private final boolean[][] transitiveClosure;
    private final Map<Node, Integer> nodeIntegerMap = new HashMap<Node, Integer>();
    private final int numberOfNodes;

    public Warshall(Graph<E> graph) {
        this.graph = graph;
        numberOfNodes = graph.getNodes().size();
        transitiveClosure = new boolean[numberOfNodes][numberOfNodes];
        List<Node<E>> nodes = graph.getNodes();
        for (int i = 0; i < numberOfNodes; i++) {
            nodeIntegerMap.put(nodes.get(i), i);
        }
        for (Edge edge : graph.getEdges()) {
            if (!Edge.Type.BACK.equals(edge.getType())) {
                int i = nodeIntegerMap.get(edge.getSource());
                int j = nodeIntegerMap.get(edge.getDestination());
                transitiveClosure[i][j] = true;
            }
        }
        for (int i = 0; i < numberOfNodes; i++) {
            for (int j = 0; j < numberOfNodes; j++) {
                if (transitiveClosure[i][j]) {
                    for (int k = 0; k < numberOfNodes; k++) {
                        if (transitiveClosure[j][k]) {
                            transitiveClosure[i][k] = true;
                        }
                    }
                }
            }
        }
    }

    public List<Fork> findForks() {
        List<Fork> forks = new ArrayList<Fork>();
        for (Node<E> source : graph.getNodes()) {
            if (source.getOutgoingEdges().size() > 1) {
                int i = nodeIntegerMap.get(source);
                for (Node<E> sink : graph.getNodes()) {
                    List<Edge<E>> incomingEdges = sink.getIncomingEdges();
                    if (incomingEdges.size() > 1) {
                        int k = 0;
                        for (Edge edge : incomingEdges) {
                            int j = nodeIntegerMap.get(edge.getSource());
                            if (transitiveClosure[i][j]) {
                                k++;
                            }
                        }
                        if (k > 1) {
                            Fork fork = new Fork(source, sink);
                            fork.bind();
                            forks.add(fork);
                        }
                    }
                }
            }
        }
        return forks;
    }
}

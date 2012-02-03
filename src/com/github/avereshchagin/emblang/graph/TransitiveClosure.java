package com.github.avereshchagin.emblang.graph;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author A. Vereshchagin
 */
public class TransitiveClosure {

    private final boolean[][] transitiveClosureMatrix;
    private final Map<GraphNode, Integer> nodeIntegerMap = new IdentityHashMap<GraphNode, Integer>();

    private TransitiveClosure(Set<GraphNode> nodes) {
        int numberOfNodes = nodes.size();
        transitiveClosureMatrix = new boolean[numberOfNodes][numberOfNodes];
        int i = 0;
        for (GraphNode node : nodes) {
            nodeIntegerMap.put(node, i++);
        }
    }

    private void enablePath(GraphNode source, GraphNode destination) {
        transitiveClosureMatrix[nodeIntegerMap.get(source)][nodeIntegerMap.get(destination)] = true;
    }

    public boolean hasPath(GraphNode source, GraphNode destination) {
        return transitiveClosureMatrix[nodeIntegerMap.get(source)][nodeIntegerMap.get(destination)];
    }

    public static <E> TransitiveClosure forGraph(Graph<E> graph) {
        TransitiveClosure transitiveClosure = new TransitiveClosure(graph.getNodes());

        for (GraphNode source : graph.getNodes()) {
            for (GraphNode destination : graph.getOutNodes(source)) {
                transitiveClosure.enablePath(source, destination);
            }
        }

        boolean[][] transitiveClosureMatrix = transitiveClosure.transitiveClosureMatrix;
        for (int i = 0; i < transitiveClosureMatrix.length; i++) {
            for (int j = 0; j < transitiveClosureMatrix.length; j++) {
                if (transitiveClosureMatrix[i][j]) {
                    for (int k = 0; k < transitiveClosureMatrix.length; k++) {
                        if (transitiveClosureMatrix[j][k]) {
                            transitiveClosureMatrix[i][k] = true;
                        }
                    }
                }
            }
        }

        return transitiveClosure;
    }
}

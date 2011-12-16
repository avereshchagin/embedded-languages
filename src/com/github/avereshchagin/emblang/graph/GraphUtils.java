package com.github.avereshchagin.emblang.graph;

import com.intellij.openapi.util.text.StringUtil;

import java.io.PrintStream;

/**
 * Author: A. Vereshchagin
 * Date: 15.12.11
 */
public class GraphUtils {

    public static <E> void printDotGraph(Graph<E> graph, PrintStream out) {
        out.println("digraph G {");
        out.println("node [style=filled];");
        for (Node<E> node : graph.getNodes()) {
            out.print(System.identityHashCode(node) + " [label=\"" + StringUtil.escapeQuotes(node.toString()) + "\",");
            out.print("color=gray");
            out.println("];");
        }
        for (Edge<E> edge : graph.getEdges()) {
            out.println(System.identityHashCode(edge.getSource()) + " -> " +
                    System.identityHashCode(edge.getDestination()) + ";");
        }
        out.println("}");
    }

    public static <E> void showGraph(Graph<E> graph) {
        try {
            Process process = Runtime.getRuntime().exec("/usr/local/bin/dot -Tpng -o/tmp/graph.png");
            PrintStream out = new PrintStream(process.getOutputStream());
            printDotGraph(graph, out);
            out.flush();
            out.close();
            process.waitFor();
            Runtime.getRuntime().exec("open /tmp/graph.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

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
        for (GraphNode node : graph.getNodes()) {
            out.print(System.identityHashCode(node) + " [label=\"" + StringUtil.escapeQuotes(
                    graph.getValue(node).toString()) + "\",");
            out.print("color=gray");
            out.println("];");
        }
        for (GraphNode source : graph.getNodes()) {
            for (GraphNode destination : graph.getOutNodes(source)) {
                out.println(System.identityHashCode(source) + " -> " +
                        System.identityHashCode(destination) + ";");
            }
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

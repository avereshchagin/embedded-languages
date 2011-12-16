package com.github.avereshchagin.emblang.graph;

import org.w3c.dom.Element;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: A. Vereshchagin
 * Date: 15.12.11
 */
public class GraphFromXmlBuilder implements GraphBuilder {

    private final Graph<String> graph = new Graph<String>();
    private final Map<String, Node<String>> nodeMap = new HashMap<String, Node<String>>();

    public void addNode(Element nodeElement) {
        String typeStr = nodeElement.getAttribute("type");
        String nameStr = nodeElement.getAttribute("name");
        if (nameStr != null) {
            Node<String> node = new Node<String>();
            node.setData(nameStr);
            graph.addNode(node);
            nodeMap.put(nameStr, node);
        }
    }

    public void addEdge(Element edgeElement) {
        String sourceStr = edgeElement.getAttribute("source");
        String destinationStr = edgeElement.getAttribute("destination");
        Node<String> source = nodeMap.get(sourceStr);
        Node<String> destination = nodeMap.get(destinationStr);
        if (source != null && destination != null) {
            graph.addEdge(source, destination);
        }
    }

    public Graph<String> getGraph() {
        return graph;
    }
}

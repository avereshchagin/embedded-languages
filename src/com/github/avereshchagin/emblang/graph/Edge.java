package com.github.avereshchagin.emblang.graph;

/**
 * Author: A. Vereshchagin
 * Date: 15.12.11
 */
public class Edge<E> {

    private final Node<E> source;
    private final Node<E> destination;

    public static enum Type {
        FORWARD,
        BACK,
        CROSS
    }

    private Type type;

    public Edge(Node<E> source, Node<E> destination) {
        this.source = source;
        this.destination = destination;
    }

    public Node<E> getSource() {
        return source;
    }

    public Node<E> getDestination() {
        return destination;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return source.toString() + " -> " + destination.toString();
    }
}

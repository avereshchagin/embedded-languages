package com.github.avereshchagin.emblang.graph;

/**
 * Author: A. Vereshchagin
 * Date: 16.12.11
 */
public class Fork<E> {

    private final Node<E> source;
    private final Node<E> sink;

    public Fork(Node<E> source, Node<E> sink) {
        this.source = source;
        this.sink = sink;
    }

    public void bind() {
        source.setOutgoingFork(this);
        sink.addIncomingFork(this);
    }

    public Node<E> getSource() {
        return source;
    }

    public Node<E> getSink() {
        return sink;
    }

    @Override
    public String toString() {
        return "Fork: " + source.toString() + " .. " + sink.toString();
    }
}

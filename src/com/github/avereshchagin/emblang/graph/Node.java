package com.github.avereshchagin.emblang.graph;

import java.util.*;

/**
 * Author: A. Vereshchagin
 * Date: 15.12.11
 */
public class Node<E> {

    private final List<Edge<E>> incomingEdges = new ArrayList<Edge<E>>();
    private final List<Edge<E>> outgoingEdges = new ArrayList<Edge<E>>();
    private E data;

    private boolean visited;
    private int enterValue;
    private int leaveValue;

    private final Set<Loop> loops = new HashSet<Loop>();
    private Fork outgoingFork;
    private final Set<Fork> incomingForks = new HashSet<Fork>();

    public void addIncomingEdge(Edge<E> edge) {
        incomingEdges.add(edge);
    }

    public void addOutgoingEdge(Edge<E> edge) {
        outgoingEdges.add(edge);
    }

    public List<Edge<E>> getIncomingEdges() {
        return Collections.unmodifiableList(incomingEdges);
    }

    public List<Edge<E>> getOutgoingEdges() {
        return Collections.unmodifiableList(outgoingEdges);
    }

    public E getData() {
        return data;
    }

    public void setData(E data) {
        this.data = data;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public int getEnterValue() {
        return enterValue;
    }

    public void setEnterValue(int enterValue) {
        this.enterValue = enterValue;
    }

    public int getLeaveValue() {
        return leaveValue;
    }

    public void setLeaveValue(int leaveValue) {
        this.leaveValue = leaveValue;
    }

    public void addLoop(Loop loop) {
        loops.add(loop);
    }

    public Fork getOutgoingFork() {
        return outgoingFork;
    }

    public void setOutgoingFork(Fork outgoingFork) {
        this.outgoingFork = outgoingFork;
    }

    public void addIncomingFork(Fork fork) {
        incomingForks.add(fork);
    }

    @Override
    public String toString() {
        return data != null ? data.toString() : "null";
    }
}

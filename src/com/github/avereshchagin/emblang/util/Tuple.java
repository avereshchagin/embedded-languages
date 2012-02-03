package com.github.avereshchagin.emblang.util;

/**
 * @author A. Vereshchagin
 */
public class Tuple<A, B> {

    private final A first;

    private final B second;

    public Tuple(A first, B second) {
        this.first = first;
        this.second = second;
    }

    public A getFirst() {
        return first;
    }

    public B getSecond() {
        return second;
    }
}

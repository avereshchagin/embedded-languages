package com.github.avereshchgin.alvor.cfg;

public class CfgEdge {

    public static enum Type {
        FORWARD,
        BACK,
        CROSS
    }

    private final CfgStatement source;

    private final CfgStatement destination;

    private Type type;

    public CfgEdge(CfgStatement source, CfgStatement destination) {
        this.source = source;
        this.destination = destination;
        source.addOutgoingEdge(this);
        destination.addIncomingEdge(this);
    }

    public CfgStatement getSource() {
        return source;
    }

    public CfgStatement getDestination() {
        return destination;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}

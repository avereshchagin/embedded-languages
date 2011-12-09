package com.github.avereshchagin.emblang.cfg;

import java.util.*;

public class CfgStatement {

    protected final List<CfgEdge> incomingEdges = new ArrayList<CfgEdge>();

    protected final List<CfgEdge> outgoingEdges = new ArrayList<CfgEdge>();

    protected final Set<Integer> loops = new HashSet<Integer>();

    protected int enterValue;

    protected int leaveValue;

    protected boolean visited;

    protected boolean atLeastOnce;

    private boolean verificationRequired;

    public List<CfgEdge> getIncomingEdges() {
        return Collections.unmodifiableList(incomingEdges);
    }

    public void addIncomingEdge(CfgEdge edge) {
        incomingEdges.add(edge);
    }

    public List<CfgEdge> getOutgoingEdges() {
        return Collections.unmodifiableList(outgoingEdges);
    }

    public void addOutgoingEdge(CfgEdge edge) {
        outgoingEdges.add(edge);
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

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public boolean isAtLeastOnce() {
        return atLeastOnce;
    }

    public void setAtLeastOnce(boolean atLeastOnce) {
        this.atLeastOnce = atLeastOnce;
    }

    public boolean isVerificationRequired() {
        return verificationRequired;
    }

    public void setVerificationRequired(boolean verificationRequired) {
        this.verificationRequired = verificationRequired;
    }

    public void addLoop(int id) {
        loops.add(id);
    }

    public boolean hasLoop() {
        return !loops.isEmpty();
    }

    public boolean containsLoop(int id) {
        return loops.contains(id);
    }

    public int getLoopId() {
        return loops.iterator().next();
    }

    @Override
    public String toString() {
        return "";
    }
}

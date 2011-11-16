package com.github.avereshchgin.alvor.controlflow;

public class Label {

    private Instruction target;

    public Instruction getTarget() {
        return target;
    }

    public void setTarget(Instruction target) {
        assert this.target == null;
        this.target = target;
    }

    @Override
    public String toString() {
        return "label_" + System.identityHashCode(this);
    }
}

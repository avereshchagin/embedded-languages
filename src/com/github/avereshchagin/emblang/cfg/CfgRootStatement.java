package com.github.avereshchagin.emblang.cfg;

public class CfgRootStatement extends CfgStatement {

    private final String methodName;

    public CfgRootStatement(String methodName) {
        this.methodName = methodName;
    }

    @Override
    public String toString() {
        return methodName;
    }
}

package com.github.avereshchagin.emblang.cfg;

import java.io.PrintStream;

/**
 * Author: A. Vereshchagin
 * Date: 08.12.11
 */
public interface CfgBuilder {

    void printDotGraph(PrintStream out);

    void showGraph();

    ControlFlowGraph getControlFlowGraph();
}

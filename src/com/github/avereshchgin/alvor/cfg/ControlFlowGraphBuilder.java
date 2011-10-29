package com.github.avereshchgin.alvor.cfg;

import com.github.avereshchgin.alvor.verification.VerifiableMethodsFinder;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiMethod;

import java.io.PrintStream;
import java.util.Collections;

public class ControlFlowGraphBuilder {

    private final ControlFlowGraph cfg = new ControlFlowGraph();

    private final VerifiableMethodsFinder finder;

    public ControlFlowGraphBuilder(VerifiableMethodsFinder finder) {
        this.finder = finder;
    }

    public void addMethod(PsiMethod psiMethod) {
        CfgNode rootNode = new CfgRoot(psiMethod);
        cfg.addNode(rootNode);
        MyJavaElementVisitor.processCodeBlock(this, Collections.singletonList(rootNode), psiMethod.getBody());
    }

    private void printDotGraph(PrintStream out) {
        out.println("digraph G {");
        out.println("node [style=filled];");
        for (CfgNode node : cfg.getNodes()) {
            out.print(System.identityHashCode(node) + " [label=\"" + StringUtil.escapeQuotes(node.toString()) + "\",");
            if (node instanceof CfgRoot) {
                out.print("color=lightblue");
            } else if (node.isVerificationRequired()) {
                out.print("color=red");
            } else if (!"".equals(node.toString())) {
                out.print("color=palegreen");
            } else {
                out.print("color=gray");
            }
            out.println("];");
        }
        for (CfgNode srcNode : cfg.getNodes()) {
            for (CfgNode destNode : srcNode.getNextNodes()) {
                out.println(System.identityHashCode(srcNode) + " -> " + System.identityHashCode(destNode) + ";");
            }
        }
        out.println("}");
    }

    public void showGraph() {
        try {
            Process process = Runtime.getRuntime().exec("/usr/local/bin/dot -Tpng -o/tmp/graph.png");
            PrintStream out = new PrintStream(process.getOutputStream());
            printDotGraph(out);
            out.flush();
            out.close();
            process.waitFor();
            Runtime.getRuntime().exec("open /tmp/graph.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ControlFlowGraph getControlFlowGraph() {
        return cfg;
    }

    public VerifiableMethodsFinder getFinder() {
        return finder;
    }
}

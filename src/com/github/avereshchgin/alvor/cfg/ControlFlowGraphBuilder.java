package com.github.avereshchgin.alvor.cfg;

import com.github.avereshchgin.alvor.verification.VerifiableMethodsFinder;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiIdentifier;
import com.intellij.psi.PsiMethod;

import java.io.PrintStream;
import java.util.Collections;
import java.util.List;

public class ControlFlowGraphBuilder {

    private final ControlFlowGraph cfg = new ControlFlowGraph();

    private final VerifiableMethodsFinder finder;

    public ControlFlowGraphBuilder(VerifiableMethodsFinder finder) {
        this.finder = finder;
    }

    public void addMethod(PsiMethod psiMethod) {
        CfgStatement rootNode = new CfgRootStatement(psiMethod.getName());
        cfg.addNode(rootNode);
        List<CfgStatement> prevNodes = CfgJavaElementVisitor.processCodeBlock(
                this, Collections.singletonList(rootNode), psiMethod.getBody(),
                Collections.<PsiIdentifier, CfgStatement>emptyMap()).getPreviousNodes();
        CfgStatement returnNode = null;
        for (CfgStatement prevNode : prevNodes) {
            if (!(prevNode instanceof CfgReturnStatement)) {
                if (returnNode == null) {
                    returnNode = new CfgReturnStatement();
                    cfg.addNode(returnNode);
                }
                cfg.addEdge(prevNode, returnNode);
            }
        }
    }

    private void printDotGraph(PrintStream out) {
        out.println("digraph G {");
        out.println("node [style=filled];");
        for (CfgStatement node : cfg.getNodes()) {
            out.print(System.identityHashCode(node) + " [label=\"" + StringUtil.escapeQuotes(node.toString()) + "\",");
            if (node instanceof CfgRootStatement || node instanceof CfgReturnStatement) {
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
        for (CfgEdge edge : cfg.getEdges()) {
            out.println(System.identityHashCode(edge.getSource()) + " -> " +
                    System.identityHashCode(edge.getDestination()) + ";");
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

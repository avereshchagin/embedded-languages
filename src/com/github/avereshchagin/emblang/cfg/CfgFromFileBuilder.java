package com.github.avereshchagin.emblang.cfg;

import com.intellij.openapi.util.text.StringUtil;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Author: A. Vereshchagin
 * Date: 08.12.11
 */
public class CfgFromFileBuilder implements CfgBuilder {

    private final ControlFlowGraph cfg = new ControlFlowGraph();

    private static interface NodeBuilder {
        CfgStatement buildNode();
    }

    private static final Map<String, NodeBuilder> NODE_BUILDERS = new HashMap<String, NodeBuilder>();

    static {
        NODE_BUILDERS.put("CfgStatement", new NodeBuilder() {
            public CfgStatement buildNode() {
                return new CfgStatement();
            }
        });

        NODE_BUILDERS.put("CfgRootStatement", new NodeBuilder() {
            public CfgStatement buildNode() {
                return new CfgRootStatement("");
            }
        });

        NODE_BUILDERS.put("CfgReturnStatement", new NodeBuilder() {
            public CfgStatement buildNode() {
                return new CfgReturnStatement();
            }
        });

        NODE_BUILDERS.put("CfgAssignmentStatement", new NodeBuilder() {
            public CfgStatement buildNode() {
                return new CfgAssignmentStatement(null, null);
            }
        });
    }

    private final Map<String, CfgStatement> namedNodes = new HashMap<String, CfgStatement>();

    public void buildControlFlowGraph(String fileName) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
            String line;
            while ((line = reader.readLine()) != null) {
                processLine(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ex) {
                }
            }
        }
    }

    private void processLine(String line) {
        StringTokenizer tokenizer = new StringTokenizer(line);
        if (tokenizer.countTokens() < 3) {
            return;
        }
        String type = tokenizer.nextToken();
        if ("node".equals(type)) {
            NodeBuilder nodeBuilder = NODE_BUILDERS.get(tokenizer.nextToken());
            if (nodeBuilder != null) {
                CfgStatement node = nodeBuilder.buildNode();
                cfg.addNode(node);
                namedNodes.put(tokenizer.nextToken(), node);
            }
        } else if ("edge".equals(type)) {
            CfgStatement source = namedNodes.get(tokenizer.nextToken());
            CfgStatement destination = namedNodes.get(tokenizer.nextToken());
            if (source != null && destination != null) {
                cfg.addEdge(source, destination);
            }
        }
    }

    public void printDotGraph(PrintStream out) {
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
                    System.identityHashCode(edge.getDestination()) +
                    (CfgEdge.Type.BACK.equals(edge.getType()) ? " [style=dashed]" : "") + ";");
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

    public static void main(String[] args) {
        CfgFromFileBuilder builder = new CfgFromFileBuilder();
        builder.buildControlFlowGraph("testData/cfg/CfgFromFileBuilderTest/simpleGraph");
        builder.showGraph();
    }
}

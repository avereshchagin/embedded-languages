package com.github.avereshchagin.emblang.graph;

import junit.framework.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.util.List;

/**
 * Author: A. Vereshchagin
 * Date: 16.12.11
 */
public class ReversedDFSTest {

    public static final File TEST_DFS_INPUT = new File("testData/graph/ReversedDFSTest/");

    @Test
    public void testDFS()
            throws Exception {
        File[] testFiles = TEST_DFS_INPUT.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith(".xml");
            }
        });
        if (testFiles != null) {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            for (File testFile : testFiles) {
                GraphFromXmlBuilder graphBuilder = new GraphFromXmlBuilder();
                Document document = builder.parse(testFile);
                NodeList nodeList = document.getElementsByTagName("node");
                for (int i = 0; i < nodeList.getLength(); i++) {
                    graphBuilder.addNode((Element) nodeList.item(i));
                }
                nodeList = document.getElementsByTagName("edge");
                for (int i = 0; i < nodeList.getLength(); i++) {
                    graphBuilder.addEdge((Element) nodeList.item(i));
                }

                Graph<String> graph = graphBuilder.getGraph();
                ReversedDFS<String> dfs = new ReversedDFS<String>(graph, graph.getNodes());
                List<Loop> loops = dfs.findLoops();

                BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(
                        testFile.getAbsolutePath().replaceAll("\\.xml", ".txt")
                )));
                Assert.assertEquals(loops.toString(), reader.readLine());
                reader.close();
            }
        }
    }
}

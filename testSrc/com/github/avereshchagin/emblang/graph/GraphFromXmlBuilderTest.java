package com.github.avereshchagin.emblang.graph;

import org.junit.Test;

import java.io.File;

/**
 * Author: A. Vereshchagin
 * Date: 15.12.11
 */
public class GraphFromXmlBuilderTest {

    public static final File TEST_GRAPH_BUILDING_INPUT = new File("testData/graph/GraphFromXmlBuilderTest/");

    @Test
    public void testGraphBuilding()
            throws Exception {
//        File[] testFiles = TEST_GRAPH_BUILDING_INPUT.listFiles(new FilenameFilter() {
//            public boolean accept(File dir, String name) {
//                return name.endsWith(".xml");
//            }
//        });
//        if (testFiles != null) {
//            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//            DocumentBuilder builder = factory.newDocumentBuilder();
//            for (File testFile : testFiles) {
//                GraphFromXmlBuilder graphBuilder = new GraphFromXmlBuilder();
//                Document document = builder.parse(testFile);
//                NodeList nodeList = document.getElementsByTagName("node");
//                for (int i = 0; i < nodeList.getLength(); i++) {
//                    graphBuilder.addNode((Element) nodeList.item(i));
//                }
//                nodeList = document.getElementsByTagName("edge");
//                for (int i = 0; i < nodeList.getLength(); i++) {
//                    graphBuilder.addEdge((Element) nodeList.item(i));
//                }
//
//                GraphImpl graph = graphBuilder.getGraph();
//                String graphStr = graph.toString();
//
//                BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(
//                        testFile.getAbsolutePath().replaceAll("\\.xml", ".txt")
//                )));
//                Assert.assertEquals(graph.toString(), reader.readLine());
//                reader.close();
//            }
//        }
    }
}

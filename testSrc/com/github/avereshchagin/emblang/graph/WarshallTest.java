//package com.github.avereshchagin.emblang.graph;
//
//import com.github.avereshchagin.emblang.old.*;
//import junit.framework.Assert;
//import org.junit.Test;
//import org.w3c.dom.Document;
//import org.w3c.dom.Element;
//import org.w3c.dom.NodeList;
//
//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;
//import java.io.*;
//import java.util.List;
//
///**
// * Author: A. Vereshchagin
// * Date: 16.12.11
// */
//public class WarshallTest {
//
//    public static final File TEST_WARSHALL_INPUT = new File("testData/graph/WarshallTest/");
//
//    @Test
//    public void testWarshall()
//            throws Exception {
//        File[] testFiles = TEST_WARSHALL_INPUT.listFiles(new FilenameFilter() {
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
//                GraphImpl<String> graph = graphBuilder.getGraph();
//                ReversedDFS<String> reversedDFS = new ReversedDFS<String>(graph, graph.getNodes());
//                Warshall<String> warshall = new Warshall<String>(graph);
//                List<Fork> forks = warshall.findForks();
//
//                BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(
//                        testFile.getAbsolutePath().replaceAll("\\.xml", ".txt")
//                )));
//                Assert.assertEquals(forks.toString(), reader.readLine());
//                reader.close();
//            }
//        }
//    }
//}

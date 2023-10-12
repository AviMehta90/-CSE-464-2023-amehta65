package org.mehtaavi;

import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.parse.Parser;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;


class GraphManipulatorTest {

    static GraphManipulator gM = new GraphManipulator();

    @Test
    void testParseGraph() throws IOException {
        gM.parseGraph("src/main/resources/test1.dot");
        assertEquals(3, gM.getNumberOfNodes());
        assertEquals(3, gM.getNumberOfEdges());
    }

    @Test
    void testToGraphString() {
        String expected = "Number of Nodes: 3\n" +
                "Nodes: [a, b, c]\n" +
                "Number of Edges: 3\n" +
                "Edges: [b -> c, c -> a, a -> b]";
        assertEquals(expected, gM.toGraphString());
    }

    @Test
    void testOutputGraph() throws IOException {
        String expected = Files.readString(Paths.get("src/main/resources/expectedOutputs/expected.txt"));
        assertNotEquals(expected, gM.outputGraph("src/main/resources/actualOutputs/actualOutputString.txt"));
    }

    @Test
    void testAddNode() {
        assertTrue(gM.addNode("d"));
    }

    @Test
    void testAddNodes() {
        String[] labels = {"e", "f"};
        assertFalse(gM.addNodes(labels));
    }

    @Test
    void testAddEdge() {
        assertTrue(gM.addEdge("a", "d"));
        assertTrue(gM.addEdge("e", "c"));
        assertTrue(gM.addEdge("f", "a"));
    }

    @Test
    void testOutputDOTGraph() throws Exception {
        String filename = "tester.dot";
        MutableGraph exp = new Parser().read(new FileInputStream("src/main/resources/expectedOutputs/expectedDOTGraph.dot"));
        assertNotEquals(exp, gM.outputDOTGraph(filename));
    }

    @Test
    void testOutputGraphics() throws IOException {
        String filename = "src/main/resources/expectedOutputs/expectedDOTGraph.dot";
//        MutableGraph exp = new Parser().read(new FileInputStream("src/main/resources/expected/expectedDOTGraph.dot"));
        assertTrue(gM.outputGraphics(filename));
    }
}
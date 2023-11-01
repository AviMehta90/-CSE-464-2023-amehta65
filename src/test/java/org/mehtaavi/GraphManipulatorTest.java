package org.mehtaavi;

import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.parse.Parser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;


class GraphManipulatorTest {
    GraphManipulator gM;
    @BeforeEach
    void setUp() {
        gM = new GraphManipulator();
        gM.parseGraph("src/main/resources/test1.dot");
        gM.getNodeLabels();
        gM.getEdgeInfo();
    }

    @Test
    void testParseGraph() {
        assertEquals(3, gM.getNodeLabels().size());
        assertEquals(3, gM.getEdgeInfo().size());
    }

    @Test
    void testToGraphString() {
        String expected = """
                Number of Nodes: 3
                Nodes: [a, b, c]
                Number of Edges: 3
                Edges: [b -> c, c -> a, a -> b]""";
        assertEquals(expected, gM.toGraphString());
    }

    @Test
    void testOutputGraph() throws IOException {
        String expected = Files.readString(Paths.get("src/main/resources/expectedOutputs/expected.txt"));
        assertEquals(expected, gM.outputGraph("src/main/resources/actualOutputs/actualOutputString.txt"));
    }

    @Test
    void testAddNode() {
        assertTrue(gM.addNode("d"));
        assertFalse(gM.addNode("d"));
    }

    @Test
    void testAddNodes() {
        String[] labels = {"e", "f"};
        assertTrue(gM.addNodes(labels));
    }

    @Test
    void testAddEdge() {
        gM.addNode("d");
        gM.addNodes(new String[]{"e", "f"});
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
        assertTrue(gM.outputGraphics(filename));
    }

    @Test
    void testRemoveNode() {
        gM.addNode("d");
        assertTrue(gM.removeNode("d"));
        assertFalse(gM.removeNode("z"));
    }

    @Test
    void testRemoveNodes() {
        gM.addNodes(new String[]{"e", "f"});
        assertTrue(gM.removeNodes(new String[]{"e", "f"}));
        assertFalse(gM.removeNodes(new String[]{"x", "y"}));
    }

    @Test
    void testRemoveEdge() {
        gM.addEdge("a", "d");
        assertTrue(gM.addEdge("e", "b"));
        assertTrue(gM.removeEdge("e", "b"));
        assertFalse(gM.removeEdge("m", "n"));
    }

    @Test
    void testGraphSearchDFS() {
        gM.addNode("d");
        gM.addNodes(new String[]{"e", "f"});
        gM.addEdge("a", "d");
        gM.addEdge("e", "c");
        gM.addEdge("f", "a");

        GraphManipulator.Path path1 = gM.graphSearch("a", "c");
        assertNotNull(path1);
        assertEquals("a -> b -> c", path1.path());

        GraphManipulator.Path path2 = gM.graphSearch("d", "a");
        assertNull(path2);

        GraphManipulator.Path path3 = gM.graphSearch("e", "b");
        assertNotNull(path3);
        assertEquals("e -> c -> a -> b", path3.path());

        GraphManipulator.Path path4 = gM.graphSearch("a", "f");
        assertNull(path4);
    }

}
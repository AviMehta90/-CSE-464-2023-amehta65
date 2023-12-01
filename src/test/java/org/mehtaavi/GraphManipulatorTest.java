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
                Edges: [c->a, a->b, b->c]""";
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
        System.out.println(gM.toGraphString());
        System.out.println("Adding node d");
        gM.addNode("d");
        System.out.println(gM.toGraphString());
        assertTrue(gM.removeNode("d"));
        System.out.println(gM.toGraphString());
        assertFalse(gM.removeNode("z"));
    }

    @Test
    void testRemoveNodes() {
        System.out.println("Adding nodes e, f");
        gM.addNodes(new String[]{"e", "f"});
        System.out.println(gM.toGraphString());
        System.out.println("Removing nodes: e, f");
        assertTrue(gM.removeNodes(new String[]{"e", "f"}));
        System.out.println(gM.toGraphString());
        System.out.println("Removing non-existent nodes: x, y");
        assertFalse(gM.removeNodes(new String[]{"x", "y"}));
    }

    @Test
    void testRemoveEdge() {
        gM.addEdge("a", "d");
        assertTrue(gM.addEdge("e", "b"));
        System.out.println(gM.toGraphString());
        assertTrue(gM.removeEdge("e", "b"));
        System.out.println(gM.toGraphString());
        System.out.println("Removing non-existent edge: m -> n");
        assertFalse(gM.removeEdge("m", "n"));
    }

    @Test
    void testGraphSearch() throws IOException {
        gM.addNode("d");
        gM.addNodes(new String[]{"e", "f"});
        gM.addEdge("a", "d");
        gM.addEdge("e", "c");
        gM.addEdge("f", "a");

        MutableGraph graph = new Parser().read(new FileInputStream("src/main/resources/expectedOutputs/expectedDOTGraph.dot"));

        gM.setSearchStrategy(new DFSAlgorithm(graph));
        System.out.println("Performing DFS");
        GraphManipulator.Path dfsPath = gM.graphSearch("a","c");
        assertEquals("a -> b -> c", dfsPath.path());

        gM.setSearchStrategy(new BFSAlgorithm(graph));
        System.out.println("Performing BFS");
        GraphManipulator.Path bfsPath = gM.graphSearch("e", "b");
        assertEquals("e -> c -> a -> b", bfsPath.path());

        MutableGraph test_graph = new Parser().read(new FileInputStream("src/main/resources/input2.dot"));
        gM.setSearchStrategy(new RandomWalkAlgorithm(test_graph));
        System.out.println("Performing Random Walk Search");
        GraphManipulator.Path rwsPath = gM.graphSearch("a", "c");
        System.out.println(rwsPath.toString());

    }

    @Test
    void testRandomWalk() throws IOException{
        MutableGraph test_graph = new Parser().read(new FileInputStream("src/main/resources/input2.dot"));
        gM.setSearchStrategy(new RandomWalkAlgorithm(test_graph));
        System.out.println("Performing Random Walk Search");
        String allSearches = gM.randomWalkSearchProcess("a", "c", 3);
        System.out.println(allSearches);
    }

}
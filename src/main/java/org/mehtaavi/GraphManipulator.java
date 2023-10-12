package org.mehtaavi;

import guru.nidi.graphviz.engine.*;
import guru.nidi.graphviz.model.*;
import guru.nidi.graphviz.parse.Parser;

import java.io.*;
import java.util.*;

import static guru.nidi.graphviz.model.Factory.mutGraph;
import static guru.nidi.graphviz.model.Factory.mutNode;

public class GraphManipulator {

    private static MutableGraph g;
    public GraphManipulator() {
        g = mutGraph("example").setDirected(true);
    }

    // Feature 1: Parse a DOT graph file to create a graph

    public void parseGraph(String filePath) throws IOException {
        try {
            InputStream dot = new FileInputStream(filePath);
            g = new Parser().read(dot);
            System.out.println("Dot File Parsed at " + filePath);
        }
        catch (Exception e){
            System.out.println("Dot File not imported properly");
        }
    }

    private final HashSet<String> nodeSet = new HashSet<>();
    public int getNumberOfNodes() {
        for (MutableNode node: g.nodes()){
            String name = node.toString();
            String nodeName = name.substring(0, name.indexOf("{"));
            nodeSet.add(nodeName);
        }
        return nodeSet.size();
    }

    public Set<String> getNodeLabels() {
        Set<String> labels = new HashSet<>();
        if (g != null){
            for (MutableNode node : g.nodes()) {
                if (nodeSet.contains(node.name().toString())){
                    labels.add(node.name().toString());
                    nodeSet.add(node.name().toString());
                }
            }
        }
        return labels;
    }

    public int getNumberOfEdges() {
        return g.edges().size();
    }

    public Set<String> getEdgeInfo() {
        Set<String> edgeInfo = new HashSet<>();
        if (g != null){
            for (var edge : g.edges()) {
                assert edge.from() != null;
                String ef = edge.from().toString();
                edgeInfo.add(ef.substring(0, ef.indexOf("{")) + " -> " + ef.substring(ef.indexOf(">")+1, ef.indexOf(":")));
            }
        }
        return edgeInfo;
    }

    public String toGraphString() {
        return "Number of Nodes: " + getNumberOfNodes() +
                "\nNodes: " + getNodeLabels() +
                "\nNumber of Edges: " + getNumberOfEdges() +
                "\nEdges: " + getEdgeInfo();
    }

    public String outputGraph(String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(toGraphString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Output String Graph Info at "+ filePath);
        return toGraphString();
    }

    // Feature 2: Adding nodes from the imported graph

    public boolean addNode(String label) {
        if(!getNodeLabels().contains(label)){
            g.nodes().add(mutNode(label));
        }
        else {
            System.out.println("Node already in graph");
            return false;
        }
        return true;
    }

    public boolean addNodes(String[] labels) {
        System.out.println("Adding nodes: "+ Arrays.toString(labels));
        for (String label : labels) {
            return addNode(label);
        }
        return false;
    }

    // Feature 3: Adding edges from the imported graph

    public boolean addEdge(String srcLabel, String dstLabel){
        g.add(mutNode(srcLabel).addLink(dstLabel));
        System.out.println("Edge created: "+ srcLabel+ " -> "+ dstLabel);
        return true;
    }

    // Feature 4: Output the imported graph into a DOT file or graphics

    public MutableGraph outputDOTGraph(String filename) throws Exception {
        try {
            String pref = "src/main/resources/actualOutputs";
            String filePath = pref + filename;
            Graphviz.fromGraph(g).render(Format.DOT).toFile(new File(filePath));
            InputStream dot = new FileInputStream(filePath);
            g = new Parser().read(dot);
        }
        catch (Exception e){
            throw new Exception("DOT File not formed");
        }
        return g;
    }

    public boolean outputGraphics(String filePath) throws IOException {
        String pref = "src/main/resources/";
        InputStream dot = new FileInputStream(filePath);
        g = new Parser().read(dot);
        Graphviz.fromGraph(g).width(700).render(Format.PNG).toFile(new File(pref+"new_graphPNG.png"));
        return g != null;
    }

//    public static void main(String[] args) throws IOException {
//        String dotFilePath = "src/main/resources/test1.dot";
//
//        try {
//            // Feature 1: Parse a DOT Graph File
//            System.out.println("Feature 1: Parsing DOT Graph File");
//            parseGraph(dotFilePath);
//            System.out.println("\nDot File looks like: ");
//            System.out.println(g.toString());
//            System.out.println(toGraphString());
//            outputGraph("src/main/resources/expected.txt");
//
//            // Feature 2: Adding Nodes
//            System.out.println("\nFeature 2: Adding Node(s): d, e, f");
//            boolean nodeAdded = addNodes(new String[]{"d", "e", "f"});
//            System.out.println("New node(s) added: " + nodeAdded);
//
//            // Feature 3: Adding Edges
//            System.out.println("\nFeature 3: Adding Edges");
//            boolean edgeAdded1 = addEdge("a", "d");
//            boolean edgeAdded2 = addEdge("e", "c");
//            boolean edgeAdded3 = addEdge("f", "a");
//            System.out.println("New edge(s) added\n");
//            System.out.println(g.toString());
//
//            // Feature 4: Output the Imported Graph as DOT File and Graphics
//            String outputDotFile = "graph_for_graphics.dot";
//            System.out.println("\nFeature 4: Output the Imported Graph as DOT File");
//            MutableGraph updatedGraph = outputDOTGraph(outputDotFile);
//            System.out.println("DOT file created at: " + outputDotFile);
//
//            String dotFilePathForGraphics = "src/main/resources/graph_for_graphics.dot";
//            System.out.println("\nFeature 4: Output the Imported Graph as Graphics");
//            String graphicsGenerated = outputGraphics(dotFilePathForGraphics);
//            System.out.println("Graph graphics generated at : " + graphicsGenerated);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
}

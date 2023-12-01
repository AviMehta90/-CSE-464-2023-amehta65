package org.mehtaavi;

import guru.nidi.graphviz.engine.*;
import guru.nidi.graphviz.model.*;
import guru.nidi.graphviz.parse.Parser;

import java.io.*;
import java.util.*;

import static guru.nidi.graphviz.model.Factory.mutGraph;
import static guru.nidi.graphviz.model.Factory.mutNode;

// Class to manipulate and interact with graphs
public class GraphManipulator {

    // Constants for edge representation
    private static final String EDGE_DELIMITER = "->";
    private static final String PATH_PREFIX = "src/main/resources/";

    // Graph instance and search strategy
    private MutableGraph g;
    private GraphSearchStrategy searchStrategy;

    // Sets to store unique node labels and edge information
    Set<String> nodeSet = new HashSet<>();
    Set<String> edgeSet = new HashSet<>();

    // Constructor to initialize the graph
    public GraphManipulator() {
        g = mutGraph("example").setDirected(true);
    }

    // Set the search strategy for graph searches
    public void setSearchStrategy(GraphSearchStrategy strategy) {
        this.searchStrategy = strategy;
    }

    // Parse a graph from a DOT file
    public void parseGraph(String filePath) {
        try {
            InputStream dot = new FileInputStream(filePath);
            g = new Parser().read(dot);
            System.out.println("Dot File Parsed at " + filePath);
        } catch (Exception e) {
            System.out.println("Dot File not imported properly");
        }
    }

    // Get unique node labels from the graph
    public Set<String> getNodeLabels() {
        if (g != null) {
            for (MutableNode node : g.nodes()) {
                nodeSet.add(node.toString().substring(0, node.toString().indexOf("{")));
            }
        }
        return nodeSet;
    }

    // Get information about edges in the graph
    public Set<String> getEdgeInfo() {
        if (g != null) {
            for (Link edge : g.edges()) {
                assert edge.from() != null;
                String ef = edge.from().toString();
                String tempEdge = ef.substring(0, ef.indexOf("{")) + EDGE_DELIMITER +
                        ef.substring(ef.indexOf(">") + 1, ef.indexOf(":"));
                edgeSet.add(tempEdge);
            }
        }
        return edgeSet;
    }

    // Convert graph information to a string representation
    public String toGraphString() {
        return "Number of Nodes: " + nodeSet.size() +
                "\nNodes: " + nodeSet +
                "\nNumber of Edges: " + edgeSet.size() +
                "\nEdges: " + edgeSet;
    }

    // Output graph information to a file
    public String outputGraph(String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(toGraphString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Output String Graph Info at " + filePath);
        return toGraphString();
    }

    // Add a node to the graph
    public boolean addNode(String label) {
        if (nodeSet.add(label)) {
            g.add(mutNode(label));
            return true;
        }
        System.out.println("Node already in graph");
        return false;
    }

    // Remove a node from the graph
    public boolean removeNode(String label) {
        if (nodeSet.remove(label)) {
            g.nodes().remove(mutNode(label));
            System.out.println("Node " + label + " removed.");
            return true;
        }
        System.out.println("Node " + label + " not found.");
        return false;
    }

    // Add multiple nodes to the graph
    public boolean addNodes(String[] labels) {
        return modifyNodes(labels, true);
    }

    // Remove multiple nodes from the graph
    public boolean removeNodes(String[] labels) {
        return modifyNodes(labels, false);
    }

    // Helper method to add or remove multiple nodes
    private boolean modifyNodes(String[] labels, boolean isAdd) {
        boolean allModified = true;
        for (String label : labels) {
            if (isAdd) {
                if (!addNode(label)) {
                    allModified = false;
                }
            } else {
                if (!removeNode(label)) {
                    allModified = false;
                }
            }
        }
        return allModified;
    }

    // Add an edge between two nodes in the graph
    public boolean addEdge(String srcLabel, String dstLabel) {
        String edgeKey = String.format("%s%s%s", srcLabel, EDGE_DELIMITER, dstLabel);
        if (edgeSet.add(edgeKey)) {
            g.add(mutNode(srcLabel).addLink(dstLabel));
            System.out.println("Edge created: " + srcLabel + " -> " + dstLabel);
            return true;
        }
        System.out.println("Edge already in graph");
        return false;
    }

    // Find a node in the graph by its label
    private MutableNode findNode(String label) {
        for (MutableNode node : g.nodes()) {
            if (node.name().toString().equals(label)) {
                return node;
            }
        }
        return null;
    }

    // Remove an edge between two nodes in the graph
    public boolean removeEdge(String srcLabel, String dstLabel) {
        String edgeKey = srcLabel + EDGE_DELIMITER + dstLabel;
        if (!edgeSet.remove(edgeKey)) {
            System.out.println("Edge " + srcLabel + " -> " + dstLabel + " not found.");
            return false;
        }
        MutableNode srcNode = findNode(srcLabel);
        if (srcNode == null) {
            System.out.println("Source node not found.");
            return false;
        }
        Link targetLink = null;
        for (Link link : srcNode.links()) {
            String actualDst = link.to().toString().replace(":", "");
            if (actualDst.equals(dstLabel)) {
                targetLink = link;
                break;
            }
        }
        if (targetLink != null) {
            srcNode.links().remove(targetLink);
            System.out.println("Edge " + srcLabel + " -> " + dstLabel + " removed.");
            return true;
        } else {
            System.out.println("Link not found in source node links.");
            return false;
        }
    }

    // Output the graph in DOT format to a file
    public MutableGraph outputDOTGraph(String filename) throws Exception {
        try {
            String filePath = PATH_PREFIX + "actualOutputs" + filename;
            Graphviz.fromGraph(g).render(Format.DOT).toFile(new File(filePath));
            InputStream dot = new FileInputStream(filePath);
            g = new Parser().read(dot);
        } catch (Exception e) {
            throw new Exception("Error creating DOT file: " + e.getMessage());
        }
        return g;
    }

    // Output the graph as a PNG image
    public boolean outputGraphics(String filePath) throws IOException {
        InputStream dot = new FileInputStream(filePath);
        g = new Parser().read(dot);
        Graphviz.fromGraph(g).width(700).render(Format.PNG).toFile(new File(PATH_PREFIX + "new_graph_image.png"));
        return g != null;
    }

    // Perform a graph search using the specified strategy
    public Path graphSearch(String srcLabel, String dstLabel) {
        return searchStrategy.graphSearch(srcLabel, dstLabel);
    }

    // Record class to represent a path in the graph
    public record Path(String path) {
    }

    // Conduct random walk search process for a specified number of iterations
    public String randomWalkSearchProcess(String srcLabel, String dstLabel, int numIterations) {
        StringBuilder result = new StringBuilder("\n");

        for (int i = 0; i < numIterations; i++) {
            System.out.println("Iteration: " + (i + 1));
            Path path = searchStrategy.graphSearch(srcLabel, dstLabel);
            result.append("visiting iteration ").append(i + 1).append(" ").append(path).append("\n");
        }

        return result.toString();
    }
}

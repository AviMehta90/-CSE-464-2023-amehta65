package org.mehtaavi;

import guru.nidi.graphviz.engine.*;
import guru.nidi.graphviz.model.*;
import guru.nidi.graphviz.parse.Parser;

import java.io.*;
import java.util.*;

import static guru.nidi.graphviz.model.Factory.mutGraph;
import static guru.nidi.graphviz.model.Factory.mutNode;

public class GraphManipulator {

    private MutableGraph g;
    Set<String> nodeSet = new HashSet<>();
    Set<String> edgeSet = new HashSet<>();
    public GraphManipulator() {
        g = mutGraph("example").setDirected(true);
    }

    // Feature 1: Parse a DOT graph file to create a graph
    public void parseGraph(String filePath) {
        try {
            InputStream dot = new FileInputStream(filePath);
            g = new Parser().read(dot);
            System.out.println("Dot File Parsed at " + filePath);
        }
        catch (Exception e){
            System.out.println("Dot File not imported properly");
        }
    }

    public Set<String> getNodeLabels() {
        if (g != null){
            for (MutableNode node: g.nodes()){
                String name = node.toString();
                String nodeName = name.substring(0, name.indexOf("{"));
                nodeSet.add(nodeName);
            }
        }
        return nodeSet;
    }

    public Set<String> getEdgeInfo() {
        if (g != null){
            for (Link edge : g.edges()) {
                assert edge.from() != null;
                String ef = edge.from().toString();
                String tempEdge = ef.substring(0, ef.indexOf("{")) + " -> " + ef.substring(ef.indexOf(">")+1, ef.indexOf(":"));
                edgeSet.add(tempEdge);
            }
        }
        return edgeSet;
    }

    public String toGraphString() {
        return "Number of Nodes: " + nodeSet.size() +
                "\nNodes: " + nodeSet +
                "\nNumber of Edges: " + edgeSet.size() +
                "\nEdges: " + edgeSet;
    }

    public String outputGraph(String filePath) {
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
        if (nodeSet.add(label)) {
            g.add(mutNode(label));
            return true;
        }
        System.out.println("Node already in graph");
        return false;
    }

    public boolean addNodes(String[] labels) {
        boolean allAdded = true;
        for (String label : labels) {
            if (!addNode(label)) {
                allAdded = false;
            }
        }
        return allAdded;
    }

    public boolean removeNode(String label) {
        if (nodeSet.remove(label)) {
            g.nodes().remove(mutNode(label));
            System.out.println("Node " + label + " removed.");
            return true;
        }
        System.out.println("Node " + label + " not found.");
        return false;
    }

    public boolean removeNodes(String[] labels) {
        boolean allRemoved = true;
        for (String label : labels) {
            if (!removeNode(label)) {
                allRemoved = false;
            }
        }
        return allRemoved;
    }


    public boolean addEdge(String srcLabel, String dstLabel) {
        String edgeKey = srcLabel + "->" + dstLabel;
        if (edgeSet.add(edgeKey)) {
            g.add(mutNode(srcLabel).addLink(dstLabel));
            System.out.println("Edge created: " + srcLabel + " -> " + dstLabel);
            return true;
        }
        System.out.println("Edge already in graph");
        return false;
    }

    private MutableNode findNode(String label) {
        for (MutableNode node : g.nodes()) {
            if (node.name().toString().equals(label)) {
                return node;
            }
        }
        return null;
    }

    public boolean removeEdge(String srcLabel, String dstLabel) {
        String edgeKey = srcLabel + "->" + dstLabel;
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

}

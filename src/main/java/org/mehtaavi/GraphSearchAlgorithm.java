package org.mehtaavi;

import guru.nidi.graphviz.model.*;

import java.util.*;

/**
 * An abstract class representing a generic graph search algorithm.
 */
public abstract class GraphSearchAlgorithm {

    protected final MutableGraph graph;
    protected final Set<String> visited;
    protected final Map<String, String> parentMap;

    /**
     * Constructor for the GraphSearchAlgorithm class.
     *
     * @param graph The graph on which the search algorithm operates.
     */
    public GraphSearchAlgorithm(MutableGraph graph) {
        this.graph = graph;
        this.visited = new HashSet<>();
        this.parentMap = new HashMap<>();
    }

    /**
     * Performs a graph search from a source node to a destination node.
     *
     * @param srcLabel The label of the source node.
     * @param dstLabel The label of the destination node.
     * @return A Path representing the sequence of nodes from the source to the destination, or null if no path is found.
     */
    public GraphManipulator.Path graphSearch(String srcLabel, String dstLabel) {
        LinkedList<String> qs = new LinkedList<>();
        initialize(srcLabel);
        qs.add(srcLabel);

        while (!qs.isEmpty()) {
            String currentLabel = getNextNode(qs);
            if (currentLabel == null) {
                continue;
            }

            GraphManipulator.Path pathBuilder = getPath(dstLabel, parentMap, currentLabel);
            if (pathBuilder != null) {
                return pathBuilder;
            }
            processNeighbors(currentLabel, qs);
        }
        return null;
    }

    /**
     * Abstract method to be implemented by concrete subclasses to determine the next node in the search.
     *
     * @param qs The collection of nodes to be explored.
     * @return The label of the next node to be explored.
     */
    protected abstract String getNextNode(LinkedList<String> qs);

    /**
     * Initializes the data structures for the graph search algorithm.
     *
     * @param srcLabel The label of the source node.
     */
    protected void initialize(String srcLabel) {
        visited.clear();
        parentMap.clear();
        visited.add(srcLabel);
        parentMap.put(srcLabel, null);
    }

    /**
     * Constructs a path from the destination node to the source node using the parentMap.
     *
     * @param dstLabel      The label of the destination node.
     * @param parentMap     The map storing parent relationships between nodes.
     * @param currentLabel  The label of the current node.
     * @return A Path representing the sequence of nodes from the source to the destination, or null if no path is found.
     */
    private GraphManipulator.Path getPath(String dstLabel, Map<String, String> parentMap, String currentLabel) {
        if (currentLabel.equals(dstLabel)) {
            StringBuilder pathBuilder = new StringBuilder();
            String currentNode = dstLabel;
            while (currentNode != null) {
                pathBuilder.insert(0, currentNode);
                currentNode = parentMap.get(currentNode);
                if (currentNode != null) {
                    pathBuilder.insert(0, " -> ");
                }
            }
            return new GraphManipulator.Path(pathBuilder.toString());
        }
        return null;
    }

    /**
     * Explores the neighbors of the current node and adds them to the queue if they have not been visited.
     *
     * @param currentLabel The label of the current node.
     * @param qs           The queue of nodes to be explored.
     */
    protected void processNeighbors(String currentLabel, LinkedList<String> qs) {
        for (Link edge : graph.edges()) {
            assert edge.from() != null;
            String fromNode = edge.from().toString().substring(0, edge.from().toString().indexOf("{"));
            String toNode = edge.to().toString().replace(":", "");

            if (fromNode.equals(currentLabel) && !visited.contains(toNode)) {
                qs.add(toNode);
                visited.add(toNode);
                parentMap.put(toNode, currentLabel);
            }
        }
    }
}

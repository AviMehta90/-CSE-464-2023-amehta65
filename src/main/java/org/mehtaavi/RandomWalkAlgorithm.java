package org.mehtaavi;

import guru.nidi.graphviz.model.Link;
import guru.nidi.graphviz.model.MutableGraph;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

// This class represents an implementation of a random walk algorithm for graph traversal
public class RandomWalkAlgorithm extends GraphSearchAlgorithm implements GraphSearchStrategy {

    // Constructor that takes a MutableGraph as a parameter
    public RandomWalkAlgorithm(MutableGraph graph) {
        super(graph);
    }

    // Implementation of the graph search algorithm using random walk
    @Override
    public GraphManipulator.Path graphSearch(String srcLabel, String dstLabel) {
        // StringBuilder to build the path during the traversal
        StringBuilder pathBuilder = new StringBuilder();
        // Start the traversal from the source node
        String currentNode = srcLabel;

        // Continue the traversal until the destination node is reached
        while (!currentNode.equals(dstLabel)) {
            // Get the neighbors of the current node
            List<String> neighbors = getNeighbors(currentNode);
            // Choose a random neighbor as the next node in the path
            String nextNode = chooseRandomNeighbor(neighbors);
            // Append the current node to the path
            pathBuilder.append(currentNode);
            // Print the current path
            System.out.println(new GraphManipulator.Path(pathBuilder.toString()));

            // If no valid path is found, return a message indicating that
            if (nextNode == null) {
                return new GraphManipulator.Path("No path found");
            }

            // If the next node is different from the current node, append an arrow to the path
            if (!currentNode.equals(nextNode)) {
                pathBuilder.append(" -> ");
            }

            // Move to the next node in the traversal
            currentNode = nextNode;
        }

        // Append the last node to complete the path and return it
        pathBuilder.append(currentNode);
        return new GraphManipulator.Path(pathBuilder.toString());
    }

    // Not used in this implementation, returns null
    @Override
    protected String getNextNode(LinkedList<String> qs) {
        return null;
    }

    // Choose a random neighbor from the list of neighbors
    private String chooseRandomNeighbor(List<String> neighbors) {
        Random random = new Random();
        if (neighbors.size() == 0) {
            return null;
        }
        return neighbors.get(random.nextInt(neighbors.size()));
    }

    // Get the neighbors of a given node in the graph
    private List<String> getNeighbors(String nodeLabel) {
        List<String> neighborLabels = new ArrayList<>();
        // Iterate through the edges of the graph to find neighbors
        for (Link edge : graph.edges()) {
            assert edge.from() != null;
            // Extract the from and to nodes from the edges
            String fromNode = edge.from().toString().substring(0, edge.from().toString().indexOf("{"));
            String toNode = edge.to().toString().replace(":", "");

            // If the fromNode matches the current node, add the toNode as a neighbor
            if (fromNode.equals(nodeLabel)) {
                neighborLabels.add(toNode);
            }
        }
        return neighborLabels;
    }
}

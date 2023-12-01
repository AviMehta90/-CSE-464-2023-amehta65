package org.mehtaavi;

import guru.nidi.graphviz.model.Link;
import guru.nidi.graphviz.model.MutableGraph;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class RandomWalkAlgorithm extends GraphSearchAlgorithm implements GraphSearchStrategy {

    public RandomWalkAlgorithm(MutableGraph graph) {
        super(graph);
    }

    @Override
    public GraphManipulator.Path graphSearch(String srcLabel, String dstLabel) {

        StringBuilder pathBuilder = new StringBuilder();
        String currentNode = srcLabel;

        while (!currentNode.equals(dstLabel)) {
            List<String> neighbors = getNeighbors(currentNode);
            String nextNode = chooseRandomNeighbor(neighbors);
            pathBuilder.append(currentNode);
            System.out.println(new GraphManipulator.Path(pathBuilder.toString()));
            if (nextNode == null) {
                return new GraphManipulator.Path("No path found");
            }
            if (!currentNode.equals(nextNode)) {
                pathBuilder.append(" -> ");
            }
            currentNode = nextNode;
        }
        pathBuilder.append(currentNode);
        return new GraphManipulator.Path(pathBuilder.toString());
    }

    @Override
    protected String getNextNode(LinkedList<String> qs) {
        return null;
    }

    private String chooseRandomNeighbor(List<String> neighbors) {
        Random random = new Random();
        if (neighbors.size() == 0){
            return null;
        }
        return neighbors.get(random.nextInt(neighbors.size()));
    }

    private List<String> getNeighbors(String nodeLabel) {
        List<String> neighborLabels = new ArrayList<>();
        for (Link edge : graph.edges()) {
            assert edge.from() != null;
            String fromNode = edge.from().toString().substring(0, edge.from().toString().indexOf("{"));
            String toNode = edge.to().toString().replace(":", "");

            if (fromNode.equals(nodeLabel)) {
                neighborLabels.add(toNode);
            }
        }
        return neighborLabels;
    }
}
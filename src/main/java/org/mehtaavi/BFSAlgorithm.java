package org.mehtaavi;

import guru.nidi.graphviz.model.*;

import java.util.*;

public class BFSAlgorithm extends GraphSearchAlgorithm {

    public BFSAlgorithm(MutableGraph graph) {
        super(graph);
    }

    @Override
    protected String getNextNode(LinkedList<String> queue) {
        return queue.remove();
    }

    @Override
    protected void processNeighbors(String currentLabel, LinkedList<String> queue) {
        for (Link edge : graph.edges()) {
            assert edge.from() != null;
            String fromNode = edge.from().toString().substring(0, edge.from().toString().indexOf("{"));
            String toNode = edge.to().toString().replace(":", "");

            if (fromNode.equals(currentLabel) && !visited.contains(toNode)) {
                queue.add(toNode);
                visited.add(toNode);
                parentMap.put(toNode, currentLabel);
            }
        }
    }
}

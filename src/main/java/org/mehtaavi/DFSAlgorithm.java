package org.mehtaavi;

import guru.nidi.graphviz.model.*;

import java.util.*;

public class DFSAlgorithm extends GraphSearchAlgorithm {

    public DFSAlgorithm(MutableGraph graph) {
        super(graph);
    }

    @Override
    protected String getNextNode(LinkedList<String> stack) {
        return stack.removeLast();
    }

    @Override
    protected void processNeighbors(String currentLabel, LinkedList<String> stack) {
        for (Link edge : graph.edges()) {
            assert edge.from() != null;
            String fromNode = edge.from().toString().substring(0, edge.from().toString().indexOf("{"));
            String toNode = edge.to().toString().replace(":", "");

            if (fromNode.equals(currentLabel) && !visited.contains(toNode)) {
                stack.add(toNode);
                visited.add(toNode);
                parentMap.put(toNode, currentLabel);
            }
        }
    }
}

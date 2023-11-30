package org.mehtaavi;

import guru.nidi.graphviz.model.*;

import java.util.*;

public abstract class GraphSearchAlgorithm {

    protected final MutableGraph graph;
    protected final Set<String> visited;
    protected final Map<String, String> parentMap;

    public GraphSearchAlgorithm(MutableGraph graph) {
        this.graph = graph;
        this.visited = new HashSet<>();
        this.parentMap = new HashMap<>();
    }

    public GraphManipulator.Path graphSearch(String srcLabel, String dstLabel) {
        LinkedList<String> queue = new LinkedList<>();
        initialize(srcLabel);
        queue.add(srcLabel);

        while (!queue.isEmpty()) {
            String currentLabel = getNextNode(queue);
            if (currentLabel == null) {
                continue;
            }

            GraphManipulator.Path pathBuilder = getPath(dstLabel, parentMap, currentLabel);
            if (pathBuilder != null) {
                return pathBuilder;
            }

            processNeighbors(currentLabel, queue);
        }

        return null;
    }

    protected abstract String getNextNode(LinkedList<String> qs);

    protected abstract void processNeighbors(String currentLabel, LinkedList<String> qs);

    protected void initialize(String srcLabel) {
        visited.clear();
        parentMap.clear();
        visited.add(srcLabel);
        parentMap.put(srcLabel, null);
    }

    private GraphManipulator.Path getPath(String dstLabel, Map<String, String> parentMap, String currentLabel) {
        // The same getPath method as in GraphManipulator class
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
}

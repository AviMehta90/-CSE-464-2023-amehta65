package org.mehtaavi;

import guru.nidi.graphviz.model.*;

import java.util.*;

public class DFSAlgorithm extends GraphSearchAlgorithm implements GraphSearchStrategy {
    public DFSAlgorithm(MutableGraph graph) {
        super(graph);
    }

    /**
     * Retrieves the next node from the stack during DFS traversal.
     *
     * @param stack The stack containing the nodes to be explored.
     * @return The next node to be processed in the DFS traversal.
     */
    @Override
    protected String getNextNode(LinkedList<String> stack) {
        return stack.removeLast();
    }
}

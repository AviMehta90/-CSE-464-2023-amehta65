package org.mehtaavi;

import guru.nidi.graphviz.model.*;

import java.util.*;

public class BFSAlgorithm extends GraphSearchAlgorithm implements GraphSearchStrategy {
    public BFSAlgorithm(MutableGraph graph) {
        super(graph);
    }

    /**
     * Retrieves the next node from the queue during BFS traversal.
     *
     * @param queue The queue containing the nodes to be processed.
     * @return The next node to be processed.
     */
    @Override
    protected String getNextNode(LinkedList<String> queue) {
        return queue.remove();
    }
}

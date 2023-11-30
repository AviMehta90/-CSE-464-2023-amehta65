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
}

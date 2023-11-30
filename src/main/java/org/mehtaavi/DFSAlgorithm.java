package org.mehtaavi;

import guru.nidi.graphviz.model.*;

import java.util.*;

public class DFSAlgorithm extends GraphSearchAlgorithm implements GraphSearchStrategy{
    public DFSAlgorithm(MutableGraph graph) {
        super(graph);
    }
    @Override
    protected String getNextNode(LinkedList<String> stack) {
        return stack.removeLast();
    }
}

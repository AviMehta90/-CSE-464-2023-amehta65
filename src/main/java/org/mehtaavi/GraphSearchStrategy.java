package org.mehtaavi;

// This interface defines a contract for graph search strategies.
public interface GraphSearchStrategy {

    // Performs a graph search from the source label to the destination label.
    // Returns the path between the source and destination labels using a GraphManipulator.
    GraphManipulator.Path graphSearch(String srcLabel, String dstLabel);
}

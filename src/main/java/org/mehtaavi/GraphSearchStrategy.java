package org.mehtaavi;

public interface GraphSearchStrategy {
    GraphManipulator.Path graphSearch(String srcLabel, String dstLabel);
}

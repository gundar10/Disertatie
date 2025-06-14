package main.models;

import java.util.*;

public class TemporalGraph {

    private final Map<String, List<TemporalEdge>> adjacencyList = new HashMap<>();

    public void addEdge(String from, String to, int startTime, int duration) {
        TemporalEdge edge = new TemporalEdge(from, to, startTime, duration);
        adjacencyList
            .computeIfAbsent(from, k -> new ArrayList<>())
            .add(edge);
    }

    public List<TemporalEdge> getEdgesFrom(String node) {
        return adjacencyList.getOrDefault(node, Collections.emptyList());
    }
    
    public Map<String, List<TemporalEdge>> getAllEdges() {
        return adjacencyList;
    }
}
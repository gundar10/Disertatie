package main.models;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GraphSnapshot {
    private final Map<Integer, Set<Integer>> adjacencyList = new HashMap<>();
    private final boolean directed;

    public GraphSnapshot(boolean directed) {
        this.directed = directed;
    }

    public void addEdge(int u, int v) {
        adjacencyList.putIfAbsent(u, new HashSet<>());
        adjacencyList.get(u).add(v);
        if (!directed) {
            adjacencyList.putIfAbsent(v, new HashSet<>());
            adjacencyList.get(v).add(u);
        }
    }

    public Set<Integer> getNeighbors(int node) {
        return adjacencyList.getOrDefault(node, new HashSet<>());
    }

    public GraphSnapshot deepCopy() {
        GraphSnapshot copy = new GraphSnapshot(this.directed);
        for (var entry : adjacencyList.entrySet()) {
            copy.adjacencyList.put(entry.getKey(), new HashSet<>(entry.getValue()));
        }
        return copy;
    }

    public boolean isDirected() {
        return directed;
    }

    public Map<Integer, Set<Integer>> getAdjacencyList() {
        return adjacencyList;
    }
}

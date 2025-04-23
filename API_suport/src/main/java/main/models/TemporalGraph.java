package main.models;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class TemporalGraph {
    private final TreeMap<Long, GraphSnapshot> snapshots = new TreeMap<>();
    private final boolean directed;

    public TemporalGraph(boolean directed) {
        this.directed = directed;
    }

    public void createSnapshot(long timestamp, GraphSnapshot graph) {
        snapshots.put(timestamp, graph.deepCopy());
    }

    public GraphSnapshot getSnapshotAt(long timestamp) {
        Map.Entry<Long, GraphSnapshot> entry = snapshots.floorEntry(timestamp);
        return (entry != null) ? entry.getValue() : null;
    }

    public Set<Integer> getNeighborsAtTime(int node, long timestamp) {
        GraphSnapshot snapshot = getSnapshotAt(timestamp);
        return (snapshot != null) ? snapshot.getNeighbors(node) : new HashSet<>();
    }

    public boolean isDirected() {
        return directed;
    }
}
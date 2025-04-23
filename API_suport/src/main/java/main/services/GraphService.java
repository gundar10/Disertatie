package main.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import main.models.GraphSnapshot;
import main.models.TemporalGraph;
import org.springframework.stereotype.Service;

@Service
public class GraphService {
    private final Map<String, TemporalGraph> graphMap = new HashMap<>();

    public void createGraph(String id, boolean directed) {
        graphMap.put(id, new TemporalGraph(directed));
    }

    public void addSnapshot(String id, long timestamp, List<int[]> edges) {
        TemporalGraph graph = graphMap.get(id);
        if (graph == null) return;

        GraphSnapshot snapshot = new GraphSnapshot(graph.isDirected());
        for (int[] edge : edges) {
            snapshot.addEdge(edge[0], edge[1]);
        }
        graph.createSnapshot(timestamp, snapshot);
    }

    public GraphSnapshot getSnapshot(String id, long timestamp) {
        TemporalGraph graph = graphMap.get(id);
        return (graph != null) ? graph.getSnapshotAt(timestamp) : null;
    }

    public Set<Integer> getNeighbors(String id, int node, long timestamp) {
        TemporalGraph graph = graphMap.get(id);
        return (graph != null) ? graph.getNeighborsAtTime(node, timestamp) : Set.of();
    }
}
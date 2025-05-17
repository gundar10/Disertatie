package main.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import main.models.GraphSnapshot;
import main.models.TemporalGraph;
import org.springframework.stereotype.Service;

@Service
public class GraphService {
    private final Map<String, TemporalGraph> graphMap = new HashMap<>();

    public void createSetupGraph(String id, boolean directed) {
        graphMap.put(id, new TemporalGraph(directed));
        addSnapshot(id, 1000, Arrays.asList(new int[]{1, 2}));
        addSnapshot(id, 2000, Arrays.asList(new int[]{1, 3}, new int[]{1, 5}));
        addSnapshot(id, 3000, Arrays.asList(new int[]{5, 4}));
        addSnapshot(id, 4000, Arrays.asList(new int[]{2, 3}));
        addSnapshot(id, 5000, Arrays.asList(new int[]{2, 4}));
        addSnapshot(id, 6000, Arrays.asList(new int[]{4, 2}));
        addSnapshot(id, 7000, Arrays.asList(new int[]{1, 4}));
    }

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

    public List<Long> getEdgeHistory(String id, int from, int to) {
    TemporalGraph graph = graphMap.get(id);
    if (graph == null) return List.of();

        List<Long> activeTimestamps = new ArrayList<>();
        for (Map.Entry<Long, GraphSnapshot> entry : graph.getSnapshots().entrySet()) {
            GraphSnapshot snapshot = entry.getValue();
            if (snapshot.hasEdge(from, to)) {
                activeTimestamps.add(entry.getKey());
            }
        }
        return activeTimestamps;
    }

    public List<Integer> getShortestPath(String id, int from, int to, long timestamp) {
        GraphSnapshot snapshot = getSnapshot(id, timestamp);
        if (snapshot == null) return List.of();

        Queue<List<Integer>> queue = new LinkedList<>();
        Set<Integer> visited = new HashSet<>();
        queue.offer(List.of(from));

        while (!queue.isEmpty()) {
            List<Integer> path = queue.poll();
            int last = path.get(path.size() - 1);
            if (last == to) return path;

            if (visited.add(last)) {
                for (int neighbor : snapshot.getAdjacencyList().getOrDefault(last, Set.of())) {
                    List<Integer> newPath = new ArrayList<>(path);
                    newPath.add(neighbor);
                    queue.offer(newPath);
                }
            }
        }
        return List.of();
    }

    public void deleteGraph(String id) {
        graphMap.remove(id);
    }

    public List<Long> getAllTimestamps(String id) {
        TemporalGraph graph = graphMap.get(id);
        if (graph == null) return List.of();
        return new ArrayList<>(graph.getSnapshots().keySet());
    }       
}
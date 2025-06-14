package main.models;

import java.util.*;

public class TemporalGraph {

    private final Map<String, TemporalNode> nodes = new HashMap<>();
    private final Map<String, List<TemporalEdge>> outgoingEdges = new HashMap<>();
    private final Map<String, List<TemporalEdge>> incomingEdges = new HashMap<>();

    public void addNode(String id, Map<String, Object> attributes) {
        nodes.putIfAbsent(id, new TemporalNode(id, attributes));
    }

    public void addNodeIfAbsent(String id) {
        nodes.putIfAbsent(id, new TemporalNode(id));
    }

    public void addEdge(String from, String to, int startTime, int duration) {
        addEdge(from, to, startTime, duration, null);
    }

    public void addEdge(String from, String to, int startTime, int duration, Map<String, Object> attributes) {
        addNodeIfAbsent(from);
        addNodeIfAbsent(to);
        TemporalEdge edge = new TemporalEdge(from, to, startTime, duration, attributes);
        outgoingEdges.computeIfAbsent(from, k -> new ArrayList<>()).add(edge);
        incomingEdges.computeIfAbsent(to, k -> new ArrayList<>()).add(edge);
    }

    public List<TemporalEdge> getEdgesFrom(String node) {
        return outgoingEdges.getOrDefault(node, Collections.emptyList());
    }

    public List<TemporalEdge> getEdgesTo(String node) {
        return incomingEdges.getOrDefault(node, Collections.emptyList());
    }

    public Set<String> getAllNodeIds() {
        return nodes.keySet();
    }

    public Collection<TemporalNode> getAllNodes() {
        return nodes.values();
    }

    public Map<String, List<TemporalEdge>> getAllOutgoingEdges() {
        return outgoingEdges;
    }

    public Map<String, List<TemporalEdge>> getAllIncomingEdges() {
        return incomingEdges;
    }

    public Map<String, List<TemporalEdge>> getAllEdges() {
        return outgoingEdges;
    }

    public void clear() {
        nodes.clear();
        outgoingEdges.clear();
        incomingEdges.clear();
    }
}

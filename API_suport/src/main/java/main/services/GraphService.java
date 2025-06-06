package main.services;

import main.models.TemporalGraph;
import main.models.TemporalEdge;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GraphService {

    private final TemporalGraph temporalGraph = new TemporalGraph();

    public void addEdge(String from, String to, int startTime, int duration) {
        temporalGraph.addEdge(from, to, startTime, duration);
    }

    public List<String> findTemporalShortestPath(String source, String target, int tAlpha, int tOmega) {
        PriorityQueue<PathState> pq = new PriorityQueue<>();
        pq.add(new PathState(source, 0, 0));

        Map<String, Integer> bestCost = new HashMap<>();
        bestCost.put(source, 0);

        Map<String, String> parent = new HashMap<>();

        while (!pq.isEmpty()) {
            PathState state = pq.poll();

            if (state.node.equals(target)) {
                return reconstructPath(parent, target);
            }

            for (TemporalEdge edge : temporalGraph.getEdgesFrom(state.node)) {
                if (edge.getStartTime() >= state.arrivalTime && edge.getStartTime() >= tAlpha && (edge.getStartTime() + edge.getDuration()) <= tOmega) {
                    int arrival = edge.getStartTime() + edge.getDuration();
                    int newCost = state.totalCost + edge.getDuration();

                    if (newCost < bestCost.getOrDefault(edge.getTo(), Integer.MAX_VALUE)) {
                        bestCost.put(edge.getTo(), newCost);
                        parent.put(edge.getTo(), state.node);
                        pq.add(new PathState(edge.getTo(), arrival, newCost));
                    }
                }
            }
        }

        return Collections.emptyList();
    }
    
        public List<String> findFastestPath(String source, String target, int tAlpha, int tOmega) {
        List<TemporalEdge> outgoing = temporalGraph.getEdgesFrom(source);
        outgoing.sort(Comparator.comparingInt(TemporalEdge::getStartTime));

        List<String> bestPath = Collections.emptyList();
        int bestDuration = Integer.MAX_VALUE;

        for (TemporalEdge startEdge : outgoing) {
            int start = startEdge.getStartTime();
            if (start < tAlpha || (start + startEdge.getDuration()) > tOmega) continue;

            PriorityQueue<PathState> pq = new PriorityQueue<>();
            pq.add(new PathState(startEdge.getTo(), start + startEdge.getDuration(), startEdge.getDuration()));

            Map<String, Integer> arrivalTime = new HashMap<>();
            arrivalTime.put(source, start);
            arrivalTime.put(startEdge.getTo(), start + startEdge.getDuration());

            Map<String, String> parent = new HashMap<>();
            parent.put(startEdge.getTo(), source);

            while (!pq.isEmpty()) {
                PathState state = pq.poll();

                if (state.node.equals(target)) {
                    int duration = state.arrivalTime - start;
                    if (duration < bestDuration) {
                        bestDuration = duration;
                        bestPath = reconstructPath(parent, target);
                    }
                    break; // since we only care about one earliest arrival from this start time
                }

                for (TemporalEdge edge : temporalGraph.getEdgesFrom(state.node)) {
                    if (edge.getStartTime() >= state.arrivalTime && (edge.getStartTime() + edge.getDuration()) <= tOmega) {
                        int arrival = edge.getStartTime() + edge.getDuration();
                        if (arrival < arrivalTime.getOrDefault(edge.getTo(), Integer.MAX_VALUE)) {
                            arrivalTime.put(edge.getTo(), arrival);
                            parent.put(edge.getTo(), state.node);
                            pq.add(new PathState(edge.getTo(), arrival, 0));
                        }
                    }
                }
            }
        }

        return bestPath;
    }
        
    public List<String> findEarliestArrivalPath(String source, String target, int tAlpha, int tOmega) {
        PriorityQueue<PathState> pq = new PriorityQueue<>(Comparator.comparingInt(ps -> ps.arrivalTime));
        pq.add(new PathState(source, tAlpha, 0));

        Map<String, Integer> earliestArrival = new HashMap<>();
        earliestArrival.put(source, tAlpha);

        Map<String, String> parent = new HashMap<>();

        while (!pq.isEmpty()) {
            PathState state = pq.poll();
            if (state.node.equals(target)) {
                return reconstructPath(parent, target);
            }

            for (TemporalEdge edge : temporalGraph.getEdgesFrom(state.node)) {
                int edgeStart = edge.getStartTime();
                int edgeEnd = edgeStart + edge.getDuration();
                if (edgeStart >= state.arrivalTime && edgeStart >= tAlpha && edgeEnd <= tOmega) {
                    if (edgeEnd < earliestArrival.getOrDefault(edge.getTo(), Integer.MAX_VALUE)) {
                        earliestArrival.put(edge.getTo(), edgeEnd);
                        parent.put(edge.getTo(), state.node);
                        pq.add(new PathState(edge.getTo(), edgeEnd, 0));
                    }
                }
            }
        }

        return Collections.emptyList();
    }
    
    public List<String> findLatestDeparturePath(String source, String target, int tAlpha, int tOmega) {
        PriorityQueue<PathState> pq = new PriorityQueue<>((a, b) -> Integer.compare(b.arrivalTime, a.arrivalTime));
        pq.add(new PathState(target, tOmega, 0));

        Map<String, Integer> latestDeparture = new HashMap<>();
        latestDeparture.put(target, tOmega);

        Map<String, String> parent = new HashMap<>();

        while (!pq.isEmpty()) {
            PathState state = pq.poll();
            if (state.node.equals(source)) {
                return reconstructPathReverse(parent, source, target);
            }

            for (Map.Entry<String, List<TemporalEdge>> entry : temporalGraph.getAllEdges().entrySet()) {
                for (TemporalEdge edge : entry.getValue()) {
                    String from = edge.getFrom();
                    String to = edge.getTo();
                    int edgeStart = edge.getStartTime();
                    int edgeEnd = edgeStart + edge.getDuration();

                    if (to.equals(state.node) && edgeEnd <= state.arrivalTime && edgeStart >= tAlpha && edgeEnd <= tOmega) {
                        if (edgeStart > latestDeparture.getOrDefault(from, Integer.MIN_VALUE)) {
                            latestDeparture.put(from, edgeStart);
                            parent.put(from, to);
                            pq.add(new PathState(from, edgeStart, 0));
                        }
                    }
                }
            }
        }

        return Collections.emptyList();
    }
    
        public boolean isReachable(String source, String target, int tAlpha, int tOmega) {
        Queue<PathState> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        queue.add(new PathState(source, tAlpha, 0));

        while (!queue.isEmpty()) {
            PathState state = queue.poll();
            if (state.node.equals(target)) {
                return true;
            }
            for (TemporalEdge edge : temporalGraph.getEdgesFrom(state.node)) {
                int edgeStart = edge.getStartTime();
                int edgeEnd = edgeStart + edge.getDuration();
                if (edgeStart >= state.arrivalTime && edgeEnd <= tOmega && !visited.contains(edge.getTo())) {
                    visited.add(edge.getTo());
                    queue.add(new PathState(edge.getTo(), edgeEnd, 0));
                }
            }
        }

        return false;
    }


    private List<String> reconstructPath(Map<String, String> parent, String target) {
        LinkedList<String> path = new LinkedList<>();
        String current = target;
        while (current != null) {
            path.addFirst(current);
            current = parent.get(current);
        }
        return path;
    }
    
    private List<String> reconstructPathReverse(Map<String, String> parent, String source, String target) {
        LinkedList<String> path = new LinkedList<>();
        String current = source;
        while (current != null) {
            path.addLast(current);
            current = parent.get(current);
        }
        return path;
    }

    private static class PathState implements Comparable<PathState> {
        String node;
        int arrivalTime;
        int totalCost;

        public PathState(String node, int arrivalTime, int totalCost) {
            this.node = node;
            this.arrivalTime = arrivalTime;
            this.totalCost = totalCost;
        }

        @Override
        public int compareTo(PathState other) {
            return Integer.compare(this.totalCost, other.totalCost);
        }
    }
}

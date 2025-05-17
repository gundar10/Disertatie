package main.controllers;

import java.util.List;
import java.util.Map;
import java.util.Set;
import main.models.GraphSnapshot;
import main.services.GraphService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/graph")
public class GraphController {

    private final GraphService graphService;

    public GraphController(GraphService graphService) {
        this.graphService = graphService;
    }

    @PostMapping("/setup")
    public String createSetupGraph() {
        graphService.createSetupGraph("mygraph", true);
        return "Graph created.";
    }

    @PostMapping("/{id}")
    public String createGraph(@PathVariable String id, @RequestParam boolean directed) {
        graphService.createGraph(id, directed);
        return "Graph '" + id + "' created.";
    }

    @PostMapping("/{id}/snapshot")
    public String addSnapshot(@PathVariable String id,
                              @RequestParam long timestamp,
                              @RequestBody List<int[]> edges) {
        graphService.addSnapshot(id, timestamp, edges);
        return "Snapshot added at timestamp " + timestamp;
    }

    @GetMapping("/{id}/snapshot/{timestamp}")
    public Map<Integer, Set<Integer>> getSnapshot(@PathVariable String id,
                                                  @PathVariable long timestamp) {
        GraphSnapshot snapshot = graphService.getSnapshot(id, timestamp);
        return (snapshot != null) ? snapshot.getAdjacencyList() : Map.of();
    }

    @GetMapping("/{id}/neighbors")
    public Set<Integer> getNeighbors(@PathVariable String id,
                                     @RequestParam int node,
                                     @RequestParam long timestamp) {
        return graphService.getNeighbors(id, node, timestamp);
    }

    @GetMapping("/{id}/edge-history")
    public List<Long> getEdgeHistory(@PathVariable String id,
                                    @RequestParam int from,
                                    @RequestParam int to) {
        return graphService.getEdgeHistory(id, from, to);
    }

    @GetMapping("/{id}/shortest-path")
    public List<Integer> getShortestPath(@PathVariable String id,
                                         @RequestParam int from,
                                         @RequestParam int to,
                                         @RequestParam long timestamp) {
        return graphService.getShortestPath(id, from, to, timestamp);
    }
    
    @DeleteMapping("/{id}")
    public String deleteGraph(@PathVariable String id) {
        graphService.deleteGraph(id);
        return "Graph '" + id + "' deleted.";
    }

    @GetMapping("/{id}/timestamps")
    public List<Long> getAllTimestamps(@PathVariable String id) {
        return graphService.getAllTimestamps(id);
    }
}

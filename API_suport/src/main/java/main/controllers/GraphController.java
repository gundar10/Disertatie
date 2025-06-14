package main.controllers;

import java.util.Collection;
import main.services.GraphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import main.models.TemporalEdge;
import main.models.TemporalNode;

@RestController
@RequestMapping("/graph")
public class GraphController {

    @Autowired
    private GraphService graphService;

    @PostMapping("/addNode")
    public void addNode(@RequestBody Map<String, Object> payload) {
        String id = (String) payload.get("id");
        Map<String, Object> attributes = (Map<String, Object>) payload.get("attributes");
        graphService.addNode(id, attributes);
    }

    @PostMapping("/addEdge")
    public void addEdge(@RequestBody Map<String, Object> payload) {
        String from = (String) payload.get("from");
        String to = (String) payload.get("to");
        int startTime = (int) payload.get("startTime");
        int duration = (int) payload.get("duration");
        Map<String, Object> attributes = (Map<String, Object>) payload.get("attributes");
        graphService.addEdge(from, to, startTime, duration, attributes);
    }

    @PostMapping("/addEdges")
    public void addEdges(@RequestBody List<Map<String, Object>> edgeList) {
        for (Map<String, Object> edge : edgeList) {
            String from = (String) edge.get("from");
            String to = (String) edge.get("to");
            int startTime = (int) edge.get("startTime");
            int duration = (int) edge.get("duration");
            Map<String, Object> attributes = (Map<String, Object>) edge.get("attributes");
            graphService.addEdge(from, to, startTime, duration, attributes);
        }
    }
    
    @GetMapping("/nodes")
    public Collection<TemporalNode> getAllNodes() {
        return graphService.getAllNodes();
    }

    @GetMapping("/edges")
    public List<TemporalEdge> getAllEdges() {
        return graphService.getAllEdges();
    }

    @DeleteMapping("/clear")
    public void clearGraph() {
        graphService.clearGraph();
    }

    @GetMapping("/shortestTemporalPath")
    public List<String> getShortestTemporalPath(
            @RequestParam String source,
            @RequestParam String target,
            @RequestParam int tAlpha,
            @RequestParam int tOmega) {
        return graphService.findTemporalShortestPath(source, target, tAlpha, tOmega);
    }

    @GetMapping("/fastestPath")
    public List<String> getFastestPath(
            @RequestParam String source,
            @RequestParam String target,
            @RequestParam int tAlpha,
            @RequestParam int tOmega) {
        return graphService.findFastestPath(source, target, tAlpha, tOmega);
    }
    
    @GetMapping("/earliestArrivalPath")
    public List<String> getEarliestArrivalPath(
            @RequestParam String source,
            @RequestParam String target,
            @RequestParam int tAlpha,
            @RequestParam int tOmega) {
        return graphService.findEarliestArrivalPath(source, target, tAlpha, tOmega);
    }
    
    @GetMapping("/latestDeparturePath")
    public List<String> getLatestDeparturePath(
            @RequestParam String source,
            @RequestParam String target,
            @RequestParam int tAlpha,
            @RequestParam int tOmega) {
        return graphService.findLatestDeparturePath(source, target, tAlpha, tOmega);
    }
    
    @GetMapping("/reachable")
    public boolean isReachable(
            @RequestParam String source,
            @RequestParam String target,
            @RequestParam int tAlpha,
            @RequestParam int tOmega) {
        return graphService.isReachable(source, target, tAlpha, tOmega);
    }
}

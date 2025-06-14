package main.controllers;

import main.services.GraphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/graph")
public class GraphController {

    @Autowired
    private GraphService graphService;

    @PostMapping("/addEdge")
    public void addEdge(@RequestBody Map<String, Object> payload) {
        String from = (String) payload.get("from");
        String to = (String) payload.get("to");
        int startTime = (int) payload.get("startTime");
        int duration = (int) payload.get("duration");
        graphService.addEdge(from, to, startTime, duration);
    }

    @PostMapping("/addEdges")
    public void addEdges(@RequestBody List<Map<String, Object>> edgeList) {
        for (Map<String, Object> edge : edgeList) {
            String from = (String) edge.get("from");
            String to = (String) edge.get("to");
            int startTime = (int) edge.get("startTime");
            int duration = (int) edge.get("duration");
            graphService.addEdge(from, to, startTime, duration);
        }
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

package tests;



import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import main.models.TemporalEdge;
import main.services.GraphService;

import static org.junit.jupiter.api.Assertions.*;

public class GraphServiceTest {

    private GraphService graphService;

    @BeforeEach
    public void setUp() {
        graphService = new GraphService();
        graphService.clearGraph();
    }

    @Test
    public void testAddNodeAndEdge() {
        Map<String, Object> nodeAttrs = new HashMap<>();
        nodeAttrs.put("label", "Start");
        graphService.addNode("A", nodeAttrs);

        Map<String, Object> edgeAttrs = new HashMap<>();
        edgeAttrs.put("type", "direct");
        graphService.addEdge("A", "B", 1, 2, edgeAttrs);

        List<TemporalEdge> edges = graphService.getAllEdges();
        assertEquals(1, edges.size());
        assertEquals("A", edges.get(0).getFrom());
        assertEquals("B", edges.get(0).getTo());
        assertEquals(1, edges.get(0).getStartTime());
        assertEquals(2, edges.get(0).getDuration());
        assertEquals("direct", edges.get(0).getAttribute("type"));
    }

    @Test
    public void testReachabilityPositive() {
        graphService.addEdge("A", "B", 2, 1, null);
        graphService.addEdge("B", "C", 4, 1, null);
        graphService.addEdge("A", "C", 9, 1, null);

        boolean reachable = graphService.isReachable("A", "C", 0, 10);
        assertTrue(reachable);
    }

    @Test
    public void testReachabilityNegative() {
        graphService.addEdge("A", "B", 2, 1, null);
        graphService.addEdge("B", "C", 15, 1, null);

        boolean reachable = graphService.isReachable("A", "C", 0, 10);
        assertFalse(reachable);
    }

    @Test
    public void testClearGraph() {
        graphService.addEdge("A", "B", 1, 1, null);
        assertFalse(graphService.getAllEdges().isEmpty());
        graphService.clearGraph();
        assertTrue(graphService.getAllEdges().isEmpty());
        assertTrue(graphService.getAllNodeIds().isEmpty());
    }
    
    @Test
    public void testShortestPath() {
        graphService.addEdge("A", "B", 1, 2, null);
        graphService.addEdge("B", "C", 4, 1, null);
        graphService.addEdge("A", "C", 2, 5, null);
        List<String> path = graphService.findTemporalShortestPath("A", "C", 0, 10);
        assertEquals(List.of("A", "B", "C"), path);
    }

    @Test
    public void testFastestPath() {
        graphService.addEdge("A", "B", 2, 1, null);
        graphService.addEdge("B", "C", 4, 1, null);
        graphService.addEdge("A", "C", 9, 1, null);
        List<String> path = graphService.findFastestPath("A", "C", 0, 10);
        assertEquals(List.of("A", "C"), path);
    }

    @Test
    public void testEarliestArrivalPath() {
        graphService.addEdge("A", "B", 1, 3, null);
        graphService.addEdge("B", "C", 5, 1, null);
        graphService.addEdge("A", "C", 2, 3, null);
        List<String> path = graphService.findEarliestArrivalPath("A", "C", 0, 10);
        assertEquals(List.of("A", "C"), path);
    }

    @Test
    public void testLatestDeparturePath() {
        graphService.addEdge("A", "B", 1, 3, null);
        graphService.addEdge("B", "C", 5, 1, null);
        graphService.addEdge("A", "C", 5, 1, null);
        List<String> path = graphService.findLatestDeparturePath("A", "C", 0, 10);
        assertEquals(List.of("A", "C"), path);
    }
}

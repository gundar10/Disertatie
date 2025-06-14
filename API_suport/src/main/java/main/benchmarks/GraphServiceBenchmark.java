package main.benchmarks;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import main.services.GraphService;

public class GraphServiceBenchmark {

    public static void main(String[] args) {
        int[] sizes = {1000, 2000, 5000, 10000};

        try (PrintWriter writer = new PrintWriter(new FileWriter("benchmark_results.csv"))) {
            writer.println("Size,Operation,Time(ms)");

            for (int numNodes : sizes) {
                System.out.println("\n--- Benchmarking graph with " + numNodes + " nodes ---");
                GraphService graphService = new GraphService();
                long start = System.nanoTime();

                if (numNodes < 5000) {
                    generateCompleteGraph(graphService, numNodes);
                } else {
                    generateSparseGraph(graphService, numNodes, numNodes * 1000); // 1000 edges per node
                }

                long end = System.nanoTime();
                long generationTime = (end - start) / 1_000_000;
                System.out.println("Graph generation took: " + generationTime + " ms");
                writer.printf("%d,Graph Generation,%d\n", numNodes, generationTime);

                String source = "N0";
                String target = "N" + (numNodes - 1);
                int tAlpha = 0;
                int tOmega = 1000;

                benchmark(writer, numNodes, "Shortest Path", () -> graphService.findTemporalShortestPath(source, target, tAlpha, tOmega));
                benchmark(writer, numNodes, "Fastest Path", () -> graphService.findFastestPath(source, target, tAlpha, tOmega));
                benchmark(writer, numNodes, "Earliest Arrival Path", () -> graphService.findEarliestArrivalPath(source, target, tAlpha, tOmega));
                benchmark(writer, numNodes, "Latest Departure Path", () -> graphService.findLatestDeparturePath(source, target, tAlpha, tOmega));
                benchmark(writer, numNodes, "Reachability", () -> graphService.isReachable(source, target, tAlpha, tOmega));
            }

            System.out.println("\nBenchmark results saved to benchmark_results.csv");
        } catch (IOException e) {
            System.err.println("Error writing benchmark results: " + e.getMessage());
        }
    }

    private static void generateCompleteGraph(GraphService graphService, int numNodes) {
        Random rand = new Random(42);
        for (int i = 0; i < numNodes; i++) {
            for (int j = 0; j < numNodes; j++) {
                if (i != j) {
                    String from = "N" + i;
                    String to = "N" + j;
                    int startTime = rand.nextInt(1000);
                    int duration = 1 + rand.nextInt(10);
                    graphService.addEdge(from, to, startTime, duration, null);
                }
            }
        }
    }

    private static void generateSparseGraph(GraphService graphService, int numNodes, int numEdges) {
        Random rand = new Random(42);
        for (int i = 0; i < numEdges; i++) {
            String from = "N" + rand.nextInt(numNodes);
            String to = "N" + rand.nextInt(numNodes);
            if (!from.equals(to)) {
                int startTime = rand.nextInt(1000);
                int duration = 1 + rand.nextInt(10);
                graphService.addEdge(from, to, startTime, duration, null);
            }
        }
    }

    private static void benchmark(PrintWriter writer, int size, String label, Runnable task) {
        long start = System.nanoTime();
        task.run();
        long end = System.nanoTime();
        long duration = (end - start) / 1_000_000;
        System.out.println(label + " took: " + duration + " ms");
        writer.printf("%d,%s,%d\n", size, label, duration);
    }
}
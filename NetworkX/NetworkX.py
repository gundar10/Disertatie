import time
import random
import csv
import networkx as nx

def generate_complete_graph(n):
    G = nx.DiGraph()
    for i in range(n):
        for j in range(n):
            if i != j:
                start = random.randint(0, 1000)
                duration = random.randint(1, 10)
                G.add_edge(f"N{i}", f"N{j}", startTime=start, duration=duration)
    return G

def generate_sparse_graph(n, num_edges):
    G = nx.DiGraph()
    for _ in range(num_edges):
        u = f"N{random.randint(0, n - 1)}"
        v = f"N{random.randint(0, n - 1)}"
        while u == v:
            v = f"N{random.randint(0, n - 1)}"
        start = random.randint(0, 1000)
        duration = random.randint(1, 10)
        G.add_edge(u, v, startTime=start, duration=duration)
    return G

def simulate_temporal_reachability(G, source, t_alpha, t_omega):
    # Create a subgraph with valid temporal edges
    edges_in_window = [
        (u, v) for u, v, data in G.edges(data=True)
        if data['startTime'] >= t_alpha and (data['startTime'] + data['duration']) <= t_omega
    ]
    SG = G.edge_subgraph(edges_in_window).copy()

    start = time.time()
    reachable = nx.descendants(SG, source)
    elapsed = (time.time() - start) * 1000
    return len(reachable), elapsed

def benchmark_graph(n):
    print(f"\n--- NetworkX: Benchmarking {n} nodes ---")
    start = time.time()
    if n < 5000:
        G = generate_complete_graph(n)
    else:
        G = generate_sparse_graph(n, n * 1000)
    elapsed = (time.time() - start) * 1000
    print(f"Graph creation took: {elapsed:.2f} ms")
    
    reachable_count, reach_time = simulate_temporal_reachability(G, "N0", 0, 1000)
    writer.writerow([size, "Reachability", f"{reach_time:.2f}"])

    return elapsed

if __name__ == "__main__":
    sizes = [1000, 2000, 5000, 10000]
    with open("networkx_benchmark_results.csv", "w", newline="") as file:
        writer = csv.writer(file)
        writer.writerow(["Size", "Operation", "Time(ms)"])

        for size in sizes:
            time_ms = benchmark_graph(size)
            writer.writerow([size, "Graph Creation", f"{time_ms:.2f}"])

    print("\nResults saved to networkx_benchmark_results.csv")

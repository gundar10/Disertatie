import time
import csv
import random
import pytglib as tgl


def generate_complete_graph(num_nodes):
    edges = []
    for i in range(num_nodes):
        for j in range(num_nodes):
            if i != j:
                t = random.randint(0, 1000)
                tt = random.randint(1, 10)
                e = tgl.TemporalEdge()
                e.u = i
                e.v = j
                e.t = t
                e.tt = tt
                edges.append(e)
    return edges


def generate_sparse_graph(num_nodes, num_edges):
    edges = []
    for _ in range(num_edges):
        u = random.randint(0, num_nodes - 1)
        v = random.randint(0, num_nodes - 1)
        while u == v:
            v = random.randint(0, num_nodes - 1)
        t = random.randint(0, 1000)
        tt = random.randint(1, 10)
        e = tgl.TemporalEdge()
        e.u = u
        e.v = v
        e.t = t
        e.tt = tt
        edges.append(e)
    return edges


def benchmark_algorithm(label, func, *args):
    start = time.time()
    try:
        func(*args)
    except Exception as e:
        print(f"Error in {label}: {e}")
        return None
    return (time.time() - start) * 1000


def run_benchmark():
    sizes = [10]#, 2000, 5000, 10000]
    results = []

    for size in sizes:
        print(f"\n--- Benchmarking size: {size} ---")

        if size <= 5000:
            edge_list = generate_complete_graph(size)
        else:
            edge_list = generate_sparse_graph(size, size * 10)

        start = time.time()
        tgs = tgl.load_ordered_edge_list_from_list(size, edge_list)
        build_time = (time.time() - start) * 1000
        results.append([size, "Graph Creation", f"{build_time:.2f}"])

        tg = tgl.to_incident_lists(tgs)
        interval = tgs.getTimeInterval()
        source = 0
        target = size - 1

        operations = [
            ("Earliest Arrival", tgl.earliest_arrival_path, tg, source, target, interval),
            ("Latest Departure", tgl.latest_departure_path, tg, source, target, interval),
            ("Fastest Path", tgl.minimum_duration_path, tg, source, target, interval),
            ("Shortest Path", tgl.minimum_transition_time_path, tg, source, target, interval),
            ("Reachability", tgl.number_of_reachable_nodes, tgs, source, interval)
        ]

        for label, func, *args in operations:
            duration = benchmark_algorithm(label, func, *args)
            if duration is not None:
                results.append([size, label, f"{duration:.2f}"])
                print(f"{label} took {duration:.2f} ms")

    with open("pytglib_benchmark.csv", "w", newline="") as f:
        writer = csv.writer(f)
        writer.writerow(["Size", "Operation", "Time(ms)"])
        writer.writerows(results)

    print("\nResults written to pytglib_benchmark.csv")


if __name__ == "__main__":
    run_benchmark()

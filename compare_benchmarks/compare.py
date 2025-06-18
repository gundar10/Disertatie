import pandas as pd
import matplotlib.pyplot as plt

# Load benchmark data
java_df = pd.read_csv("benchmark_results.csv")
python_df = pd.read_csv("pytglib_benchmark.csv")
networkx_df = pd.read_csv("networkx_benchmark_results.csv")

# Add source labels
java_df["Source"] = "Java"
python_df["Source"] = "TGLib (Python)"
networkx_df["Source"] = "NetworkX"

# Combine datasets
df = pd.concat([java_df, python_df, networkx_df], ignore_index=True)

# Clean column names and types
df.columns = [col.strip() for col in df.columns]
df["Time(ms)"] = pd.to_numeric(df["Time(ms)"], errors='coerce')

# Generate comparison plots
operations = df["Operation"].unique()

for op in operations:
    op_data = df[df["Operation"] == op]
    plt.figure(figsize=(10, 6))
    for source in op_data["Source"].unique():
        subset = op_data[op_data["Source"] == source]
        plt.plot(subset["Size"], subset["Time(ms)"], marker="o", label=source)
    plt.title(f"Benchmark: {op}")
    plt.xlabel("Graph Size")
    plt.ylabel("Time (ms)")
    plt.legend()
    plt.grid(True)
    plt.tight_layout()
    filename = f"benchmark_{op.replace(' ', '_').lower()}.png"
    plt.savefig(filename)
    print(f"Saved: {filename}")

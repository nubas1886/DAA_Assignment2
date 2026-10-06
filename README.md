# DAA Assignment 2: In-Memory Workload Engine

**Author:** Kasymbayev Nurbol  
**Course:** Design and Analysis of Algorithms

## 📌 Project Description
This project implements three fundamental data structures from scratch in Java: `DynamicArray`, `MyLinkedList`, and `MinHeap`. The implementations strictly avoid `java.util` collections and are designed to store primitive `int` values to ensure accurate performance metrics and memory cache behavior.

The project includes a benchmarking suite to test these structures under four specific workloads:
* **W1 (Random Access):** Measuring index-based read performance.
* **W2 (Search):** Measuring element lookup times.
* **W3 (Insert & Remove):** Measuring modification times at boundaries (head) and middle.
* **W4 (Priority Processing):** Measuring minimum element extraction in a priority queue.

## 📂 Project Structure
* `src/main/java/daa/ds/` — Contains data structure implementations (`DynamicArray`, `MyLinkedList`, `MinHeap`).
* `src/main/java/daa/metrics/` — Contains the `Metrics` counter and the `Main` entry point.
* `src/main/java/daa/bench/` — Contains the `Benchmark` engine for running the workloads.
* `src/test/java/daa/ds/` — Contains JUnit 5 tests verifying structure correctness and heap properties.
* `results/` — Contains the generated `results.csv` and performance plots.

## 🚀 How to Build and Run

### 1. Build the Project
Open the project in IntelliJ IDEA (or any other IDE) as a Maven/Gradle project. Let the IDE resolve the JUnit 5 dependencies.

### 2. Run the Benchmark
To execute the workloads and generate metrics:
1. Navigate to `src/main/java/daa/metrics/Main.java`.
2. Run the `main` method.
3. The benchmark will perform 1 warm-up run and 5 measurement runs, outputting the median values.
4. The results will be automatically saved to `results.csv` in the project root.

### 3. Run Tests
To verify the correctness of the data structures:
1. Navigate to `src/test/java/daa/ds/DataStructuresTest.java`.
2. Run the test class using JUnit 5.
3. The tests validate dynamic resizing, node pointer integrity, and the non-decreasing output property of the MinHeap. 
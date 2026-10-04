package daa.bench;

import ds.*;
import metrics.Metrics;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Random;

public class Benchmark {
    private static final int[] SIZES = {100, 1000, 10000, 100000};
    private static final int RUNS = 6; // 1 прогрев + 5 замеров

    public static void runAll(String filename) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
            pw.println("workload,variant,structure,n,time_ms,steps,moves,comparisons");
            for (int n : SIZES) {
                runListWorkload(pw, "W1_random_access", "-", n);
                runListWorkload(pw, "W2_search", "-", n);
                runListWorkload(pw, "W3_insert_remove", "head", n);
                runListWorkload(pw, "W3_insert_remove", "middle", n);
                runW4(pw, n);
            }
        } catch (IOException e) {
            System.err.println("Ошибка записи в CSV: " + e.getMessage());
        }
    }

    private static void runListWorkload(PrintWriter pw, String workload, String variant, int n) {
        Metrics arrMetrics = new Metrics();
        Metrics listMetrics = new Metrics();

        long[] arrTimes = new long[RUNS];
        long[] listTimes = new long[RUNS];

        for (int i = 0; i < RUNS; i++) {
            arrTimes[i] = executeListTask(new DynamicArray(arrMetrics), workload, variant, n, arrMetrics, i > 0);
            listTimes[i] = executeListTask(new MyLinkedList(listMetrics), workload, variant, n, listMetrics, i > 0);
        }

        writeResult(pw, workload, variant, "DynamicArray", n, getMedian(arrTimes), arrMetrics);
        writeResult(pw, workload, variant, "MyLinkedList", n, getMedian(listTimes), listMetrics);
    }

    private static long executeListTask(IntList list, String workload, String variant, int n, Metrics metrics, boolean recordMetrics) {
        Random rand = new Random(42);
        // Предзаполнение
        for (int i = 0; i < n; i++) list.add(rand.nextInt());
        if (recordMetrics) metrics.reset(); // Сбрасываем метрики после заполнения

        long start = System.nanoTime();
        if (workload.equals("W1_random_access")) {
            for (int i = 0; i < 10000; i++) list.get(rand.nextInt(n));
        } else if (workload.equals("W2_search")) {
            for (int i = 0; i < 1000; i++) {
                // Половина запросов - существующие элементы, половина - случайные
                list.contains(i % 2 == 0 ? list.get(rand.nextInt(n)) : rand.nextInt());
            }
        } else if (workload.equals("W3_insert_remove")) {
            int index = variant.equals("head") ? 0 : n / 2;
            for (int i = 0; i < 1000; i++) list.add(index, rand.nextInt());
            for (int i = 0; i < 1000; i++) list.remove(index);
        }
        return (System.nanoTime() - start) / 1_000_000; // в миллисекунды
    }

    private static void runW4(PrintWriter pw, int n) {
        Metrics metrics = new Metrics();
        long[] times = new long[RUNS];

        for (int i = 0; i < RUNS; i++) {
            Random rand = new Random(42);
            MinHeap heap = new MinHeap(n, metrics);
            for (int j = 0; j < n; j++) heap.insert(rand.nextInt());

            if (i > 0) metrics.reset();

            long start = System.nanoTime();
            for (int j = 0; j < n; j++) heap.extractMin();
            times[i] = (System.nanoTime() - start) / 1_000_000;
        }
        writeResult(pw, "W4_priority", "-", "MinHeap", n, getMedian(times), metrics);
    }

    private static long getMedian(long[] times) {
        long[] actualRuns = Arrays.copyOfRange(times, 1, RUNS); // Отбрасываем первый прогон (прогрев)
        Arrays.sort(actualRuns);
        return actualRuns[actualRuns.length / 2];
    }

    private static void writeResult(PrintWriter pw, String w, String var, String struct, int n, long time, Metrics m) {
        pw.printf("%s,%s,%s,%d,%d,%d,%d,%d%n", w, var, struct, n, time, m.steps, m.moves, m.comparisons);
    }
}
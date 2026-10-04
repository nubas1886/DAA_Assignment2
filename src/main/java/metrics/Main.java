package metrics;
import daa.bench.Benchmark;

public class Main {
    public static void main(String[] args) {
        System.out.println("Запуск бенчмарков...");
        Benchmark.runAll("results.csv");
        System.out.println("Готово! Результаты сохранены в results.csv");
    }
}
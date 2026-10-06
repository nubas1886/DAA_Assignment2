package metrics;

import org.knowm.xchart.*;
import org.knowm.xchart.style.Styler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Plotter {

    static class DataPoint {
        int n;
        double time;
        long steps, moves, comparisons;

        public DataPoint(int n, double time, long steps, long moves, long comparisons) {
            this.n = n;
            this.time = time;
            this.steps = steps;
            this.moves = moves;
            this.comparisons = comparisons;
        }
    }

    public static void main(String[] args) throws Exception {
        File plotDir = new File("results/plots");
        if (!plotDir.exists()) plotDir.mkdirs();

        Map<String, Map<String, List<DataPoint>>> allData = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader("results.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(",");
                if (parts.length < 8) continue;

                try {
                    String workload = parts[0].trim();
                    String structure = parts[2].trim();
                    int n = Integer.parseInt(parts[3].trim());
                    double time = Double.parseDouble(parts[4].trim());
                    long steps = Long.parseLong(parts[5].trim());
                    long moves = Long.parseLong(parts[6].trim());
                    long comparisons = Long.parseLong(parts[7].trim());

                    allData.putIfAbsent(workload, new HashMap<>());
                    allData.get(workload).putIfAbsent(structure, new ArrayList<>());
                    allData.get(workload).get(structure).add(new DataPoint(n, time, steps, moves, comparisons));
                } catch (NumberFormatException e) {
                    continue;
                }
            }
        }

        if (allData.isEmpty()) {
            System.out.println("❌ Ошибка: В файле results.csv не найдено числовых данных.");
            return;
        }

        for (String workload : allData.keySet()) {
            generateTimeChart(workload, allData.get(workload));
            generateOperationsChart(workload, allData.get(workload));
        }

        System.out.println("✅ Готово! Графики с правильными осями 100-1000-10000 сгенерированы.");
    }

    private static void generateTimeChart(String workload, Map<String, List<DataPoint>> structData) throws Exception {
        XYChart chart = new XYChartBuilder().width(800).height(600).title(workload + " - Time vs N").xAxisTitle("Size (N)").yAxisTitle("Time (ms)").build();
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNW);
        chart.getStyler().setMarkerSize(8);

        // Включаем логарифмическую шкалу для оси X (100, 1000, 10000)
        chart.getStyler().setXAxisLogarithmic(true);

        for (String struct : structData.keySet()) {
            List<DataPoint> data = structData.get(struct);
            double[] xData = data.stream().mapToDouble(d -> d.n).toArray();
            double[] yData = data.stream().mapToDouble(d -> d.time).toArray();
            chart.addSeries(struct, xData, yData);
        }

        BitmapEncoder.saveBitmap(chart, "results/plots/" + workload + "_time.png", BitmapEncoder.BitmapFormat.PNG);
    }

    private static void generateOperationsChart(String workload, Map<String, List<DataPoint>> structData) throws Exception {
        XYChart chart = new XYChartBuilder().width(800).height(600).title(workload + " - Operations vs N").xAxisTitle("Size (N)").yAxisTitle("Count").build();
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNW);

        // Включаем логарифмическую шкалу для обеих осей
        chart.getStyler().setXAxisLogarithmic(true);
        chart.getStyler().setYAxisLogarithmic(true);

        for (String struct : structData.keySet()) {
            List<DataPoint> data = structData.get(struct);
            double[] xData = data.stream().mapToDouble(d -> d.n).toArray();
            // XChart на логарифмической оси Y не любит нули, поэтому заменяем 0 на 1 (на графике 10^0 = 1, это почти 0)
            double[] steps = data.stream().mapToDouble(d -> d.steps == 0 ? 1 : d.steps).toArray();
            double[] moves = data.stream().mapToDouble(d -> d.moves == 0 ? 1 : d.moves).toArray();
            double[] comps = data.stream().mapToDouble(d -> d.comparisons == 0 ? 1 : d.comparisons).toArray();

            chart.addSeries(struct + " Steps", xData, steps);
            chart.addSeries(struct + " Moves", xData, moves);
            chart.addSeries(struct + " Comparisons", xData, comps);
        }

        BitmapEncoder.saveBitmap(chart, "results/plots/" + workload + "_operations.png", BitmapEncoder.BitmapFormat.PNG);
    }
}
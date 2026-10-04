package ds;
import metrics.Metrics;

public class MinHeap {
    private int[] data;
    private int size;
    private final Metrics metrics;

    public MinHeap(int capacity, Metrics metrics) {
        this.data = new int[capacity > 0 ? capacity : 10];
        this.size = 0;
        this.metrics = metrics;
    }

    private void grow() {
        int[] newData = new int[data.length * 2];
        System.arraycopy(data, 0, newData, 0, size);
        data = newData;
    }

    public void insert(int x) {
        if (size == data.length) grow();
        data[size] = x;
        metrics.moves++;
        bubbleUp(size);
        size++;
    }

    private void bubbleUp(int index) {
        while (index > 0) {
            int parent = (index - 1) / 2;
            metrics.steps += 2; // Чтение текущего элемента и родителя
            metrics.comparisons++;
            if (data[index] < data[parent]) {
                int temp = data[index];
                data[index] = data[parent];
                data[parent] = temp;
                metrics.moves += 3; // Обмен через temp (3 перемещения)
                index = parent;
            } else {
                break;
            }
        }
    }

    public int extractMin() {
        if (size == 0) throw new IllegalStateException("Heap is empty");
        int min = data[0];
        metrics.steps++;

        data[0] = data[size - 1];
        metrics.moves++;
        size--;

        if (size > 0) {
            bubbleDown(0);
        }
        return min;
    }

    private void bubbleDown(int index) {
        while (true) {
            int left = 2 * index + 1;
            int right = 2 * index + 2;
            int smallest = index;

            if (left < size) {
                metrics.steps += 2;
                metrics.comparisons++;
                if (data[left] < data[smallest]) smallest = left;
            }

            if (right < size) {
                metrics.steps += 2;
                metrics.comparisons++;
                if (data[right] < data[smallest]) smallest = right;
            }

            if (smallest != index) {
                int temp = data[index];
                data[index] = data[smallest];
                data[smallest] = temp;
                metrics.moves += 3;
                index = smallest;
            } else {
                break;
            }
        }
    }

    public int peekMin() {
        if (size == 0) throw new IllegalStateException("Heap is empty");
        metrics.steps++;
        return data[0];
    }
}
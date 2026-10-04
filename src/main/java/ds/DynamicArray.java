package ds;
import metrics.Metrics;

public class DynamicArray implements IntList {
    private int[] data;
    private int size;
    private final Metrics metrics;

    public DynamicArray(Metrics metrics) {
        this.data = new int[10];
        this.size = 0;
        this.metrics = metrics;
    }

    private void grow() {
        int[] newData = new int[data.length * 2];
        for (int i = 0; i < size; i++) {
            newData[i] = data[i];
            metrics.steps++;
            metrics.moves++;
        }
        data = newData;
    }

    @Override
    public void add(int x) {
        if (size == data.length) grow();
        data[size++] = x;
        metrics.moves++;
    }

    @Override
    public void add(int index, int x) {
        if (index < 0 || index > size) throw new IndexOutOfBoundsException("Invalid index");
        if (size == data.length) grow();

        for (int i = size; i > index; i--) {
            data[i] = data[i - 1];
            metrics.steps++;
            metrics.moves++;
        }
        data[index] = x;
        size++;
        metrics.moves++;
    }

    @Override
    public void remove(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException("Invalid index");

        for (int i = index; i < size - 1; i++) {
            data[i] = data[i + 1];
            metrics.steps++;
            metrics.moves++;
        }
        size--;
    }

    @Override
    public int get(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException("Invalid index");
        metrics.steps++;
        return data[index];
    }

    @Override
    public boolean contains(int x) {
        for (int i = 0; i < size; i++) {
            metrics.steps++;
            metrics.comparisons++;
            if (data[i] == x) return true;
        }
        return false;
    }
}
import ds.DynamicArray;
import ds.MinHeap;
import metrics.Metrics;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DataStructuresTest {

    @Test
    void testHeapSortedOutput() {
        Metrics m = new Metrics();
        MinHeap heap = new MinHeap(10, m);
        int[] input = {5, 3, 8, 1, 9, 2};

        for (int x : input) {
            heap.insert(x);
        }

        int prev = heap.extractMin();
        for (int i = 1; i < input.length; i++) {
            int current = heap.extractMin();
            assertTrue(prev <= current, "Элементы должны возвращаться в порядке неубывания");
            prev = current;
        }
    }

    @Test
    void testDynamicArray() {
        Metrics m = new Metrics();
        DynamicArray arr = new DynamicArray(m);
        arr.add(10);
        arr.add(0, 5);
        assertEquals(5, arr.get(0));
        assertEquals(10, arr.get(1));
    }
}
# Assignment 2 Report
**Author:** Kassymbayev Nurbol

## 1. Asymptotic Complexity Table

| Structure | Operation | Best Case | Average Case | Worst Case | Justification |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **DynamicArray** | `get(index)` | Θ(1) | Θ(1) | Θ(1) | Direct memory access via array index calculation. |
| | `add(x)` | Θ(1) | Θ(1) | O(n) | Appending is O(1). Resizing takes O(n) but happens rarely (amortized O(1)). |
| | `add(index, x)`| Θ(1) | Θ(n) | O(n) | Best: add at the end. Worst: add at index 0, requiring n right-shifts. |
| | `remove(index)`| Θ(1) | Θ(n) | O(n) | Best: remove last element. Worst: remove at index 0, requiring n left-shifts. |
| | `contains(x)` | Θ(1) | Θ(n) | O(n) | Best: element is at index 0. Worst: element not found, iterates full array. |
| **MyLinkedList** | `get(index)` | Θ(1) | Θ(n) | O(n) | Best: index 0 (head). Worst: index n-1, requires traversing all nodes. |
| | `add(x)` | Θ(1) | Θ(1) | Θ(1) | Using the `tail` pointer allows constant time insertion at the end. |
| | `add(index, x)`| Θ(1) | Θ(n) | O(n) | Best: index 0 (update head). Worst: index n-1, requires finding the node. |
| | `remove(index)`| Θ(1) | Θ(n) | O(n) | Best: index 0 (update head). Worst: must traverse to the node before target. |
| | `contains(x)` | Θ(1) | Θ(n) | O(n) | Sequential search traversing pointers until `x` is found or null is reached. |
| **MinHeap** | `insert(x)` | Θ(1) | O(log n) | O(log n) | Best: new element is the largest. Worst: bubbles up to the root. |
| | `extractMin()` | Θ(1) | O(log n) | O(log n) | Min is at root O(1), but bubble-down restores heap property in O(log n). |
| | `peekMin()` | Θ(1) | Θ(1) | Θ(1) | Directly returns `data[0]` without modification. |

*Note: Auxiliary space for all structures is O(n) to store the actual elements.*

## 2. Loop Invariant Proofs

### Proof 1: `DynamicArray.contains(int x)`
**Code snippet:**
```java
for (int i = 0; i < size; i++) {
    if (data[i] == x) return true;
}
return false;
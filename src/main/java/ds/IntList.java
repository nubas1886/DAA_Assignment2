package ds;

public interface IntList {
    void add(int x);
    void add(int index, int x);
    void remove(int index);
    int get(int index);
    boolean contains(int x);
}
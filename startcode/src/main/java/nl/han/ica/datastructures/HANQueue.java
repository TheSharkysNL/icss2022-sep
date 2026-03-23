package nl.han.ica.datastructures;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class HANQueue<T> implements IHANQueue<T> {
    private final List<T> list;

    public HANQueue() {
        list = List.of();
    }

    public HANQueue(Collection<? extends T> collection) {
        list = new LinkedList<>(collection);
    }

    @Override
    public void clear() {
        list.clear();
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public void enqueue(T value) {
        list.add(value);
    }

    @Override
    public T dequeue() {
        return list.removeFirst();
    }

    @Override
    public T peek() {
        return list.getFirst();
    }

    @Override
    public int getSize() {
        return 0;
    }
}
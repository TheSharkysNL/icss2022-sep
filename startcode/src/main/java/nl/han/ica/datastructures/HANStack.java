package nl.han.ica.datastructures;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class HANStack<T> implements IHANStack<T> {
    private final List<T> underlyingList;

    public HANStack() {
        underlyingList = List.of();
    }

    public HANStack(Collection<? extends T> collection) {
        underlyingList = new LinkedList<>(collection);
    }

    @Override
    public void push(T value) {
        underlyingList.add(value);
    }

    @Override
    public T pop() {
        return underlyingList.removeLast();
    }

    @Override
    public T peek() {
        return underlyingList.getLast();
    }
}


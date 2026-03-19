package nl.han.ica.datastructures;

import java.util.Collection;
import java.util.Iterator;

public class HANLinkedList<T> implements IHANLinkedList<T>, Iterable<T> {
    private HANLinkedListNode<T> head;
    private int length;

    public HANLinkedList() {

    }

    public HANLinkedList(Collection<? extends T> collection) {
        length = collection.size();
        fromIterator(collection.iterator());
    }

    @Override
    public void addFirst(T value) {
        length++;
        HANLinkedListNode<T> node = new HANLinkedListNode<>(value);

        if (head != null) {
            node.setNext(head);
        }

        head = node;
    }

    @Override
    public void clear() {
        head = null;
        length = 0;
    }

    @Override
    public void insert(int index, T value) {
        HANLinkedListNode<T> nodeAtIndex = getNodeAtPosition(index);
        HANLinkedListNode<T> newNode = new HANLinkedListNode<>(value);

        newNode.setNext(nodeAtIndex);
        length++;
    }

    @Override
    public void delete(int pos) {
        HANLinkedListNode<T> nodeAtIndex = getNodeAtPosition(pos);

        nodeAtIndex.deleteNode();

        length--;
    }

    @Override
    public T get(int pos) {
        HANLinkedListNode<T> nodeAtIndex = getNodeAtPosition(pos);

        return nodeAtIndex.getValue();
    }

    @Override
    public void removeFirst() {
        head = head.next;

        length--;
    }

    @Override
    public T getFirst() {
        if (head == null) {
            return null;
        }
        return head.value;
    }

    @Override
    public int getSize() {
        return length;
    }

    @Override
    public Iterator<T> iterator() {
        if (head == null) {
            return new EmptyHANLinkedListIterator<>();
        }
        return new HANLinkedListIterator<>(head);
    }

    private HANLinkedListNode<T> getNodeAtPosition(int position) {
        if (position == 0) {
            return head;
        }

        HANLinkedListNode<T> current = head;

        while (position > 0 && current != null) {
            current = current.getNext();

            position--;
        }

        if (position > 0) {
            throw new IndexOutOfBoundsException(position);
        }

        return current;
    }

    private void fromIterator(Iterator<? extends T> iterator) {
        if (!iterator.hasNext()) {
            return;
        }

        var first = iterator.next();
        head = new HANLinkedListNode<>(first);

        HANLinkedListNode<T> current = head;
        while (iterator.hasNext()) {
            var value = iterator.next();

            current.setNext(new HANLinkedListNode<>(value));
            current = current.getNext();
        }
    }

    private static class HANLinkedListNode<T> {
        private T value;
        private HANLinkedListNode<T> next;
        private HANLinkedListNode<T> previous;

        public HANLinkedListNode(T value) {
            this.value = value;
        }

        public void setNext(HANLinkedListNode<T> next) {
            this.next = next;
            if (next.previous != null && next.previous.next != null) {
                next.previous.next = this;
            }
            next.previous = this;
        }

        public void setPrevious(HANLinkedListNode<T> previous) {
            this.previous = previous;
            previous.next = this;
        }

        public void deleteNode() {
            previous.next = next;
            next.previous = previous;
        }

        public void setValue(T value) {
            this.value = value;
        }

        public HANLinkedListNode<T> getNext() {
            return next;
        }

        public T getValue() {
            return value;
        }

        public HANLinkedListNode<T> getPrevious() {
            return previous;
        }
    }

    private static class HANLinkedListIterator<T> implements Iterator<T> {
        private HANLinkedListNode<T> current;

        public HANLinkedListIterator(HANLinkedListNode<T> current) {
            this.current = current;
        }

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public T next() {
            T value = current.getValue();
            current = current.next;

            return value;
        }
    }

    private static class EmptyHANLinkedListIterator<T> implements Iterator<T> {

        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public T next() {
            return null;
        }
    }

}

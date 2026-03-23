package nl.han.ica.datastructures;

import java.util.*;

public class HANHashMap<K, V> implements IHANHashMap<K, V> {
    private Entry<K, V>[] entries;
    private int length;
    private int freeSpot;

    private final static int[] primes = new int[]
    {
        3, 7, 11, 17, 23, 29, 37, 47, 59, 71, 89, 107, 131, 163, 197, 239, 293, 353, 431, 521, 631, 761, 919,
        1103, 1327, 1597, 1931, 2333, 2801, 3371, 4049, 4861, 5839, 7013, 8419, 10103, 12143, 14591,
        17519, 21023, 25229, 30293, 36353, 43627, 52361, 62851, 75431, 90523, 108631, 130363, 156437,
        187751, 225307, 270371, 324449, 389357, 467237, 560689, 672827, 807403, 968897, 1162687, 1395263,
        1674319, 2009191, 2411033, 2893249, 3471899, 4166287, 4999559, 5999471, 7199369
    };

    private final static int hashPrime = 101;

    public HANHashMap() {
        this(0);
    }

    public HANHashMap(int capacity) {
        length = capacity;
        entries = new Entry[getNextPrime(capacity)];
    }

    @Override
    public int size() {
        return length;
    }

    @Override
    public boolean isEmpty() {
        return length != 0;
    }

    @Override
    public boolean containsKey(Object key) {
        return getEntry(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        for (int i = 0; i < entries.length; i++) {
            Entry<K, V> entry = entries[i];
            if (entry != null && Objects.equals(entry, value)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public V get(Object key) {
        Entry<K, V> entry = getEntry(key);
        if (entry != null) {
            return entry.getValue();
        }

        return null;
    }

    @Override
    public V put(K key, V value) {
        if (entries.length == length) {
            resize(entries.length + 1);
        }

        int hashCode = key.hashCode();
        int index = Math.abs(hashCode % entries.length);

        Entry<K, V> entry = entries[index];
        if (entry == null) {
            length++;
            entries[index] = new Entry<>(key, value, hashCode);
            return null;
        }

        int previousIndex = -1;
        while (true) {
            if (entry.hashcode == hashCode && Objects.equals(entry.getKey(), key)) {
                V oldValue = entry.getValue();
                entry.setValue(value);
                return oldValue;
            }

            if (!entry.hasNext()) {
                break;
            }

            previousIndex = entry.getNext();
            entry = entries[previousIndex];
        }

        int freeSpot = getFreeSpot();
        entries[freeSpot] = new Entry<>(key, value, hashCode);
        length++;

        entry.setNext(freeSpot);
        entry.setPrevious(previousIndex);

        return null;
    }

    @Override
    public V remove(Object key) {
        int index = getEntryIndex(key);

        if (index == -1) {
            return null;
        }

        Entry<K, V> entry = entries[index];

        entries[index] = null;
        length--;

        if (entry.hasNext() && entry.hasPrevious()) {
            entries[entry.getPrevious()].setNext(entry.getNext());
        } else if (entry.hasNext()) {
            entries[index] = entries[entry.getNext()];
        } else if (entry.hasPrevious()) {
            entries[entry.getPrevious()].setNext(-1);
        }

        return entry.getValue();
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        int newLength = length + m.size();
        if (newLength > entries.length) {
            resize(newLength);
        }

        for (Map.Entry<? extends K, ? extends V> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void clear() {
        freeSpot = 0;
        length = 0;
        Arrays.fill(entries, null);
    }

    @Override
    public Set<K> keySet() {
        Set<K> set = new HashSet<>(length);
        for (Entry<K, V> entry : entries) {
            if (entry != null) {
                set.add(entry.getKey());
            }
        }

        return set;
    }

    @Override
    public Collection<V> values() {
        List<V> set = new ArrayList<>(length);
        for (Entry<K, V> entry : entries) {
            if (entry != null) {
                set.add(entry.getValue());
            }
        }

        return set;
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        Set<Map.Entry<K, V>> set = new HashSet<>(length);
        for (Entry<K, V> entry : entries) {
            if (entry != null) {
                set.add(new AbstractMap.SimpleEntry<>(entry.getKey(), entry.getValue()));
            }
        }

        return set;
    }

    @Override
    public Iterator<Map.Entry<K, V>> iterator() {
        return new EntryIterator<>(entries);
    }

    private int getEntryIndex(Object key) {
        int hashCode = key.hashCode();
        int index = Math.abs(hashCode % entries.length);

        Entry<K, V> entry = entries[index];

        while (entry != null) {
            if (entry.hashcode == hashCode && Objects.equals(entry.getKey(), key)) {
                return index;
            }

            if (!entry.hasNext()) {
                break;
            }

            index = entry.getNext();
            entry = entries[index];
        }

        return -1;
    }

    private Entry<K, V> getEntry(Object key) {
        int entryIndex = getEntryIndex(key);
        if (entryIndex == -1) {
            return null;
        }

        return entries[entryIndex];
    }

    private void resize(int newSize) {
        int primeSize = getNextPrime(newSize);
        Entry<K, V>[] previousEntries = entries;
        length = 0;
        freeSpot = 0;

        entries = new Entry[primeSize];
        for (Entry<K, V> previousEntry : previousEntries) {
            if (previousEntry != null) {
                put(previousEntry.getKey(), previousEntry.getValue());
            }
        }
    }

    private int getFreeSpot() {
        for (int i = freeSpot; i < entries.length; i++) {
            if (entries[i] == null) {
                freeSpot = i;
                return i;
            }
        }

        return -1;
    }

    private static boolean isPrime(int candidate)
    {
        if ((candidate & 1) != 0)
        {
            int limit = (int)Math.sqrt(candidate);
            for (int divisor = 3; divisor <= limit; divisor += 2)
            {
                if ((candidate % divisor) == 0)
                    return false;
            }
            return true;
        }
        return candidate == 2;
    }

    private static int getNextPrime(int min) {
        for (int prime : primes) {
            if (prime >= min) {
                return prime;
            }
        }

        for (int i = (min | 1); i < Integer.MAX_VALUE; i += 2)
        {
            if (isPrime(i) && ((i - 1) % hashPrime != 0))
                return i;
        }

        return min;
    }

    private static class Entry<K, V> {
        private Object key;
        private Object value;
        private int next;
        private int previous;
        public int hashcode;

        public Entry(K key, V value, int hashCode) {
            this.key = key;
            this.value = value;
            this.next = -1;
            this.previous = -1;
            this.hashcode = hashCode;
        }

        public boolean hasNext() {
            return next >= 0;
        }

        public int getNext() {
            return next;
        }

        public void setNext(int value) {
            next = value;
        }

        public K getKey() {
            return (K)key;
        }

        public V getValue() {
            return (V)value;
        }

        public void setKey(K key) {
            this.key = key;
        }

        public void setValue(V value) {
            this.value = value;
        }

        public void setPrevious(int previous) {
            this.previous = previous;
        }

        public int getPrevious() {
            return previous;
        }

        public boolean hasPrevious() {
            return previous != -1;
        }
    }

    private static class EntryIterator<K, V> implements Iterator<Map.Entry<K, V>> {
        private final Entry<K, V>[] entries;
        private int index;

        public EntryIterator(Entry<K, V>[] entries) {
            this.entries = entries;
        }

        @Override
        public boolean hasNext() {
            return entries.length < index;
        }

        @Override
        public Map.Entry<K, V> next() {
            Entry<K, V> entry = entries[index++];
            while (entry == null) {
                entry = entries[index++];
            }

            return new AbstractMap.SimpleEntry<>(entry.getKey(), entry.getValue());
        }
    }
}

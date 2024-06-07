package ar.edu.itba.pod.api.models;

import java.util.*;
import java.util.function.Consumer;

public class TopNSet<K extends Comparable<K>,V extends Comparable<V>> implements Iterable<Map.Entry<K, NavigableSet<V>>> {
    private final SortedMap<K, NavigableSet<V>> map = new TreeMap<>();

    private final int n;

    public TopNSet(int n) {
        this.n = n;
    }

    public void add(K key, V value) {
        map.putIfAbsent(key, new TreeSet<>());
        NavigableSet<V> set = map.get(key);
        if(set.size() == n ){
            V lastValue = set.last();
            if(lastValue.compareTo(value) > 0 ){
                set.pollLast();
                set.add(value);
            }
        }else{
            set.add(value);
        }
    }


    @Override
    public Iterator<Map.Entry<K, NavigableSet<V>>> iterator() {
        return map.entrySet().iterator();
    }

    @Override
    public void forEach(Consumer<? super Map.Entry<K, NavigableSet<V>>> action) {
        map.entrySet().forEach(action);
    }
}

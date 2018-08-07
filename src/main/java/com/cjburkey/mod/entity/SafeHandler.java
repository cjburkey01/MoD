package com.cjburkey.mod.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

final class SafeHandler<T extends Handled> implements Iterable<T> {
    
    private final Set<T> items = new HashSet<>();
    private final Queue<T> itemsToAdd = new LinkedList<>();
    private final Queue<T> itemsToRem = new LinkedList<>();
    
    void add(T item) {
        itemsToAdd.offer(item);
    }
    
    void remove(T item) {
        itemsToRem.offer(item);
    }
    
    void update() {
        List<T> addedItems = new ArrayList<>();
        List<T> removedItems = new ArrayList<>();
        while (!itemsToAdd.isEmpty()) {
            T item = itemsToAdd.poll();
            items.add(item);
            addedItems.add(item);
        }
        for (T addedItem : addedItems) {
            addedItem.onAdd();
        }
        while (!itemsToRem.isEmpty()) {
            T item = itemsToRem.poll();
            items.remove(item);
            removedItems.add(item);
        }
        for (T removedItem : removedItems) {
            removedItem.onRemove();
        }
    }
    
    public Iterator<T> iterator() {
        return items.iterator();
    }
    
}
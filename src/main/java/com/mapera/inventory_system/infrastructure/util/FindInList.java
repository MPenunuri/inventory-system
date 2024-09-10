package com.mapera.inventory_system.infrastructure.util;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import com.mapera.inventory_system.domain.common.Identifiable;

public class FindInList {

    public static <T extends Identifiable> Optional<T> findById(List<T> list, long id) {
        return list.stream()
                .filter(item -> item.getId() == id)
                .findFirst();
    }

    public static <T extends Identifiable> T getById(List<T> list, long id, String tag) {
        Optional<T> found = findById(list, id);
        if (!found.isPresent()) {
            throw new IllegalArgumentException(tag + " not found");
        }
        T element = found.get();
        return element;
    }

    public static <T extends Identifiable> boolean findByIdAndRemove(List<T> list, long id) {
        Iterator<T> iterator = list.iterator();
        while (iterator.hasNext()) {
            T item = iterator.next();
            if (item.getId() == id) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }
}

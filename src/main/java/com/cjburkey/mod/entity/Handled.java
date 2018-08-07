package com.cjburkey.mod.entity;

import java.util.Objects;
import java.util.UUID;

public abstract class Handled {
    
    public final UUID uuid = UUID.randomUUID();
    
    abstract void onAdd();
    abstract void onRemove();
    
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Handled handled = (Handled) o;
        return Objects.equals(uuid, handled.uuid);
    }
    
    public int hashCode() {
        return Objects.hash(uuid);
    }
}
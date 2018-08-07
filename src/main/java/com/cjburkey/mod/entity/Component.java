package com.cjburkey.mod.entity;

public abstract class Component extends Handled {
    
    public final Entity parent = null;
    
    protected void onAdd() {
    }
    
    protected void onRemove() {
    }
    
    protected void onUpdate(float deltaTime) {
    }
    
    protected void onRender(float deltaTime) {
    }
    
}

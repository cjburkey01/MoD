package com.cjburkey.mod.entity;

import com.cjburkey.mod.core.Debug;

public final class EntityWorld {
    
    private final SafeHandler<Entity> entities = new SafeHandler<>();
    
    public Entity createEntity() {
        Entity entity = new Entity();
        entities.add(entity);
        return entity;
    }
    
    public void removeEntity(Entity entity) {
        entities.remove(entity);
    }
    
    public void onUpdate(float deltaTime) {
        entities.update();
        entities.forEach(e -> e.onUpdate(deltaTime));
    }
    
    public void onRender(float deltaTime) {
        entities.forEach(e -> e.onRender(deltaTime));
    }
    
    public void onExit() {
        entities.forEach(this::removeEntity);
        onUpdate(0.0f);
    }
    
}
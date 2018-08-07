package com.cjburkey.mod.entity;

import com.cjburkey.mod.core.Debug;
import com.cjburkey.mod.component.Transform;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public final class Entity extends Handled {
    
    private final SafeHandler<Component> components = new SafeHandler<>();
    
    public final Transform transform;
    
    Entity() {
        transform = addComponent(Transform.class);
    }
    
    public <T extends Component> T addComponent(Class<T> type, Object... args) {
        T component = null;
        try {
            for (Constructor<?> constructor : type.getConstructors()) {
                if (args.length != constructor.getParameterCount()) {
                    continue;
                }
                int i = 0;
                boolean fail = false;
                for (Class<?> param : constructor.getParameterTypes()) {
                    Class<?> present = args[i].getClass();
                    if (!param.isAssignableFrom(present)) {
                        if(param.isPrimitive()) {
                            fail = !((int.class.equals(param) && Integer.class.equals(present))
                                    || (long.class.equals(param) && Long.class.equals(present))
                                    || (char.class.equals(param) && Character.class.equals(present))
                                    || (short.class.equals(param) && Short.class.equals(present))
                                    || (boolean.class.equals(param) && Boolean.class.equals(present))
                                    || (byte.class.equals(param) && Byte.class.equals(present))
                                    || (float.class.equals(param) && Float.class.equals(present))
                                    || (double.class.equals(param) && Double.class.equals(present)));
                        } else {
                            fail = true;
                        }
                        if (fail) {
                            break;
                        }
                    }
                    i++;
                }
                if (fail) {
                    continue;
                }
                component = type.cast(constructor.newInstance(args));
                break;
            }
        } catch (Exception e) {
            Debug.exception(e);
        }
        if (component == null) {
            Debug.error("Failed to locate correct constructor for supplied arguments");
            return null;
        }
        
        components.add(component);
        
        try {
            Field field = component.getClass().getField("parent");
            field.setAccessible(true);
            field.set(component, this);
            
            return component;
        } catch (Exception e) {
            Debug.exception(e);
        }
        
        return null;
    }
    
    public <T extends Component> void removeComponent(T component) {
        components.remove(component);
    }
    
    public <T extends Component> void removeComponent(Class<T> type) {
        components.remove(getComponent(type));
    }
    
    public <T extends Component> void removeComponents(Class<T> type) {
        getComponents(type).forEach(this::removeComponent);
    }
    
    public <T extends Component> T getComponent(Class<T> type) {
        for (Component component : components) {
            if (type.isAssignableFrom(component.getClass())) {
                return type.cast(component);
            }
        }
        return null;
    }
    
    public <T extends Component> List<T> getComponents(Class<T> type) {
        List<T> comps = new ArrayList<>();
        components.forEach(e -> {
            if (type.isAssignableFrom(e.getClass())) {
                comps.add(type.cast(e));
            }
        });
        return comps;
    }
    
    void onAdd() {
    }
    
    void onRemove() {
        components.forEach(components::remove);
        components.update();
    }
    
    protected void onUpdate(float deltaTime) {
        components.update();
        components.forEach((e) -> e.onUpdate(deltaTime));
    }
    
    protected void onRender(float deltaTime) {
        components.forEach((e) -> e.onRender(deltaTime));
    }
    
}
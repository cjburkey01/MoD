package com.cjburkey.mod.component;

import com.cjburkey.mod.entity.Component;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector2f;

public final class Transform extends Component {
    
    public final Vector2f position = new Vector2f(0.0f, 0.0f);
    public final Quaternionf rotation = new Quaternionf().identity();
    public final Vector2f scale = new Vector2f(1.0f, 1.0f);
    private Transform parent = null;
    private Matrix4f modelMatrix = new Matrix4f().identity();
    
    // TODO: ALLOW PARENTING TRANSFORMS
    public void setParent(Transform parent) {
        this.parent = parent;
    }
    
    public Transform getTransform() {
        return parent;
    }
    
    public Matrix4f getModelMatrix() {
        return modelMatrix.identity().translate(position.x, position.y, 0.0f).scale(scale.x, scale.y, 0.0f).rotate(rotation);
    }
    
}
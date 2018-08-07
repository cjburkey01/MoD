package com.cjburkey.mod.component;

import com.cjburkey.mod.core.MoD;
import com.cjburkey.mod.core.ShaderProgram;
import com.cjburkey.mod.entity.Component;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector2i;

public final class Camera extends Component {
    
    private static Camera camera;
    
    public Camera() {
        if (camera == null) {
            makeMain();
        }
    }
    
    private boolean needUpdate = true;
    private float size = 15.0f;
    private final Vector2i windowSize = new Vector2i(0, 0);
    private final Matrix4f projectionMatrix = new Matrix4f().identity();
    private final Matrix4f viewMatrix = new Matrix4f().identity();
    
    public void setSize(float size) {
        this.size = size;
        needUpdate = true;
    }
    
    public float getSize() {
        return size;
    }
    
    private boolean checkUpdate() {
        if (!windowSize.equals(MoD.instance.getWindowSize()) || needUpdate) {
            needUpdate = false;
            windowSize.set(MoD.instance.getWindowSize());
            
            float aspect = ((float) windowSize.x / windowSize.y) * size;
            projectionMatrix.identity().ortho(-aspect, aspect, -size, size, 0.9f, 1.1f);
            viewMatrix.identity().translate(parent.transform.position.x, parent.transform.position.y, -1.0f).rotate(parent.transform.rotation.invert(new Quaternionf()));
            return true;
        }
        return false;
    }
    
    public Matrix4f getProjectionMatrix() {
        checkUpdate();
        return new Matrix4f(projectionMatrix);
    }
    
    public void getProjectionMatrix(String name, ShaderProgram shaderProgram) {
        shaderProgram.setUniform(name, getProjectionMatrix());
    }
    
    public Matrix4f getViewMatrix() {
        checkUpdate();
        return new Matrix4f(viewMatrix);
    }
    
    public void getViewMatrix(String name, ShaderProgram shaderProgram) {
        shaderProgram.setUniform(name, getViewMatrix());
    }
    
    public void makeMain() {
        camera = this;
    }
    
    public static Camera getMain() {
        return camera;
    }
    
}

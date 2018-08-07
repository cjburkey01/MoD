package com.cjburkey.mod.core;

import com.cjburkey.mod.component.Camera;
import com.cjburkey.mod.component.Transform;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL20.*;

public final class ShaderProgram {
    
    private int shaderProgram;
    private final Map<Integer, Integer> shaders = new HashMap<>();
    private final Map<String, Integer> uniforms = new HashMap<>();
    
    public ShaderProgram() {
        shaderProgram = glCreateProgram();
    }
    
    public boolean addShader(int type, String source) {
        if (shaders.containsKey(type)) {
            Debug.warn("The shader program already contains a shader of type {}; it will be overridden", type);
        }
        int shader = glCreateShader(type);
        glShaderSource(shader, source);
        glCompileShader(shader);
        String log = glGetShaderInfoLog(shader);
        if (!(log = log.trim()).isEmpty()) {
            Debug.error("Failed to compile shader of type {}: {}", type, log);
            return false;
        }
        shaders.put(type, shader);
        glAttachShader(shaderProgram, shader);
        return true;
    }
    
    public boolean link() {
        glLinkProgram(shaderProgram);
        String log = glGetProgramInfoLog(shaderProgram);
        if (!(log = log.trim()).isEmpty()) {
            Debug.error("Failed to link shader program: {}", log);
            return false;
        }
        
        glValidateProgram(shaderProgram);
        log = glGetProgramInfoLog(shaderProgram);
        if (!(log = log.trim()).isEmpty()) {
            Debug.warn("Failed to validate shader program: {}", log);
        }
        
        for (Integer shader : shaders.values()) {
            glDetachShader(shaderProgram, shader);
            glDeleteShader(shader);
        }
        return true;
    }
    
    public void cleanup() {
        unbindPrograms();
        glDeleteProgram(shaderProgram);
    }
    
    public void bindProgram() {
        glUseProgram(shaderProgram);
    }
    
    private int getOrRegisterUniform(String name) {
        if (uniforms.containsKey(name)) {
            return uniforms.get(name);
        }
        int uniform = glGetUniformLocation(shaderProgram, name);
        if (uniform >= 0) {
            uniforms.put(name, uniform);
        }
        return uniform;
    }
    
    private void setUniform(String name, UniformCallback ifFound) {
        int at = getOrRegisterUniform(name);
        if (at >= 0) {
            ifFound.success(at);
            return;
        }
        Debug.error("Failed to locate uniform \"{}\"", name);
    }
    
    public void setUniform(String name, int value) {
        setUniform(name, (loc) -> glUniform1i(loc, value));
    }
    
    public void setUniform(String name, float value) {
        setUniform(name, (loc) -> glUniform1f(loc, value));
    }
    
    public void setUniform(String name, Vector2f value) {
        setUniform(name, (loc) -> glUniform2f(loc, value.x, value.y));
    }
    
    public void setUniform(String name, Vector3f value) {
        setUniform(name, (loc) -> glUniform3f(loc, value.x, value.y, value.z));
    }
    
    public void setUniform(String name, Vector4f value) {
        setUniform(name, (loc) -> glUniform4f(loc, value.x, value.y, value.z, value.w));
    }
    
    public void setUniform(String name, Matrix3f value) {
        setUniform(name, (loc) -> glUniformMatrix3fv(loc, false, value.get(new float[9])));
    }
    
    public void setUniform(String name, Matrix4f value) {
        setUniform(name, (loc) -> glUniformMatrix4fv(loc, false, value.get(new float[16])));
    }
    
    public void setCameraUniforms(Camera camera) {
        camera.getProjectionMatrix("projectionMatrix", this);
        camera.getViewMatrix("viewMatrix", this);
    }
    
    public void setObjectUniforms(Transform transform) {
        setUniform("modelMatrix", transform.getModelMatrix());
    }
    
    public static void unbindPrograms() {
        glUseProgram(0);
    }
    
}
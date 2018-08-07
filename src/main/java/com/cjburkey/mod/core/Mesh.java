package com.cjburkey.mod.core;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL30.*;

public abstract class Mesh {
    
    private static Mesh current;
    
    private int elements;
    private int vao;
    private int vbo;
    private int ebo;
    
    public Mesh() {
        vao = glGenVertexArrays();
        
        vbo = glGenBuffers();
        ebo = glGenBuffers();
        createBuffers();
    }
    
    protected void createBuffers() {
    }
    
    protected void deleteBuffers() {
    }
    
    protected void addVertsAndInds(Vector3f[] verts, short[] inds) {
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, vecToArr(verts), GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        
        addInds(inds);
    }
    
    protected void addVertsAndInds(Vector2f[] verts, short[] inds) {
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, vecToArr(verts), GL_STATIC_DRAW);
        glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        
        addInds(inds);
    }
    
    private void addInds(short[] inds) {
        elements = inds.length;
        
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, inds, GL_STATIC_DRAW);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    }
    
    public void bind() {
        glBindVertexArray(vao);
    }
    
    protected void preRender() {
        bind();
        glEnableVertexAttribArray(0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
    }
    
    protected void renderCall() {
        glDrawElements(GL_TRIANGLES, elements, GL_UNSIGNED_SHORT, 0);
    }
    
    protected void postRender() {
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glDisableVertexAttribArray(0);
        unbind();
    }
    
    public final void render() {
        preRender();
        renderCall();
        postRender();
    }
    
    protected void unbind() {
        glBindVertexArray(0);
    }
    
    public final void cleanup() {
        glDeleteVertexArrays(vao);
        
        glDeleteBuffers(vbo);
        glDeleteBuffers(ebo);
        deleteBuffers();
    }
    
    public static void unbindMeshes() {
        current.unbind();
    }
    
    protected static float[] vecToArr(Vector2f[] verts) {
        float[] out = new float[verts.length * 2];
        for (int i = 0; i < verts.length; i ++) {
            out[i * 2] = verts[i].x;
            out[i * 2 + 1] = verts[i].y;
        }
        return out;
    }
    
    protected static float[] vecToArr(Vector3f[] verts) {
        float[] out = new float[verts.length * 3];
        for (int i = 0; i < verts.length; i ++) {
            out[i * 3] = verts[i].x;
            out[i * 3 + 1] = verts[i].y;
            out[i * 3 + 2] = verts[i].z;
        }
        return out;
    }
    
    protected static float[] vecToArr(Vector4f[] verts) {
        float[] out = new float[verts.length * 4];
        for (int i = 0; i < verts.length; i ++) {
            out[i * 4] = verts[i].x;
            out[i * 4 + 1] = verts[i].y;
            out[i * 4 + 2] = verts[i].z;
            out[i * 4 + 3] = verts[i].w;
        }
        return out;
    }
    
}
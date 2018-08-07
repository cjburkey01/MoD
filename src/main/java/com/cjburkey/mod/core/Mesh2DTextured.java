package com.cjburkey.mod.core;

import org.joml.Vector2f;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

public class Mesh2DTextured extends Mesh {
    
    public boolean cleanTexture = true;
    private Texture texture;
    private int uvbo;
    
    public Mesh2DTextured(Texture texture) {
        super();
        setTexture(texture);
    }
    
    public void setTexture(Texture texture) {
        this.texture = texture;
    }
    
    public Texture getTexture() {
        return texture;
    }
      
    protected void createBuffers() {
        super.createBuffers();
    
        uvbo = glGenBuffers();
    }
    
    protected void deleteBuffers() {
        super.deleteBuffers();
        
        glDeleteBuffers(uvbo);
        
        if (cleanTexture && texture != null) {
            texture.cleanup();
        }
    }
    
    public void register(Vector2f[] verts, short[] inds, Vector2f[] uvs) {
        bind();
        
        addVertsAndInds(verts, inds);
        
        glBindBuffer(GL_ARRAY_BUFFER, uvbo);
        glBufferData(GL_ARRAY_BUFFER, vecToArr(uvs), GL_STATIC_DRAW);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        
        unbind();
    }
    
    protected void preRender() {
        super.preRender();
        
        glEnableVertexAttribArray(1);
        
        if (texture != null) {
            texture.bind();
        }
    }
    
    protected void postRender() {
        Texture.unbind();
        
        glDisableVertexAttribArray(1);
        
        super.postRender();
    }
    
}
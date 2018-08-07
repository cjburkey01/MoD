package com.cjburkey.mod.core;

import org.joml.Vector2f;

public class Mesh2D extends Mesh {
    
    public Mesh2D() {
        super();
    }
    
    public void register(Vector2f[] verts, short[] inds) {
        bind();
        addVertsAndInds(verts, inds);
        unbind();
    }
    
}
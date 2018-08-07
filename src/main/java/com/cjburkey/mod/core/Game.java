package com.cjburkey.mod.core;

import com.cjburkey.mod.component.MeshRenderer;
import com.cjburkey.mod.entity.Entity;
import org.joml.Vector2f;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;

final class Game {
    
    private static ShaderProgram program;
    private static Texture texture;
    private static Mesh2DTextured mesh;
    
    static void init() {
        program = new ShaderProgram();
        program.addShader(GL20.GL_VERTEX_SHADER, IO.readFileText("/assets/shaders/basic.vsh"));
        program.addShader(GL20.GL_FRAGMENT_SHADER, IO.readFileText("/assets/shaders/basic.fsh"));
        program.link();
        program.bindProgram();
        
        texture = Texture.createTexture("/assets/textures/test.png");
        
        mesh = new Mesh2DTextured(texture);
        mesh.register(new Vector2f[] {
                new Vector2f(-0.5f, 0.5f),
                new Vector2f(0.5f, 0.5f),
                new Vector2f(0.5f, -0.5f),
                new Vector2f(-0.5f, -0.5f),
        }, new short[] {
                0, 1, 2,
                0, 2, 3,
        }, new Vector2f[] {
                new Vector2f(0.0f, 0.0f),
                new Vector2f(1.0f, 0.0f),
                new Vector2f(1.0f, 1.0f),
                new Vector2f(0.0f, 1.0f),
        });
        
        Entity testEntity = MoD.instance.getWorld().createEntity();
        MeshRenderer meshRenderer = testEntity.addComponent(MeshRenderer.class);
        if (meshRenderer == null) {
            Debug.error("Failed to create mesh renderer");
            return;
        }
        meshRenderer.cleanMesh = true;
        meshRenderer.setMesh(mesh);
    }
    
    static void exit() {
        program.cleanup();
    }
    
}
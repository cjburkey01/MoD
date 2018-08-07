package com.cjburkey.mod.core;

import com.cjburkey.mod.component.Camera;
import com.cjburkey.mod.component.MeshRenderer;
import com.cjburkey.mod.entity.Entity;
import org.joml.Vector2f;
import org.lwjgl.opengl.GL20;

final class Game {
    
    private static ShaderProgram program;
    private static Texture texture;
    private static MeshRenderer meshRenderer;
    private static Mesh2DTextured mesh;
    
    static void init() {
        MoD.instance.getWorld().createEntity().addComponent(Camera.class).makeMain();
        Camera.getMain().setSize(2.0f);
        
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
        meshRenderer = testEntity.addComponent(MeshRenderer.class);
        if (meshRenderer == null) {
            Debug.error("Failed to create mesh renderer");
            return;
        }
        meshRenderer.setShader(program);
        meshRenderer.cleanMesh = true;
        meshRenderer.setMesh(mesh);
        meshRenderer.parent.transform.position.x = -3.0f;
    }
    
    static void update() {
        meshRenderer.parent.transform.position.x += MoD.instance.getDeltaTime();
        if (meshRenderer.parent.transform.position.x >= 3.0f) {
            meshRenderer.parent.transform.position.x = -3.0f;
            meshRenderer.parent.transform.scale.y *= 0.5f;
        }
        meshRenderer.parent.transform.scale.y *= 1.005f;
        program.setCameraUniforms(Camera.getMain());
    }
    
    static void render() {
    }
    
    static void exit() {
        program.cleanup();
    }
    
    public static ShaderProgram getProgram() {
        return program;
    }
    
}
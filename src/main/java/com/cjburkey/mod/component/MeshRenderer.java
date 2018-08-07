package com.cjburkey.mod.component;

import com.cjburkey.mod.core.Mesh;
import com.cjburkey.mod.core.ShaderProgram;
import com.cjburkey.mod.entity.Component;

public class MeshRenderer extends Component {
    
    public boolean cleanMesh = true;
    private Mesh mesh;
    private ShaderProgram shader;
    
    public void setShader(ShaderProgram shader) {
        this.shader = shader;
    }
    
    public ShaderProgram getShader() {
        return shader;
    }
    
    protected void onRender(float deltaTime) {
        if (shader != null) {
            shader.setObjectUniforms(parent.transform);
        }
        if (mesh != null) {
            mesh.render();
        }
    }
    
    protected void onRemove() {
        if (cleanMesh) {
            mesh.cleanup();
        }
    }
    
    public void setMesh(Mesh mesh) {
        this.mesh = mesh;
    }
    
    public Mesh getMesh() {
        return mesh;
    }
    
}
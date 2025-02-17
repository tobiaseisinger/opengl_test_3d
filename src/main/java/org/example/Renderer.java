package org.example;

import org.example.Shader.ShaderUtil;
import org.example.mesh.Mesh;
import org.example.mesh.MeshData;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;
import java.util.List;

public class Renderer {
    private ShaderUtil shader;
    private TextureLoader textureLoader;

    public Renderer(ShaderUtil shader, TextureLoader textureLoader) {
        this.shader = shader;
        this.textureLoader = textureLoader;
    }

    public void prepare() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        shader.use();
        textureLoader.bind();
    }

    public void submit(Mesh mesh, Matrix4f modelMatrix, Matrix4f viewMatrix, Matrix4f projectionMatrix) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer buffer = stack.mallocFloat(16);

            GL20.glUniformMatrix4fv(shader.getModelLocation(), false, modelMatrix.get(buffer));
            GL20.glUniformMatrix4fv(shader.getViewLocation(), false, viewMatrix.get(buffer));
            GL20.glUniformMatrix4fv(shader.getProjectionLocation(), false, projectionMatrix.get(buffer));

            mesh.draw();
        }
    }

    public void submitAll(List<MeshData> meshes, Matrix4f viewMatrix, Matrix4f projectionMatrix) {
        for (MeshData meshData : meshes) {
            submit(meshData.mesh, meshData.modelMatrix, viewMatrix, projectionMatrix);
        }
    }

    public void cleanup() {
        shader.delete();
        textureLoader.cleanup();
    }
}
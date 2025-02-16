package org.example.mesh;

import org.joml.Matrix4f;

public class MeshData {
    public Mesh mesh;
    public Matrix4f modelMatrix;

    public MeshData(Mesh mesh, Matrix4f modelMatrix) {
        this.mesh = mesh;
        this.modelMatrix = modelMatrix;
    }
}
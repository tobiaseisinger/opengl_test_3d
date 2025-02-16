package org.example.blocks;

import org.example.mesh.Mesh;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Block {
    private Mesh mesh;
    private Matrix4f modelMatrix;

    public Block(float size, Vector3f position) {
        float halfSize = size / 2.0f;

        float[] vertices = {
                // Position         // Farbe
                -halfSize, -halfSize, -halfSize,  1.0f, 0.0f, 0.0f, // Rot
                halfSize, -halfSize, -halfSize,  0.0f, 1.0f, 0.0f, // Grün
                halfSize,  halfSize, -halfSize,  0.0f, 0.0f, 1.0f, // Blau
                -halfSize,  halfSize, -halfSize,  1.0f, 1.0f, 0.0f, // Gelb
                -halfSize, -halfSize,  halfSize,  1.0f, 0.0f, 1.0f, // Magenta
                halfSize, -halfSize,  halfSize,  0.0f, 1.0f, 1.0f, // Cyan
                halfSize,  halfSize,  halfSize,  1.0f, 1.0f, 1.0f, // Weiß
                -halfSize,  halfSize,  halfSize,  0.0f, 0.0f, 0.0f  // Schwarz
        };

        int[] indices = {
                0, 1, 2, 2, 3, 0,  // Vorderseite
                1, 5, 6, 6, 2, 1,  // Rechte Seite
                5, 4, 7, 7, 6, 5,  // Rückseite
                4, 0, 3, 3, 7, 4,  // Linke Seite
                3, 2, 6, 6, 7, 3,  // Oben
                4, 5, 1, 1, 0, 4   // Unten
        };

        this.mesh = new Mesh(vertices, indices);

        this.modelMatrix = new Matrix4f().translate(position);
    }

    public Mesh getMesh() {
        return mesh;
    }

    public Matrix4f getModelMatrix() {
        return modelMatrix;
    }
}
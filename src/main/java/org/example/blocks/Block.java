package org.example.blocks;

import org.example.TextureLoader;
import org.example.mesh.Mesh;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Block {
    private Mesh mesh;
    private Matrix4f modelMatrix;

    public Block(float size, Vector3f position, TextureLoader textureLoader,
                 String topTexture, String bottomTexture, String leftTexture,
                 String rightTexture, String frontTexture, String backTexture) {
        float halfSize = size / 2.0f;

        float[] texTop = textureLoader.getTextureCoords(topTexture);
        float[] texBottom = textureLoader.getTextureCoords(bottomTexture);
        float[] texLeft = textureLoader.getTextureCoords(leftTexture);
        float[] texRight = textureLoader.getTextureCoords(rightTexture);
        float[] texFront = textureLoader.getTextureCoords(frontTexture);
        float[] texBack = textureLoader.getTextureCoords(backTexture);

        float[] vertices = {
                // Vorderseite
                -halfSize, -halfSize, -halfSize,  texFront[0], texFront[3],
                halfSize, -halfSize, -halfSize,  texFront[2], texFront[3],
                halfSize,  halfSize, -halfSize,  texFront[2], texFront[1],
                -halfSize,  halfSize, -halfSize,  texFront[0], texFront[1],

                // Rückseite
                -halfSize, -halfSize,  halfSize,  texBack[0], texBack[3],
                halfSize, -halfSize,  halfSize,  texBack[2], texBack[3],
                halfSize,  halfSize,  halfSize,  texBack[2], texBack[1],
                -halfSize,  halfSize,  halfSize,  texBack[0], texBack[1],

                // Linke Seite
                -halfSize, -halfSize, -halfSize,  texLeft[0], texLeft[3],
                -halfSize, -halfSize,  halfSize,  texLeft[2], texLeft[3],
                -halfSize,  halfSize,  halfSize,  texLeft[2], texLeft[1],
                -halfSize,  halfSize, -halfSize,  texLeft[0], texLeft[1],

                // Rechte Seite
                halfSize, -halfSize, -halfSize,  texRight[0], texRight[3],
                halfSize, -halfSize,  halfSize,  texRight[2], texRight[3],
                halfSize,  halfSize,  halfSize,  texRight[2], texRight[1],
                halfSize,  halfSize, -halfSize,  texRight[0], texRight[1],

                // Oben
                -halfSize,  halfSize, -halfSize,  texTop[0], texTop[3],
                halfSize,  halfSize, -halfSize,  texTop[2], texTop[3],
                halfSize,  halfSize,  halfSize,  texTop[2], texTop[1],
                -halfSize,  halfSize,  halfSize,  texTop[0], texTop[1],

                // Unten
                -halfSize, -halfSize, -halfSize,  texBottom[0], texBottom[3],
                halfSize, -halfSize, -halfSize,  texBottom[2], texBottom[3],
                halfSize, -halfSize,  halfSize,  texBottom[2], texBottom[1],
                -halfSize, -halfSize,  halfSize,  texBottom[0], texBottom[1]
        };

        int[] indices = {
                0, 1, 2,  2, 3, 0,   // Vorderseite
                4, 5, 6,  6, 7, 4,   // Rückseite
                8, 9, 10, 10, 11, 8, // Links
                12, 13, 14, 14, 15, 12, // Rechts
                16, 17, 18, 18, 19, 16, // Oben
                20, 21, 22, 22, 23, 20  // Unten
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

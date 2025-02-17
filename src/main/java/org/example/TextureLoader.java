package org.example;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.stb.STBImage;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;

public class TextureLoader {
    private int textureID;
    private int atlasWidth, atlasHeight;
    private int tileSize;
    private Map<String, float[]> textureCoords;

    public TextureLoader(String filePath, int tileSize) {
        this.tileSize = tileSize;
        this.textureCoords = new HashMap<>();

        int[] width = new int[1], height = new int[1];
        ByteBuffer imageData = loadTexture(filePath, width, height);
        this.atlasWidth = width[0];
        this.atlasHeight = height[0];

        textureID = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, atlasWidth, atlasHeight, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, imageData);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
    }

    private ByteBuffer loadTexture(String filePath, int[] width, int[] height) {
        IntBuffer w = BufferUtils.createIntBuffer(1);
        IntBuffer h = BufferUtils.createIntBuffer(1);
        IntBuffer channels = BufferUtils.createIntBuffer(1);

        ByteBuffer image = STBImage.stbi_load(filePath, w, h, channels, 4);
        if (image == null) {
            throw new RuntimeException("Failed to load texture: " + filePath);
        }

        width[0] = w.get(0);
        height[0] = h.get(0);

        return image;
    }

    public void registerTexture(String name, int x, int y) {
        float u = (float) (x * tileSize) / atlasWidth;
        float v = (float) (y * tileSize) / atlasHeight;
        float tileU = (float) tileSize / atlasWidth;
        float tileV = (float) tileSize / atlasHeight;

        textureCoords.put(name, new float[]{u, v, u + tileU, v + tileV});
    }

    public float[] getTextureCoords(String name) {
        return textureCoords.get(name);
    }

    public void bind() {
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
    }

    public void cleanup() {
        GL11.glDeleteTextures(textureID);
    }
}

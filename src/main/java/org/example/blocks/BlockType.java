package org.example.blocks;

import org.example.TextureLoader;

public enum BlockType {
    GRASS("grass"),
    DIRT("dirt"),
    STONE("stone"),
    WOOD("oak_log"),
    SAND("sand");;

    private final String textureName;
    private float[] textureCoords;

    BlockType(String textureName) {
        this.textureName = textureName;
    }

    public void setTextureCoords(TextureLoader atlas) {
        this.textureCoords = atlas.getTextureCoords(textureName);
    }

    public float[] getTextureCoords() {
        return textureCoords;
    }
}

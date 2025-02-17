package org.example.blocks;

import org.example.TextureLoader;
import org.joml.Vector3f;

public class GrassBlock extends Block {
    public GrassBlock(float size, Vector3f position, TextureLoader textureLoader) {
        super(size, position, textureLoader,
                "grass_top",  // Oben
                "dirt",       // Unten
                "grass_side", // Links
                "grass_side", // Rechts
                "grass_side", // Vorne
                "grass_side"  // Hinten
        );
    }
}

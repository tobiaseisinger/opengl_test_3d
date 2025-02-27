package org.example;

import org.example.Shader.ShaderUtil;
import org.example.blocks.Block;
import org.example.blocks.BlockType;
import org.example.blocks.GrassBlock;
import org.example.mesh.MeshData;
import org.example.utils.Utils;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.*;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

public class Application {
    private Window window;
    private ShaderUtil shader;
    private Camera camera;
    private Renderer renderer;
    private List<MeshData> blocks;

    public void run() {
        init();
        loop();
        cleanup();
    }

    private void init() {
        window = new Window("Test 3D", 800, 600);
        shader = new ShaderUtil("src/main/java/org/example/Shader/vertex_shader.glsl", "src/main/java/org/example/Shader/fragment_shader.glsl");
        TextureLoader textureLoader = new TextureLoader("src/main/resources/atlas.png", 16);

        camera = new Camera(new Vector3f(0, 0, 5), new Vector3f(0, 0, -1), new Vector3f(0, 1, 0), -90, 0, 2.5f);
        camera.setCursorPosCallback(window.getWindowHandle());

        renderer = new Renderer(shader, textureLoader);

        blocks = new ArrayList<>();

        textureLoader.registerTexture("grass_top", 0, 0);
        textureLoader.registerTexture("dirt", 2, 0);
        textureLoader.registerTexture("grass_side", 3, 0);

        for (BlockType type : BlockType.values()) {
            type.setTextureCoords(textureLoader);
        }

        addBlocks(textureLoader);


        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }

    private void addBlocks(TextureLoader textureLoader) {
        GrassBlock grassBlock = new GrassBlock(1.0f, new Vector3f(0, 0, 0), textureLoader);
        blocks.add(new MeshData(grassBlock.getMesh(), grassBlock.getModelMatrix()));
    }

    private void loop() {
        while (!window.shouldClose()) {
            float currentFrame = (float) GLFW.glfwGetTime();
            float deltaTime = currentFrame - lastFrame;
            lastFrame = currentFrame;

            renderer.prepare();

            camera.processInput(window.getWindowHandle(), deltaTime);

            Matrix4f viewMatrix = new Matrix4f().lookAt(
                    new Vector3f(camera.getPosition()),
                    new Vector3f(camera.getPosition()).add(camera.getFront()),
                    camera.getUp()
            );

            Matrix4f projectionMatrix = new Matrix4f().perspective(
                    (float) Math.toRadians(45),
                    800.0f / 600.0f,
                    0.1f, // Near-Clipping-Ebene
                    100.0f // Far-Clipping-Ebene
            );

            renderer.submitAll(blocks, viewMatrix, projectionMatrix);

            window.swapBuffers();
            window.pollEvents();
        }
    }

    private void cleanup() {
        renderer.cleanup();
        window.destroy();
    }

    private float lastFrame = 0.0f;

}

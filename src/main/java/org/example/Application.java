package org.example;

import org.example.utils.Utils;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.*;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;

public class Application {
    private long window;
    private int vao, vbo;
    private int modelLoc, viewLoc, projLoc;

    private Vector3f cameraPos = new Vector3f(0.0f, 0.0f, 3.0f);
    private Vector3f cameraFront = new Vector3f(0.0f, 0.0f, -1.0f);
    private Vector3f cameraUp = new Vector3f(0.0f, 1.0f, 0.0f);
    private float yaw = -90.0f;
    private float pitch = 0.0f;
    private float lastX = 400, lastY = 300;
    private boolean firstMouse = true;
    private float deltaTime = 0.0f, lastFrame = 0.0f;

    public void run() {
        init();
        loop();
        GLFW.glfwDestroyWindow(window);
        GLFW.glfwTerminate();
    }

    private void loop() {
        int shaderProgram = loadShader("src/main/java/org/example/Shader/vertex_shader.glsl", "src/main/java/org/example/Shader/fragment_shader.glsl");
        GL20.glUseProgram(shaderProgram);

        while (!GLFW.glfwWindowShouldClose(window)) {
            float currentFrame = (float) GLFW.glfwGetTime();
            deltaTime = currentFrame - lastFrame;
            lastFrame = currentFrame;

            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

            updateMatrices();


            GL30.glBindVertexArray(vao);
            GL11.glDrawElements(GL11.GL_TRIANGLES, 36, GL11.GL_UNSIGNED_INT, 0);

            GLFW.glfwSwapBuffers(window);
            GLFW.glfwPollEvents();
        }

        GL20.glDeleteProgram(shaderProgram);
    }

    private void init() {
        if (!GLFW.glfwInit()) {
            throw new RuntimeException("Failed to initialize GLFW");
        }

        window = GLFW.glfwCreateWindow(800, 600, "Test 3D", 0, 0);
        if (window == 0) {
            throw new RuntimeException("Failed to create window");
        }

        GLFWVidMode vidMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
        GLFW.glfwSetWindowPos(window, (vidMode.width() - 800) / 2, (vidMode.height() - 600) / 2);
        GLFW.glfwMakeContextCurrent(window);
        GL.createCapabilities();
        setupShaders();
        setupBuffers();
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        if (!GL.getCapabilities().OpenGL33) {
            throw new RuntimeException("OpenGL 3.3 wird nicht unterstützt!");
        }
    }

    private void setupBuffers() {
        float[] vertices = {
                // Position         // Farbe
                -0.5f, -0.5f, -0.5f,  1.0f, 0.0f, 0.0f, // Rot
                0.5f, -0.5f, -0.5f,  0.0f, 1.0f, 0.0f, // Grün
                0.5f,  0.5f, -0.5f,  0.0f, 0.0f, 1.0f, // Blau
                -0.5f,  0.5f, -0.5f,  1.0f, 1.0f, 0.0f, // Gelb
                -0.5f, -0.5f,  0.5f,  1.0f, 0.0f, 1.0f, // Magenta
                0.5f, -0.5f,  0.5f,  0.0f, 1.0f, 1.0f, // Cyan
                0.5f,  0.5f,  0.5f,  1.0f, 1.0f, 1.0f, // Weiß
                -0.5f,  0.5f,  0.5f,  0.0f, 0.0f, 0.0f  // Schwarz
        };

        int[] indices = {
                0, 1, 2, 2, 3, 0,  // Vorderseite
                1, 5, 6, 6, 2, 1,  // Rechte Seite
                5, 4, 7, 7, 6, 5,  // Rückseite
                4, 0, 3, 3, 7, 4,  // Linke Seite
                3, 2, 6, 6, 7, 3,  // Oben
                4, 5, 1, 1, 0, 4   // Unten
        };

        vao = GL30.glGenVertexArrays();
        vbo = GL15.glGenBuffers();
        int ebo = GL15.glGenBuffers();

        GL30.glBindVertexArray(vao);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertices, GL15.GL_STATIC_DRAW);

        GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 6 * Float.BYTES, 0);
        GL20.glEnableVertexAttribArray(0);

        GL20.glVertexAttribPointer(1, 3, GL11.GL_FLOAT, false, 6 * Float.BYTES, 3 * Float.BYTES);
        GL20.glEnableVertexAttribArray(1);

        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ebo);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indices, GL15.GL_STATIC_DRAW);

        GL30.glBindVertexArray(0);
    }

    private int loadShader(String vertexPath, String fragmentPath) {
        int vertexShader = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
        GL20.glShaderSource(vertexShader, Utils.loadFileAsString(vertexPath));
        GL20.glCompileShader(vertexShader);

        int fragmentShader = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
        GL20.glShaderSource(fragmentShader, Utils.loadFileAsString(fragmentPath));
        GL20.glCompileShader(fragmentShader);

        int shaderProgram = GL20.glCreateProgram();
        GL20.glAttachShader(shaderProgram, vertexShader);
        GL20.glAttachShader(shaderProgram, fragmentShader);
        GL20.glLinkProgram(shaderProgram);

        GL20.glDeleteShader(vertexShader);
        GL20.glDeleteShader(fragmentShader);

        return shaderProgram;
    }

    private void setupShaders() {
        int shaderProgram = loadShader("src/main/java/org/example/Shader/vertex_shader.glsl", "src/main/java/org/example/Shader/fragment_shader.glsl");
        GL20.glUseProgram(shaderProgram);

        modelLoc = GL20.glGetUniformLocation(shaderProgram, "model");
        viewLoc = GL20.glGetUniformLocation(shaderProgram, "view");
        projLoc = GL20.glGetUniformLocation(shaderProgram, "projection");

        updateMatrices();
    }

    private void updateMatrices() {
        float cameraSpeed = 2.5f * deltaTime;
        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_W) == GLFW.GLFW_PRESS)
            cameraPos.add(new Vector3f(cameraFront).mul(cameraSpeed));
        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_S) == GLFW.GLFW_PRESS)
            cameraPos.sub(new Vector3f(cameraFront).mul(cameraSpeed));
        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_A) == GLFW.GLFW_PRESS)
            cameraPos.sub(new Vector3f(cameraFront).cross(cameraUp, new Vector3f()).normalize().mul(cameraSpeed));
        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_D) == GLFW.GLFW_PRESS)
            cameraPos.add(new Vector3f(cameraFront).cross(cameraUp, new Vector3f()).normalize().mul(cameraSpeed));

        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer buffer = stack.mallocFloat(16);

            Matrix4f model = new Matrix4f().rotate((float) Math.toRadians(45), 0, 1, 0);
            GL20.glUniformMatrix4fv(modelLoc, false, model.get(buffer));

            Matrix4f view = new Matrix4f().lookAt(cameraPos, cameraPos.add(cameraFront, new Vector3f()), cameraUp);
            GL20.glUniformMatrix4fv(viewLoc, false, view.get(buffer));

            Matrix4f projection = new Matrix4f().perspective(
                    (float) Math.toRadians(45), // FOV
                    800.0f / 600.0f,            // Seitenverhältnis
                    0.1f, 100.0f                // Near und Far Clipping
            );
            GL20.glUniformMatrix4fv(projLoc, false, projection.get(buffer));
        }

        GLFW.glfwSetCursorPosCallback(window, (win, xpos, ypos) -> {
            if (firstMouse) {
                lastX = (float) xpos;
                lastY = (float) ypos;
                firstMouse = false;
            }

            float xoffset = (float) xpos - lastX;
            float yoffset = lastY - (float) ypos; // Umgekehrte Y-Richtung
            lastX = (float) xpos;
            lastY = (float) ypos;

            float sensitivity = 0.1f;
            xoffset *= sensitivity;
            yoffset *= sensitivity;

            yaw += xoffset;
            pitch += yoffset;

            if (pitch > 89.0f) pitch = 89.0f;
            if (pitch < -89.0f) pitch = -89.0f;

            Vector3f direction = new Vector3f();
            direction.x = (float) Math.cos(Math.toRadians(yaw)) * (float) Math.cos(Math.toRadians(pitch));
            direction.y = (float) Math.sin(Math.toRadians(pitch));
            direction.z = (float) Math.sin(Math.toRadians(yaw)) * (float) Math.cos(Math.toRadians(pitch));
            cameraFront.set(direction.normalize());
        });
    }

}

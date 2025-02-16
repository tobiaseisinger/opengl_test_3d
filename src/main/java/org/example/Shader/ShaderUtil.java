package org.example.Shader;

import org.lwjgl.opengl.GL20;
import org.example.utils.Utils;

public class ShaderUtil {
    private int programId;
    private int modelLoc, viewLoc, projLoc;

    public ShaderUtil(String vertexPath, String fragmentPath) {
        programId = loadProgram(vertexPath, fragmentPath);
        modelLoc = GL20.glGetUniformLocation(programId, "model");
        viewLoc = GL20.glGetUniformLocation(programId, "view");
        projLoc = GL20.glGetUniformLocation(programId, "projection");
    }

    private int loadProgram(String vertexPath, String fragmentPath) {
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

    public void use() {
        GL20.glUseProgram(programId);
    }

    public int getModelLocation() {
        return modelLoc;
    }

    public int getViewLocation() {
        return viewLoc;
    }

    public int getProjectionLocation() {
        return projLoc;
    }

    public void delete() {
        GL20.glDeleteProgram(programId);
    }

}

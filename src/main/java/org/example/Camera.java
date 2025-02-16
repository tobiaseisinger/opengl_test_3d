package org.example;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

public class Camera {
    private Vector3f position;
    private Vector3f front;
    private Vector3f up;
    private float yaw, pitch;
    private float lastX, lastY;
    private boolean firstMouse;
    private float speed;

    public Camera(Vector3f position, Vector3f front, Vector3f up, float yaw, float pitch, float speed) {
        this.position = position;
        this.front = front;
        this.up = up;
        this.yaw = yaw;
        this.pitch = pitch;
        this.speed = speed;
        this.firstMouse = true;
    }

    public void processInput(long window, float deltaTime) {
        Vector3f movement = new Vector3f();
        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_W) == GLFW.GLFW_PRESS)
            movement.add(new Vector3f(front).mul(speed * deltaTime));
        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_S) == GLFW.GLFW_PRESS)
            movement.sub(new Vector3f(front).mul(speed * deltaTime));
        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_A) == GLFW.GLFW_PRESS)
            movement.sub(new Vector3f(front).cross(up, new Vector3f()).normalize().mul(speed * deltaTime));
        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_D) == GLFW.GLFW_PRESS)
            movement.add(new Vector3f(front).cross(up, new Vector3f()).normalize().mul(speed * deltaTime));
        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_SPACE) == GLFW.GLFW_PRESS)
            movement.add(new Vector3f(up).mul(speed * deltaTime));
        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_LEFT_SHIFT) == GLFW.GLFW_PRESS)
            movement.sub(new Vector3f(up).mul(speed * deltaTime));

        position.add(movement);
    }


    public void setCursorPosCallback(long window) {
        GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
        GLFW.glfwSetCursorPosCallback(window, (win, xpos, ypos) -> {
            if (firstMouse) {
                lastX = (float) xpos;
                lastY = (float) ypos;
                firstMouse = false;
            }
            float xoffset = (float) xpos - lastX;
            float yoffset = lastY - (float) ypos;
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
            front.set(direction.normalize());
        });
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector3f getFront() {
        return front;
    }

    public Vector3f getUp() {
        return up;
    }
}
package me.rochblondiaux.minecraft.input;

import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

import lombok.Data;
import me.rochblondiaux.minecraft.game.Window;

@Data
public class MouseInput {

    private final Vector2f currentPosition = new Vector2f();
    private final Vector2f displayVector = new Vector2f();
    private final Vector2f previousPosition = new Vector2f(-1, -1);
    private boolean inWindow = false;
    private boolean leftButtonPressed = false;
    private boolean rightButtonPressed = false;

    public MouseInput(Window window) {
        GLFW.glfwSetCursorPosCallback(window.getWindowHandle(), (windowHandle, xpos, ypos) -> {
            currentPosition.x = (float) xpos;
            currentPosition.y = (float) ypos;
        });
        GLFW.glfwSetCursorEnterCallback(window.getWindowHandle(), (handle, entered) -> inWindow = entered);
        GLFW.glfwSetMouseButtonCallback(window.getWindowHandle(), (handle, button, action, mode) -> {
            leftButtonPressed = button == GLFW.GLFW_MOUSE_BUTTON_1 && action == GLFW.GLFW_PRESS;
            rightButtonPressed = button == GLFW.GLFW_MOUSE_BUTTON_2 && action == GLFW.GLFW_PRESS;
        });
    }

    public void input() {
        this.displayVector.set(0, 0);

        if (this.previousPosition.x > 0
            && this.previousPosition.y > 0
            && this.inWindow) {
            float deltaX = this.currentPosition.x - this.previousPosition.x;
            float deltaY = this.currentPosition.y - this.previousPosition.y;
            boolean rotateX = deltaX != 0;
            boolean rotateY = deltaY != 0;
            if (rotateX)
                this.displayVector.y = deltaX;
            if (rotateY)
                this.displayVector.x = deltaY;
        }

        this.previousPosition.set(currentPosition);
    }
}

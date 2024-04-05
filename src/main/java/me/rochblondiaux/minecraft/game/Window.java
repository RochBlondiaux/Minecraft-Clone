package me.rochblondiaux.minecraft.game;

import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.system.MemoryUtil;

import lombok.Builder;
import lombok.Data;
import me.rochblondiaux.minecraft.game.model.Cleanable;
import me.rochblondiaux.minecraft.game.model.Updatable;
import me.rochblondiaux.minecraft.input.MouseInput;

@Data
public class Window implements Cleanable, Updatable {

    private final long windowHandle;
    private final MouseInput mouseInput;
    private final Runnable resizeCallback;
    private int height;
    private int width;

    public Window(WindowOptions options, Runnable resizeCallback) {
        this.height = options.height;
        this.resizeCallback = resizeCallback;
        this.width = options.width;
        this.windowHandle = createWindow(options);
        this.mouseInput = new MouseInput(this);

        this.show();
    }

    private long createWindow(WindowOptions options) {
        if (!GLFW.glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        // Set window hints
        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);

        // Profile
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 2);
        if (options.compatibleProfile) {
            GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_COMPAT_PROFILE);
        } else {
            GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
            GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GLFW.GLFW_TRUE);
        }

        // Size
        if (options.width == 0 || options.height == 0)
            throw new IllegalArgumentException("Width and height must be greater than 0");

        // Create window
        long windowHandle = GLFW.glfwCreateWindow(options.width, options.height, options.title, MemoryUtil.NULL, MemoryUtil.NULL);
        if (windowHandle == 0)
            throw new IllegalStateException("Failed to create window");

        // Set window callbacks
        GLFW.glfwSetFramebufferSizeCallback(windowHandle, (window, width, height) -> onResize(width, height));
        GLFW.glfwSetErrorCallback((int errorCode, long msgPtr) -> System.err.println("GLFW Error [" + errorCode + "]: " + MemoryUtil.memUTF8(msgPtr)));


        GLFW.glfwMakeContextCurrent(windowHandle);
        GLFW.glfwSwapInterval(options.fps > 0 ? 0 : 1);

        return windowHandle;
    }

    public void show() {
        GLFW.glfwShowWindow(windowHandle);
    }

    public void hide() {
        GLFW.glfwHideWindow(windowHandle);
    }

    public void resize(int width, int height) {
        GLFW.glfwSetWindowSize(windowHandle, width, height);
    }

    public boolean isKeyPressed(int keyCode) {
        return GLFW.glfwGetKey(windowHandle, keyCode) == GLFW.GLFW_PRESS;
    }

    public void poolEvents() {
        GLFW.glfwPollEvents();
    }

    @Override
    public void update() {
        GLFW.glfwSwapBuffers(windowHandle);
    }

    public boolean shouldClose() {
        return GLFW.glfwWindowShouldClose(windowHandle);
    }

    @Override
    public void cleanup() {
        Callbacks.glfwFreeCallbacks(windowHandle);
        GLFW.glfwDestroyWindow(windowHandle);
        GLFW.glfwTerminate();
        GLFWErrorCallback errorCallback = GLFW.glfwSetErrorCallback(null);
        if (errorCallback != null)
            errorCallback.free();
    }

    private void onResize(int width, int height) {
        this.width = width;
        this.height = height;
        try {
            this.resizeCallback.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Data
    @Builder
    public static class WindowOptions {

        private String title;
        private boolean compatibleProfile;
        private int fps;
        private int height;
        private int ups;
        private int width;
    }

}

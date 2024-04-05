package me.rochblondiaux.minecraft.graphics.renderer;

import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import me.rochblondiaux.minecraft.scene.Scene;
import me.rochblondiaux.minecraft.game.Window;
import me.rochblondiaux.minecraft.game.model.Cleanable;

public class Renderer implements Cleanable {

    private final SceneRenderer sceneRenderer;

    public Renderer() {
        GL.createCapabilities();
        GL11.glEnable(GL11.GL_DEPTH_TEST);

        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);

        this.sceneRenderer = new SceneRenderer();
    }

    public void render(Window window, Scene scene) {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        GL11.glViewport(0, 0, window.getWidth(), window.getHeight());
        this.sceneRenderer.render(scene);
    }

    @Override
    public void cleanup() {
        this.sceneRenderer.cleanup();
    }
}

package me.rochblondiaux.minecraft;

import lombok.Data;
import me.rochblondiaux.minecraft.game.Engine;
import me.rochblondiaux.minecraft.graphics.renderer.Renderer;
import me.rochblondiaux.minecraft.scene.Scene;
import me.rochblondiaux.minecraft.game.Window;
import me.rochblondiaux.minecraft.game.model.GameLogic;
import me.rochblondiaux.minecraft.graphics.Mesh;

@Data
public class Minecraft implements GameLogic {

    private Engine engine;

    public Minecraft() {
        this.engine = new Engine(
                Window.WindowOptions.builder()
                        .title("Minecraft")
                        .width(1280)
                        .height(720)
                        .fps(60)
                        .ups(30)
                        .build(),
                this
        );
        this.engine.start();
    }

    @Override
    public void init(Window window, Scene scene, Renderer renderer) {
        float[] positions = new float[]{
                -0.5f, 0.5f, -1.0f,
                -0.5f, -0.5f, -1.0f,
                0.5f, -0.5f, -1.0f,
                0.5f, 0.5f, -1.0f,
        };
        float[] colors = new float[]{
                0.5f, 0.0f, 0.0f,
                0.0f, 0.5f, 0.0f,
                0.0f, 0.5f, 0.5f,
                0.0f, 0.5f, 0.5f,
        };
        int[] indices = new int[]{
                0, 1, 3, 3, 1, 2,
        };
        Mesh mesh = new Mesh(positions, colors, indices);
        scene.addMesh("quad", mesh);
    }

    @Override
    public void input(Window window, Scene scene, long deltaTime) {

    }

    @Override
    public void update(Window window, Scene scene, long deltaTime) {

    }

    @Override
    public void cleanup() {

    }
}

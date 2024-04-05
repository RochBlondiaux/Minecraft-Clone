package me.rochblondiaux.minecraft;

import lombok.Data;
import me.rochblondiaux.minecraft.game.Engine;
import me.rochblondiaux.minecraft.graphics.renderer.Renderer;
import me.rochblondiaux.minecraft.game.Scene;
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
        float[] positions = new float[] {
                0.0f, 0.5f, 0.0f,
                -0.5f, -0.5f, 0.0f,
                0.5f, -0.5f, 0.0f
        };
        Mesh mesh = new Mesh(positions, 3);
        scene.addMesh("triangle", mesh);
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

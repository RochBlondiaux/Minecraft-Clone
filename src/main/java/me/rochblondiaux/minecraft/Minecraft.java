package me.rochblondiaux.minecraft;

import java.nio.file.Path;

import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

import lombok.Data;
import me.rochblondiaux.minecraft.game.Engine;
import me.rochblondiaux.minecraft.game.Window;
import me.rochblondiaux.minecraft.game.model.GameLogic;
import me.rochblondiaux.minecraft.graphics.renderer.Renderer;
import me.rochblondiaux.minecraft.input.MouseInput;
import me.rochblondiaux.minecraft.scene.Camera;
import me.rochblondiaux.minecraft.scene.Scene;
import me.rochblondiaux.minecraft.scene.model.Entity;
import me.rochblondiaux.minecraft.scene.model.Model;
import me.rochblondiaux.minecraft.scene.model.ModelLoader;

@Data
public class Minecraft implements GameLogic {

    private static final float MOUSE_SENSITIVITY = 0.1f;
    private static final float MOVEMENT_SPEED = 0.05f;

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

    private Entity cubeEntity;

    @Override
    public void init(Window window, Scene scene, Renderer renderer) {
        Model cubeModel = ModelLoader.loadModel("cube-model", Path.of("C:\\Users\\Roch Blondiaux\\Documents\\Development\\Games\\Minecraft-Clone\\src\\main\\resources\\models\\abra\\abra.obj"),
                scene.getTextureAtlas());
        scene.addModel(cubeModel);

        cubeEntity = new Entity("cube-entity", cubeModel.getId());
        cubeEntity.setPosition(0, 0, -2);
        scene.addEntity(cubeEntity);
    }

    @Override
    public void input(Window window, Scene scene, long deltaTime) {
        final float move = deltaTime * MOVEMENT_SPEED;
        final Camera camera = scene.getCamera();

        if (window.isKeyPressed(GLFW.GLFW_KEY_W))
            camera.moveForward(move);
        else if (window.isKeyPressed(GLFW.GLFW_KEY_S))
            camera.moveBackward(move);
        if (window.isKeyPressed(GLFW.GLFW_KEY_A))
            camera.moveLeft(move);
        else if (window.isKeyPressed(GLFW.GLFW_KEY_D))
            camera.moveRight(move);
        if (window.isKeyPressed(GLFW.GLFW_KEY_UP))
            camera.moveUp(move);
        else if (window.isKeyPressed(GLFW.GLFW_KEY_DOWN))
            camera.moveDown(move);

        MouseInput mouseInput = window.getMouseInput();
        if (mouseInput.isRightButtonPressed()) {
            Vector2f displVec = mouseInput.getDisplayVector();
            camera.addRotation((float) Math.toRadians(-displVec.x * MOUSE_SENSITIVITY),
                    (float) Math.toRadians(-displVec.y * MOUSE_SENSITIVITY));
        }

    }

    @Override
    public void update(Window window, Scene scene, long deltaTime) {

    }

    @Override
    public void cleanup() {

    }
}

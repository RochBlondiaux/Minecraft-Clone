package me.rochblondiaux.minecraft;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

import lombok.Data;
import me.rochblondiaux.minecraft.game.Engine;
import me.rochblondiaux.minecraft.game.Window;
import me.rochblondiaux.minecraft.game.model.GameLogic;
import me.rochblondiaux.minecraft.graphics.Entity;
import me.rochblondiaux.minecraft.graphics.Material;
import me.rochblondiaux.minecraft.graphics.Mesh;
import me.rochblondiaux.minecraft.graphics.Model;
import me.rochblondiaux.minecraft.graphics.renderer.Renderer;
import me.rochblondiaux.minecraft.graphics.texture.Texture;
import me.rochblondiaux.minecraft.input.MouseInput;
import me.rochblondiaux.minecraft.scene.Camera;
import me.rochblondiaux.minecraft.scene.Scene;

@Data
public class Minecraft implements GameLogic {

    private static final float MOUSE_SENSITIVITY = 0.1f;
    private static final float MOVEMENT_SPEED = 0.005f;

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
        float[] positions = new float[]{
                // V0
                -0.5f, 0.5f, 0.5f,
                // V1
                -0.5f, -0.5f, 0.5f,
                // V2
                0.5f, -0.5f, 0.5f,
                // V3
                0.5f, 0.5f, 0.5f,
                // V4
                -0.5f, 0.5f, -0.5f,
                // V5
                0.5f, 0.5f, -0.5f,
                // V6
                -0.5f, -0.5f, -0.5f,
                // V7
                0.5f, -0.5f, -0.5f,

                // For text coords in top face
                // V8: V4 repeated
                -0.5f, 0.5f, -0.5f,
                // V9: V5 repeated
                0.5f, 0.5f, -0.5f,
                // V10: V0 repeated
                -0.5f, 0.5f, 0.5f,
                // V11: V3 repeated
                0.5f, 0.5f, 0.5f,

                // For text coords in right face
                // V12: V3 repeated
                0.5f, 0.5f, 0.5f,
                // V13: V2 repeated
                0.5f, -0.5f, 0.5f,

                // For text coords in left face
                // V14: V0 repeated
                -0.5f, 0.5f, 0.5f,
                // V15: V1 repeated
                -0.5f, -0.5f, 0.5f,

                // For text coords in bottom face
                // V16: V6 repeated
                -0.5f, -0.5f, -0.5f,
                // V17: V7 repeated
                0.5f, -0.5f, -0.5f,
                // V18: V1 repeated
                -0.5f, -0.5f, 0.5f,
                // V19: V2 repeated
                0.5f, -0.5f, 0.5f,
        };
        float[] textCoords = new float[]{
                0.0f, 0.0f,
                0.0f, 0.5f,
                0.5f, 0.5f,
                0.5f, 0.0f,

                0.0f, 0.0f,
                0.5f, 0.0f,
                0.0f, 0.5f,
                0.5f, 0.5f,

                // For text coords in top face
                0.0f, 0.5f,
                0.5f, 0.5f,
                0.0f, 1.0f,
                0.5f, 1.0f,

                // For text coords in right face
                0.0f, 0.0f,
                0.0f, 0.5f,

                // For text coords in left face
                0.5f, 0.0f,
                0.5f, 0.5f,

                // For text coords in bottom face
                0.5f, 0.0f,
                1.0f, 0.0f,
                0.5f, 0.5f,
                1.0f, 0.5f,
        };
        int[] indices = new int[]{
                // Front face
                0, 1, 3, 3, 1, 2,
                // Top Face
                8, 10, 11, 9, 8, 11,
                // Right face
                12, 13, 7, 5, 12, 7,
                // Left face
                14, 15, 6, 4, 14, 6,
                // Bottom face
                16, 18, 19, 17, 16, 19,
                // Back face
                4, 6, 7, 5, 4, 7,};
        Texture texture = scene.getTextureAtlas().createTexture("C:\\Users\\Roch Blondiaux\\Documents\\Development\\Games\\Minecraft-Clone\\src\\main\\resources\\textures\\block\\grass.png");
        Material material = new Material();
        material.setTexturePath(texture.getPath());
        List<Material> materialList = new ArrayList<>();
        materialList.add(material);

        Mesh mesh = new Mesh(positions, textCoords, indices);
        material.getMeshes().add(mesh);
        Model cubeModel = new Model("cube-model", materialList);
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

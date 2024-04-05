package me.rochblondiaux.minecraft.scene;

import org.joml.Matrix4f;

import lombok.Data;

@Data
public class Projection {

    private static final float FOV = (float) Math.toRadians(60.0f);
    private static final float Z_FAR = 1000.0f;
    private static final float Z_NEAR = 0.01f;

    private final Matrix4f projectionMatrix;

    public Projection(int width, int height) {
        this.projectionMatrix = new Matrix4f();
        this.update(width, height);
    }

    public void update(int width, int height) {
        float aspectRatio = (float) width / height;
        this.projectionMatrix.identity().perspective(FOV, aspectRatio, Z_NEAR, Z_FAR);
    }
}

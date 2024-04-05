package me.rochblondiaux.minecraft.scene.model;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import lombok.Data;

@Data
public class Entity {

    private final String id;
    private final String modelId;
    private final Matrix4f modelMatrix = new Matrix4f();
    private final Vector3f position = new Vector3f();
    private final Quaternionf rotation = new Quaternionf();
    private float scale = 1;

    public void setPosition(float x, float y, float z) {
        this.position.set(x, y, z);
    }

    public void setRotation(float x, float y, float z, float angle) {
        this.rotation.fromAxisAngleRad(x, y, z, angle);
    }

    public void updateModelMatrix() {
        this.modelMatrix.translationRotateScale(
                this.position,
                this.rotation,
                this.scale
        );
    }

}

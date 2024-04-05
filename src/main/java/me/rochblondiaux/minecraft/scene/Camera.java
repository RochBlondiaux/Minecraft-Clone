package me.rochblondiaux.minecraft.scene;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import lombok.Data;

@Data
public class Camera {

    private final Vector3f direction = new Vector3f();
    private final Vector3f right = new Vector3f();
    private final Vector3f up = new Vector3f();
    private final Vector3f position = new Vector3f();
    private final Matrix4f viewMatrix = new Matrix4f();
    private final Vector2f rotation = new Vector2f();


    public void addRotation(float x, float y) {
        this.rotation.add(x, y);
        this.recalculate();
    }

    public void moveBackward(float distance) {
        this.viewMatrix.positiveZ(direction)
                .negate()
                .mul(distance);
        this.position.sub(direction);
        this.recalculate();
    }

    public void moveDown(float distance) {
        this.viewMatrix.positiveY(up).mul(distance);
        this.position.sub(up);
        this.recalculate();
    }

    public void moveForward(float distance) {
        this.viewMatrix.positiveZ(direction)
                .negate()
                .mul(distance);
        this.position.add(direction);
        this.recalculate();
    }

    public void moveLeft(float distance) {
        this.viewMatrix.positiveX(right).mul(distance);
        this.position.sub(right);
        this.recalculate();
    }

    public void moveRight(float distance) {
        this.viewMatrix.positiveX(right).mul(distance);
        this.position.add(right);
        this.recalculate();
    }

    public void moveUp(float distance) {
        this.viewMatrix.positiveY(up).mul(distance);
        this.position.add(up);
        this.recalculate();
    }

    public void setPosition(float x, float y, float z) {
        this.position.set(x, y, z);
        this.recalculate();
    }

    public void rotate(float x, float y) {
        this.rotation.set(x, y);
        this.recalculate();
    }

    private void recalculate() {
        this.viewMatrix.identity()
                .rotateX(this.rotation.x)
                .rotateY(this.rotation.y)
                .translate(-this.position.x, -this.position.y, -this.position.z);
    }
}

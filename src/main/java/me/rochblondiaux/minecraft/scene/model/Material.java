package me.rochblondiaux.minecraft.scene.model;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector4f;

import lombok.Data;
import me.rochblondiaux.minecraft.game.model.Cleanable;

@Data
public class Material implements Cleanable {

    public static final Vector4f DEFAULT_COLOR = new Vector4f(0.0f, 0.0f, 0.0f, 1.0f);

    private String texturePath;
    private final Vector4f diffuseColor = new Vector4f(DEFAULT_COLOR);
    private final List<Mesh> meshes = new ArrayList<>();

    public void setDiffuseColor(Vector4f color) {
        this.diffuseColor.set(color);
    }

    @Override
    public void cleanup() {
        this.meshes.forEach(Mesh::cleanup);
    }
}

package me.rochblondiaux.minecraft.game;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;
import me.rochblondiaux.minecraft.game.model.Cleanable;
import me.rochblondiaux.minecraft.graphics.Mesh;

@Data
public class Scene implements Cleanable {

    private final Map<String, Mesh> meshes = new HashMap<>();

    public void addMesh(String name, Mesh mesh) {
        meshes.put(name, mesh);
    }

    @Override
    public void cleanup() {
        this.meshes.values().forEach(Mesh::cleanup);
    }
}

package me.rochblondiaux.minecraft.graphics;


import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import me.rochblondiaux.minecraft.game.model.Cleanable;

@Data
public class Model implements Cleanable {

    private final String id;
    private final List<Entity> entities = new ArrayList<>();
    private final List<Mesh> meshes;

    public Model(String id, List<Mesh> meshes) {
        this.id = id;
        this.meshes = meshes;
    }

    @Override
    public void cleanup() {
        this.meshes.forEach(Mesh::cleanup);
    }
}

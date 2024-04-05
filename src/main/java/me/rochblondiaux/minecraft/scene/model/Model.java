package me.rochblondiaux.minecraft.scene.model;


import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import me.rochblondiaux.minecraft.game.model.Cleanable;

@Data
public class Model implements Cleanable {

    private final String id;
    private final List<Entity> entities = new ArrayList<>();
    private final List<Material> materials = new ArrayList<>();

    public Model(String id, List<Material> materials) {
        this.id = id;
        this.materials.addAll(materials);
    }

    @Override
    public void cleanup() {
        this.materials.forEach(Material::cleanup);
    }
}

package me.rochblondiaux.minecraft.graphics;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import me.rochblondiaux.minecraft.game.model.Cleanable;

@Data
public class Material implements Cleanable {

    private String texturePath;
    private final List<Mesh> meshes = new ArrayList<>();


    @Override
    public void cleanup() {
        this.meshes.forEach(Mesh::cleanup);
    }
}

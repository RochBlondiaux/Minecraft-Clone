package me.rochblondiaux.minecraft.scene;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;
import me.rochblondiaux.minecraft.game.model.Cleanable;
import me.rochblondiaux.minecraft.graphics.Entity;
import me.rochblondiaux.minecraft.graphics.Mesh;
import me.rochblondiaux.minecraft.graphics.Model;

@Data
public class Scene implements Cleanable {

    private final Map<String, Mesh> meshes = new HashMap<>();
    private final Map<String, Model> models = new HashMap<>();
    private final Projection projection;

    public Scene(int width, int height) {
        this.projection = new Projection(width, height);
    }

    public void addEntity(Entity entity) {
        String modelId = entity.getModelId();
        Model model = models.get(modelId);
        if (model == null)
            throw new IllegalArgumentException("Model not found: " + modelId);
        model.getEntities().add(entity);
    }

    public void addModel(Model model) {
        models.put(model.getId(), model);
    }


    public void resize(int width, int height) {
        this.projection.update(width, height);
    }

    public void addMesh(String name, Mesh mesh) {
        meshes.put(name, mesh);
    }

    @Override
    public void cleanup() {
        this.meshes.values().forEach(Mesh::cleanup);
        this.models.values().forEach(Model::cleanup);
    }
}

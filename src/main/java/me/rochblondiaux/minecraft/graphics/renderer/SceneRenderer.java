package me.rochblondiaux.minecraft.graphics.renderer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import lombok.Data;
import me.rochblondiaux.minecraft.game.model.Cleanable;
import me.rochblondiaux.minecraft.graphics.Entity;
import me.rochblondiaux.minecraft.graphics.Mesh;
import me.rochblondiaux.minecraft.graphics.Model;
import me.rochblondiaux.minecraft.graphics.UniformMap;
import me.rochblondiaux.minecraft.scene.Scene;
import me.rochblondiaux.minecraft.shader.ShaderModuleData;
import me.rochblondiaux.minecraft.shader.ShaderProgram;

@Data
public class SceneRenderer implements Cleanable {

    private final ShaderProgram shaderProgram;
    private final UniformMap uniformMap;

    public SceneRenderer() {
        List<ShaderModuleData> shaderModuleData = new ArrayList<>();
        shaderModuleData.add(new ShaderModuleData("shaders/scene.vert", GL20.GL_VERTEX_SHADER));
        shaderModuleData.add(new ShaderModuleData("shaders/scene.frag", GL20.GL_FRAGMENT_SHADER));
        this.shaderProgram = new ShaderProgram(shaderModuleData);

        this.uniformMap = new UniformMap(shaderProgram.getId());
        this.uniformMap.create("projectionMatrix");
        this.uniformMap.create("modelMatrix");
    }

    public void render(Scene scene) {
        // Bind the shader program
        this.shaderProgram.bind();

        // Set the uniforms
        this.uniformMap.set("projectionMatrix", scene.getProjection().getProjectionMatrix());

        // Render the scene
        Collection<Model> models = scene.getModels().values();
        for (Model model : models) {
            for (Mesh mesh : model.getMeshes()) {
                GL30.glBindVertexArray(mesh.getVaoId());
                List<Entity> entities = model.getEntities();
                for (Entity entity : entities) {
                    this.uniformMap.set("modelMatrix", entity.getModelMatrix());
                    GL30.glDrawElements(GL30.GL_TRIANGLES, mesh.getVerticesCount(), GL30.GL_UNSIGNED_INT, 0);
                }
            }
        }


        // Clear the depth buffer
        GL30.glBindVertexArray(0);

        // Unbind the shader program
        this.shaderProgram.unbind();
    }

    public void createUniforms() {

    }

    @Override
    public void cleanup() {
        this.shaderProgram.cleanup();
    }
}

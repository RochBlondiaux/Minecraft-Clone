package me.rochblondiaux.minecraft.graphics.renderer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.lwjgl.opengl.GL13C;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import lombok.Data;
import me.rochblondiaux.minecraft.game.model.Cleanable;
import me.rochblondiaux.minecraft.graphics.*;
import me.rochblondiaux.minecraft.graphics.texture.Texture;
import me.rochblondiaux.minecraft.graphics.texture.TextureAtlas;
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
        this.createUniforms();
    }

    public void render(Scene scene) {
        // Bind the shader program
        this.shaderProgram.bind();

        // Set the uniforms
        this.uniformMap.set("projectionMatrix", scene.getProjection().getProjectionMatrix());
        this.uniformMap.set("viewMatrix", scene.getCamera().getViewMatrix());
        this.uniformMap.set("txtSampler", 0);

        // Render the scene
        Collection<Model> models = scene.getModels().values();
        TextureAtlas textureAtlas = scene.getTextureAtlas();
        for (Model model : models) {
            List<Entity> entities = model.getEntities();

            // Materials
            for (Material material : model.getMaterials()) {
                Texture texture = textureAtlas.getTexture(material.getTexturePath());
                GL13C.glActiveTexture(GL13C.GL_TEXTURE0);
                texture.bind();

                // Mesh
                for (Mesh mesh : material.getMeshes()) {
                    GL30.glBindVertexArray(mesh.getVaoId());
                    for (Entity entity : entities) {
                        this.uniformMap.set("modelMatrix", entity.getModelMatrix());
                        GL30.glDrawElements(GL30.GL_TRIANGLES, mesh.getVerticesCount(), GL30.GL_UNSIGNED_INT, 0);
                    }
                }
            }
        }


        // Clear the depth buffer
        GL30.glBindVertexArray(0);

        // Unbind the shader program
        this.shaderProgram.unbind();
    }

    public void createUniforms() {
        this.uniformMap.create("projectionMatrix");
        this.uniformMap.create("viewMatrix");
        this.uniformMap.create("modelMatrix");
        this.uniformMap.create("txtSampler");
    }

    @Override
    public void cleanup() {
        this.shaderProgram.cleanup();
    }
}

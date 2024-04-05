package me.rochblondiaux.minecraft.graphics.renderer;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import lombok.Data;
import me.rochblondiaux.minecraft.graphics.UniformMap;
import me.rochblondiaux.minecraft.scene.Scene;
import me.rochblondiaux.minecraft.game.model.Cleanable;
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
    }

    public void render(Scene scene) {
        this.shaderProgram.bind();

        this.uniformMap.set("projectionMatrix", scene.getProjection().getProjectionMatrix());

        scene.getMeshes()
                .values()
                .forEach(mesh -> {
                    GL30.glBindVertexArray(mesh.getVaoId());
                    GL11.glDrawElements(GL11.GL_TRIANGLES, mesh.getVerticesCount(), GL11.GL_UNSIGNED_INT, 0);
                });
        GL30.glBindVertexArray(0);

        this.shaderProgram.unbind();
    }

    public void createUniforms() {

    }

    @Override
    public void cleanup() {
        this.shaderProgram.cleanup();
    }
}

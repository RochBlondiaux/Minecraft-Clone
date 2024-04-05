package me.rochblondiaux.minecraft.shader;

import java.util.List;

import org.lwjgl.opengl.GL20;

import lombok.Getter;
import me.rochblondiaux.minecraft.game.model.Bindable;
import me.rochblondiaux.minecraft.game.model.Cleanable;
import me.rochblondiaux.minecraft.utils.ResourceUtils;

@Getter
public class ShaderProgram implements Bindable, Cleanable {

    private final int id;

    public ShaderProgram(List<ShaderModuleData> shaderModuleDataList) {
        this.id = GL20.glCreateProgram();
        if (this.id == 0)
            throw new IllegalStateException("Failed to create shader program");

        // Create shader modules
        List<Integer> shaderIds = shaderModuleDataList.stream()
                .map(shaderModuleData -> this.createShader(ResourceUtils.readResource(shaderModuleData.path()), shaderModuleData.type()))
                .toList();

        // Link shader modules
        this.link(shaderIds);
    }

    protected int createShader(String source, int type) {
        int shaderId = GL20.glCreateShader(type);
        if (shaderId == 0)
            throw new IllegalStateException("Failed to create shader");

        GL20.glShaderSource(shaderId, source);
        GL20.glCompileShader(shaderId);

        if (GL20.glGetShaderi(shaderId, GL20.GL_COMPILE_STATUS) == 0)
            throw new IllegalStateException("Failed to compile shader: " + GL20.glGetShaderInfoLog(shaderId));

        GL20.glAttachShader(this.id, shaderId);

        return shaderId;
    }

    private void link(List<Integer> shaderModules) {
        // Attach shader modules
        GL20.glLinkProgram(this.id);

        // Check for linking errors
        if (GL20.glGetProgrami(this.id, GL20.GL_LINK_STATUS) == 0)
            throw new IllegalStateException("Failed to link shader program: " + GL20.glGetProgramInfoLog(this.id));

        // Detach and delete shader modules
        shaderModules.forEach(integer -> GL20.glDetachShader(this.id, integer));
        shaderModules.forEach(GL20::glDeleteShader);
    }

    public void validate() {
        GL20.glValidateProgram(this.id);

        if (GL20.glGetProgrami(this.id, GL20.GL_VALIDATE_STATUS) == 0)
            throw new IllegalStateException("Failed to validate shader program: " + GL20.glGetProgramInfoLog(this.id));
    }

    @Override
    public void bind() {
        GL20.glUseProgram(this.id);
    }

    @Override
    public void unbind() {
        GL20.glUseProgram(0);
    }

    @Override
    public void cleanup() {
        unbind();

        if (this.id != 0)
            GL20.glDeleteProgram(this.id);
    }
}

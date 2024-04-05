package me.rochblondiaux.minecraft.graphics;

import java.util.HashMap;
import java.util.Map;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL20;
import org.lwjgl.system.MemoryStack;

import lombok.Data;

@Data
public class UniformMap {

    private final int programId;
    private final Map<String, Integer> uniforms = new HashMap<>();

    private int getUniformLocation(String name) {
        Integer location = this.uniforms.get(name);
        if (location == null)
            throw new IllegalStateException("Could not find uniform variable '" + name + "'.");
        return location;
    }

    public void create(String name) {
        int uniformLocation = GL20.glGetUniformLocation(this.programId, name);
        if (uniformLocation < 0)
            throw new IllegalStateException("Could not find uniform variable '" + name + "'.");
        this.uniforms.put(name, uniformLocation);
    }

    public void set(String name, Matrix4f value) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            GL20.glUniformMatrix4fv(getUniformLocation(name), false, value.get(stack.mallocFloat(16)));
        }
    }

    public void set(String name, int value) {
        GL20.glUniform1i(getUniformLocation(name), value);
    }
}

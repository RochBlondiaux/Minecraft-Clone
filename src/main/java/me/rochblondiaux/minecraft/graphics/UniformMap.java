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

    public void create(String name) {
        int uniformLocation = GL20.glGetUniformLocation(this.programId, name);
        if (uniformLocation < 0)
            throw new IllegalStateException("Could not find uniform variable '" + name + "'.");
        this.uniforms.put(name, uniformLocation);
    }

    public void set(String name, Matrix4f value) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            Integer location = this.uniforms.get(name);
            if (location == null)
                throw new IllegalStateException("Could not find uniform variable '" + name + "'.");
            GL20.glUniformMatrix4fv(location, false, value.get(stack.mallocFloat(16)));
        }
    }
}

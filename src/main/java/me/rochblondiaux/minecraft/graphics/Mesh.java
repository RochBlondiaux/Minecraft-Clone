package me.rochblondiaux.minecraft.graphics;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryStack;

import lombok.Data;
import me.rochblondiaux.minecraft.game.model.Cleanable;

@Data
public class Mesh implements Cleanable {

    private final int dimensions;
    private final int vaoId;
    private final List<Integer> vboIds = new ArrayList<>();

    public Mesh(float[] positions, int dimensions) {
        this.dimensions = dimensions;

        try (MemoryStack stack = MemoryStack.stackPush()) {
            this.vaoId = GL30.glGenVertexArrays();
            GL30.glBindVertexArray(this.vaoId);

            // Position VBO
            int vboId = GL30.glGenBuffers();
            this.vboIds.add(vboId);
            FloatBuffer positionsBuffer = stack.mallocFloat(positions.length);
            positionsBuffer.put(0, positions);
            GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, vboId);
            GL30.glBufferData(GL30.GL_ARRAY_BUFFER, positionsBuffer, GL30.GL_STATIC_DRAW);
            GL20.glEnableVertexAttribArray(0);
            GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);

            GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, 0);
            GL30.glBindVertexArray(0);
        }
    }

    @Override
    public void cleanup() {
        this.vboIds.forEach(GL30::glDeleteBuffers);
        GL30.glDeleteVertexArrays(this.vaoId);
    }
}

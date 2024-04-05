package me.rochblondiaux.minecraft.graphics;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
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

    private final int verticesCount;
    private final int vaoId;
    private final List<Integer> vboIds = new ArrayList<>();

    public Mesh(float[] positions, float[] colors, int[] indices) {
        this.verticesCount = indices.length;
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

            // Color VBO
            vboId = GL30.glGenBuffers();
            this.vboIds.add(vboId);
            FloatBuffer colorsBuffer = stack.mallocFloat(colors.length);
            colorsBuffer.put(0, colors);
            GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, vboId);
            GL30.glBufferData(GL30.GL_ARRAY_BUFFER, colorsBuffer, GL30.GL_STATIC_DRAW);
            GL20.glEnableVertexAttribArray(1);
            GL20.glVertexAttribPointer(1, 3, GL11.GL_FLOAT, false, 0, 0);

            // Index VBO
            vboId = GL30.glGenBuffers();
            this.vboIds.add(vboId);
            IntBuffer indicesBuffer = stack.mallocInt(indices.length);
            indicesBuffer.put(0, indices);
            GL30.glBindBuffer(GL30.GL_ELEMENT_ARRAY_BUFFER, vboId);
            GL30.glBufferData(GL30.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL30.GL_STATIC_DRAW);

            // Unbind the VBO
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

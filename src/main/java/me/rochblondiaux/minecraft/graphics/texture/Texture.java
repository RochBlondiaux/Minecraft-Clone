package me.rochblondiaux.minecraft.graphics.texture;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import lombok.Data;
import me.rochblondiaux.minecraft.game.model.Cleanable;

@Data
public class Texture implements Cleanable {

    private final int id;
    private final String path;

    public Texture(int width, int height, ByteBuffer byteBuffer) {
        this.path = "";
        this.id = generateTexture(width, height, byteBuffer);
    }

    public Texture(String path) {
        this.path = path;
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer width = stack.mallocInt(1);
            IntBuffer height = stack.mallocInt(1);
            IntBuffer channels = stack.mallocInt(1);

            ByteBuffer data = STBImage.stbi_load(path, width, height, channels, 4);
            if (data == null)
                throw new RuntimeException("Failed to load a texture file: " + path);

            int widthValue = width.get();
            int heightValue = height.get();

            this.id = generateTexture(widthValue, heightValue, data);

            STBImage.stbi_image_free(data);
        }
    }

    public void bind() {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
    }

    public void unbind() {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
    }

    @Override
    public void cleanup() {
        GL11.glDeleteTextures(id);
    }

    private int generateTexture(int width, int height, ByteBuffer byteBuffer) {
        int id = GL11.glGenTextures();

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0,
                GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, byteBuffer);
        GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);

        return id;
    }
}

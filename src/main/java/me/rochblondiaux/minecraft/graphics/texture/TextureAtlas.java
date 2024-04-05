package me.rochblondiaux.minecraft.graphics.texture;

import java.util.HashMap;
import java.util.Map;

import me.rochblondiaux.minecraft.game.model.Cleanable;

public class TextureAtlas implements Cleanable {

    public static final String DEFAULT_TEXTURE = "C:\\Users\\Roch Blondiaux\\Documents\\Development\\Games\\Minecraft-Clone\\src\\main\\resources\\textures\\missing.png";
    private final Map<String, Texture> textures = new HashMap<>();

    public TextureAtlas() {
        textures.put(DEFAULT_TEXTURE, new Texture(DEFAULT_TEXTURE));
    }

    public Texture createTexture(String path) {
        return this.textures.computeIfAbsent(path, Texture::new);
    }

    public Texture getTexture(String path) {
        return this.textures.getOrDefault(path, this.textures.get(DEFAULT_TEXTURE));
    }

    @Override
    public void cleanup() {
        this.textures.values().forEach(Texture::cleanup);
    }
}

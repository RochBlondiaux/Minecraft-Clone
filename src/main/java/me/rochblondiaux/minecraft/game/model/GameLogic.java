package me.rochblondiaux.minecraft.game.model;

import me.rochblondiaux.minecraft.graphics.renderer.Renderer;
import me.rochblondiaux.minecraft.scene.Scene;
import me.rochblondiaux.minecraft.game.Window;

public interface GameLogic extends Cleanable {

    void init(Window window, Scene scene, Renderer renderer);

    void input(Window window, Scene scene, long deltaTime);

    void update(Window window, Scene scene, long deltaTime);

}

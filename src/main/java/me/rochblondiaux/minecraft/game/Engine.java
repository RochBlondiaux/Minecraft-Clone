package me.rochblondiaux.minecraft.game;

import me.rochblondiaux.minecraft.game.model.Cleanable;
import me.rochblondiaux.minecraft.game.model.GameLogic;
import me.rochblondiaux.minecraft.graphics.renderer.Renderer;
import me.rochblondiaux.minecraft.scene.Scene;

public class Engine implements Cleanable {

    public static final int TARGET_UPS = 30;
    private final GameLogic gameLogic;
    private final Window window;
    private Renderer renderer;
    private boolean running;
    private Scene scene;
    private int targetFps;
    private int targetUps;

    public Engine(Window.WindowOptions options, GameLogic logic) {
        this.window = new Window(options, resizeCallback());
        this.gameLogic = logic;
        this.renderer = new Renderer();
        this.scene = new Scene(window.getWidth(), window.getWidth());
        this.targetFps = options.getFps();
        this.targetUps = options.getUps();

        this.gameLogic.init(window, scene, renderer);
    }

    public void start() {
        this.running = true;
        this.run();
    }

    public void stop() {
        this.running = false;
    }

    private void run() {
        long initialTime = System.currentTimeMillis();
        float updateInterval = 1000.0f / this.targetUps;
        float renderInterval = this.targetFps > 0 ? 1000.0f / this.targetFps : 0;
        float deltaUpdate = 0;
        float deltaRender = 0;

        long updateTime = initialTime;
        while (this.running && !this.window.shouldClose()) {
            this.window.poolEvents();

            long currentTime = System.currentTimeMillis();
            deltaUpdate += (currentTime - initialTime) / updateInterval;
            deltaRender += (currentTime - initialTime) / renderInterval;

            // Handle inputs
            if (targetFps <= 0 || deltaRender >= 1) {
                this.window.getMouseInput().input();
                this.gameLogic.input(this.window, this.scene, currentTime - initialTime);
            }

            // Update
            if (deltaUpdate >= 1) {
                long diff = currentTime - updateTime;
                this.gameLogic.update(this.window, this.scene, diff);
                updateTime = currentTime;
                deltaUpdate--;
            }

            // Render
            if (targetFps <= 0 || deltaRender >= 1) {
                this.renderer.render(this.window, this.scene);
                deltaRender--;
                window.update();
            }

            initialTime = currentTime;
        }

        this.cleanup();
    }

    private Runnable resizeCallback() {
        return () -> {
            this.scene.resize(window.getWidth(), window.getHeight());
        };
    }

    @Override
    public void cleanup() {
        this.window.cleanup();
        this.renderer.cleanup();
        this.scene.cleanup();
    }
}

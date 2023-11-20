/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.lib.thread;

public abstract class AbstractPerTickRunner extends Thread {
    private static final int BILION = 1_000_000_000;
    private static final double MAX_FPS = 60.0;

    @Override
    public void run() {
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;

        while (true) {
            final double drawInterval = BILION / MAX_FPS;
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;
            if (delta >= 1) {
                onTickUpdate();
                delta--;
                drawCount++;
            }
            if (timer >= BILION) {
                onUpdateFpsState(drawCount);
                drawCount = 0;
                timer = 0;
            }
        }
    }

    public abstract void onTickUpdate();
    public abstract void onUpdateFpsState(int fpsValue);
}

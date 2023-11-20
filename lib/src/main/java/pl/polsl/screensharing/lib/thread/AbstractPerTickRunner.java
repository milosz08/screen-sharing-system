/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.lib.thread;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbstractPerTickRunner implements Runnable {
    private static final int MILION = 1_000_000;
    private static final int BILION = 1_000_000_000;

    private final Thread thread;
    private final AtomicInteger maxFps;

    public AbstractPerTickRunner(int maxFps) {
        thread = new Thread(this);
        this.maxFps = new AtomicInteger(maxFps);
    }

    @Override
    public void run() {
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;

        while (thread != null) {
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

    public void startThread() {
        if (!thread.isAlive()) {
            thread.start();
        }
    }

    public void updateMaxFps(int maxFps) {
        this.maxFps.set(maxFps);
    }

    public abstract void onTickUpdate();
    public abstract void onUpdateFpsState(int fpsValue);
}

package pl.polsl.screensharing.lib.thread;

import pl.polsl.screensharing.lib.SharedConstants;

public abstract class AbstractPerTickRunner extends Thread {
    private static final double MAX_FPS = 60.0;

    @Override
    public void run() {
        final double drawInterval = SharedConstants.BILION / MAX_FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;

        while (true) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;
            if (delta >= 1) {
                onTickUpdate();
                delta--;
                drawCount++;
            }
            if (timer >= SharedConstants.BILION) {
                onUpdateFpsState(drawCount);
                drawCount = 0;
                timer = 0;
            }
        }
    }

    public abstract void onTickUpdate();

    public abstract void onUpdateFpsState(int fpsValue);
}

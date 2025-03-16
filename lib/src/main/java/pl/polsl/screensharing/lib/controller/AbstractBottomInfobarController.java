package pl.polsl.screensharing.lib.controller;

import pl.polsl.screensharing.lib.Utils;
import pl.polsl.screensharing.lib.gui.AbstractBottomInfobar;

import javax.swing.*;
import java.util.function.Consumer;

public abstract class AbstractBottomInfobarController {
    private final Timer jvmMeasurementsTimer;

    private final Consumer<AbstractBottomInfobar> jvmMeasurementsListener = bottomInfobar -> {
        final long memory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        bottomInfobar.getMemoryUsageLabel().setText(Utils.parseBytes(memory, "Memory", false));
    };

    protected AbstractBottomInfobarController(AbstractBottomInfobar bottomInfobar) {
        jvmMeasurementsTimer = new Timer(5000, e -> jvmMeasurementsListener.accept(bottomInfobar));
        jvmMeasurementsTimer.start();
    }
}

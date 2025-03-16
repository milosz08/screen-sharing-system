package pl.polsl.screensharing.lib.gui.fragment;

import io.reactivex.rxjava3.core.Observable;
import pl.polsl.screensharing.lib.state.AbstractDisposableProvider;
import pl.polsl.screensharing.lib.state.ColoredLabelState;

import javax.swing.*;
import java.awt.*;

public class JAppActionRectInfo extends JPanel {
    private Color selectedColor;

    public JAppActionRectInfo(
        Observable<? extends ColoredLabelState> state$,
        AbstractDisposableProvider disposableProvider
    ) {
        selectedColor = Color.GRAY;
        disposableProvider.wrapAsDisposable(state$, color -> {
            selectedColor = color.getColor();
            repaint();
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(selectedColor);
        g.fillRect(10, 10, 30, 30);
    }
}

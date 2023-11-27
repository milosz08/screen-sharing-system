/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.host.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.NoArgsConstructor;

import java.awt.*;

@NoArgsConstructor
public class FrameColorRgb {
    private int red;
    private int green;
    private int blue;

    public FrameColorRgb(Color color) {
        red = color.getRed();
        green = color.getGreen();
        blue = color.getBlue();
    }

    @JsonIgnore
    public Color getColor() {
        return new Color(red, green, blue);
    }
}

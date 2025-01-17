package command.net;

import java.awt.*;

public enum AtariColor {
    WHITE(new Color(0, 0, 0)),
    GOLD(new Color(67, 68, 3)),
    ORANGE(new Color(114, 41, 1)),
    BRIGHT_ORANGE(new Color(128, 28, 0)),
    PINK(new Color(138, 1, 1)),
    PURPLE(new Color(127, 1, 97)),
    PURPLE_BLUE(new Color(72, 1, 112)),
    BLUE(new Color(21, 2, 118)),
    BLUE2(new Color(0, 4, 125)),
    LIGHT_BLUE(new Color(1, 24, 129)),
    TORQ(new Color(1, 44, 87)),
    GREEN_BLUE(new Color(0, 61, 43)),
    GREEN(new Color(0, 59, 1)),
    YELLOW_GREEN(new Color(18, 56, 4)),
    ORANGE_GREEN(new Color(42, 48, 0)),
    LIGHT_ORANGE(new Color(65, 41, 1));

    public final Color color;

    AtariColor(Color color) {
        this.color = color;
    }
}

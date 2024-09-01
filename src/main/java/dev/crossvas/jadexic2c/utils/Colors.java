package dev.crossvas.jadexic2c.utils;

public enum Colors {
    MONO_GREEN(0xFF0CA21D),
    MONO_LIME(0xFF00FF07),
    MONO_AQUA(0xFF00F2F2),
    MONO_YELLOW(0xFFEDDB05),
    YELLOW(0xFFEDDB05),
    GREEN(0xFF06B50F),
    RED(0xFFFF1100),
    BLUE(0xFF00ABFF),
    ORANGE(0xFFFB7D00),
    AQUA(0xFF00F2F2),
    PURPLE(0xFFBC3987),
    BROWN(0xFF7A4D33),
    CORNFLOWER(0xFF8496FF),
    PINK(0xFFFF79FF),
    GOLD(0xFFFFF200);

    public final int aColor;
    public final int bColor;
    public final int back;

    Colors(int color) {
        this.aColor = color;
        this.bColor = darker(color);
        this.back = doubleDarker(color);
    }

    public static int doubleDarker(int color) {
        return darker(darker(color));
    }

    public static int trippleDarker(int color) {
        return darker(darker(darker(color)));
    }

    public static int darker(int color) {
        return darker(color, 0.7F);
    }

    public static int darker(int color, float factor) {
        if (Float.compare(factor, 1.0F) == 0) {
            return color;
        } else {
            int r = Math.max(0, (int)((float)(color >> 16 & 255) * factor));
            int g = Math.max(0, (int)((float)(color >> 8 & 255) * factor));
            int b = Math.max(0, (int)((float)(color & 255) * factor));
            return color & -16777216 | (r & 255) << 16 | (g & 255) << 8 | b & 255;
        }
    }

    public static int doubleBrighter(int color) {
        return brighter(brighter(color));
    }

    public static int brighter(int color) {
        return brighter(color, 0.7F);
    }

    public static int brighter(int color, float factor) {
        if (Float.compare(factor, 1.0F) == 0) {
            return color;
        } else {
            int r = color >> 16 & 255;
            int g = color >> 8 & 255;
            int b = color & 255;
            int i = (int)(1.0 / (1.0 - (double)factor));
            if (r == 0 && g == 0 && b == 0) {
                return color & -16777216 | (i & 255) << 16 | (i & 255) << 8 | i & 255;
            } else {
                if (r > 0 && r < i) {
                    r = i;
                }

                if (g > 0 && g < i) {
                    g = i;
                }

                if (b > 0 && b < i) {
                    b = i;
                }

                return color & -16777216 | Math.min(255, (int)((float)r / factor)) << 16 | Math.min(255, (int)((float)g / factor)) << 8 | Math.min(255, (int)((float)b / factor));
            }
        }
    }
}

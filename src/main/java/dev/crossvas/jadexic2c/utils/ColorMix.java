package dev.crossvas.jadexic2c.utils;

import ic2.core.utils.math.ColorUtils;

public enum ColorMix {
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
    public final int mono;
    public final int back;

    ColorMix(int color) {
        this.mono = color;
        this.aColor = color;
        this.bColor = ColorUtils.darker(color);
        this.back = ColorUtils.doubleDarker(color);
    }
}

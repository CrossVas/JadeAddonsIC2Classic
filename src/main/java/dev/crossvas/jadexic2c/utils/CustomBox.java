package dev.crossvas.jadexic2c.utils;

import snownee.jade.api.ui.BoxStyle;

public class CustomBox extends BoxStyle {

    public CustomBox(int borderColor, int backColor) {
        this.borderWidth = 1.0F;
        this.borderColor = borderColor;
        this.bgColor = backColor;
    }

    public CustomBox(int backColor) {
        this.borderWidth = 1.0F;
        this.borderColor = 0xFFFFFFFF;
        this.bgColor = backColor;
    }

    public static CustomBox BLACK_WHITE = new CustomBox(0xFFFFFFFF, 0xFF000000);

}

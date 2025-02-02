package ic2.jadeplugin.elements;

import snownee.jade.api.ui.BoxStyle;

@SuppressWarnings("all")
public class SpecialBoxStyle extends BoxStyle {

    public SpecialBoxStyle(int borderColor, int backColor) {
        this.borderWidth = 1.0F;
        this.borderColor = borderColor;
        this.bgColor = backColor;
    }

    public SpecialBoxStyle(int backColor) {
        this.borderWidth = 1.0F;
        this.borderColor = 0xFFFFFFFF;
        this.bgColor = backColor;
    }
}

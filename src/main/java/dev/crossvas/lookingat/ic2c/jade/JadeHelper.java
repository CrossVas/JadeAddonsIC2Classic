package dev.crossvas.lookingat.ic2c.jade;

import snownee.jade.api.ITooltip;

public class JadeHelper {

    public static void paddingY(ITooltip tooltip, int y) {
        tooltip.add(tooltip.getElementHelper().spacer(0, y));
    }
}

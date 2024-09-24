package dev.crossvas.waila.ic2.utils;

import mcp.mobius.waila.api.SpecialChars;
import net.minecraft.util.IChatComponent;

import java.util.List;

public interface IToolTipHelper {

    /**
     * Tooltip Builder
     * @param 4 - string only, 0 - false, 1 - true
     * @param 5 - centered, 0 - means false, 1 - true
     * @param 6 - texture info, leave 0 for nothing
     *
     * */

    default void bar(List<String> tooltip, int current, int max, int color, IChatComponent text, String textureInfo) {
        String[] barData = new String[7];
        barData[0] = String.valueOf(current);
        barData[1] = String.valueOf(max);
        barData[2] = String.valueOf(color);
        barData[3] = text.getFormattedText();
        barData[4] = "0";
        barData[5] = "0";
        barData[6] = textureInfo;
        tooltip.add(SpecialChars.getRenderString("jade.progress", barData));
    }

    default void text(List<String> tooltip, IChatComponent text, boolean centered) {
        String[] textData = new String[7];
        textData[0] = String.valueOf(1);
        textData[1] = String.valueOf(1);
        textData[2] = String.valueOf(1);
        textData[3] = text.getFormattedText();
        textData[4] = "1";
        if (centered) {
            textData[5] = "1";
        } else {
            textData[5] = "0";
        }
        textData[6] = "0";
        tooltip.add(SpecialChars.getRenderString("jade.progress", textData));
    }
}

package dev.crossvas.jadexic2c;

import dev.crossvas.jadexic2c.elements.SpecialBoxStyle;
import dev.crossvas.jadexic2c.elements.SpecialProgressStyle;
import ic2.core.utils.math.ColorUtils;
import net.minecraft.ChatFormatting;
import snownee.jade.Jade;
import snownee.jade.api.ui.BoxStyle;
import snownee.jade.api.ui.IProgressStyle;
import snownee.jade.impl.ui.ProgressStyle;

public class JadeCommonHelper {

    public static boolean forceTopStyle() {
        return Jade.CONFIG.get().getPlugin().get(JadePluginHandler.TOP_STYLE);
    }

    public static BoxStyle getStyle(int color) {
        return forceTopStyle() ? new SpecialBoxStyle(ColorUtils.doubleDarker(color)) : BoxStyle.DEFAULT;
    }

    public static IProgressStyle getProgressStyle(int color) {
        return forceTopStyle() ? new SpecialProgressStyle().color(color, ColorUtils.darker(color)) : new ProgressStyle().color(color, ColorUtils.darker(color));
    }

    public static ChatFormatting getFormattingStyle() {
        return forceTopStyle() ? ChatFormatting.WHITE : ChatFormatting.GRAY;
    }
}

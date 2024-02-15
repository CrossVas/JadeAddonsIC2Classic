package dev.crossvas.jadexic2c.helpers;

import dev.crossvas.jadexic2c.elements.SpecialBoxStyle;
import dev.crossvas.jadexic2c.elements.SpecialProgressStyle;
import dev.crossvas.jadexic2c.utils.ColorMix;
import ic2.core.utils.math.ColorUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import snownee.jade.api.ITooltip;

public class BarHelper {

    public static void bar(ITooltip tooltip, int current, int max, String text, ColorMix color) {
        tooltip.add(tooltip.getElementHelper().progress((float) current / max, Component.translatable(text).withStyle(ChatFormatting.WHITE), new SpecialProgressStyle().color(color.aColor, color.bColor), new SpecialBoxStyle(color.back), true));
    }

    public static void bar(ITooltip tooltip, int current, int max, Component text, ColorMix color) {
        tooltip.add(tooltip.getElementHelper().progress((float) current / max, text, new SpecialProgressStyle().color(color.aColor, color.bColor), new SpecialBoxStyle(color.back), true));
    }

    public static void bar(ITooltip tooltip, int current, int max, Component text, int mainColor) {
        tooltip.add(tooltip.getElementHelper().progress((float) current / max, text, new SpecialProgressStyle().color(mainColor, ColorUtils.darker(mainColor)), new SpecialBoxStyle(ColorUtils.doubleDarker(mainColor)), true));
    }

    public static void monoBar(ITooltip tooltip, int current, int max, Component text, ColorMix color) {
        tooltip.add(tooltip.getElementHelper().progress((float) current / max, text, new SpecialProgressStyle().color(color.mono, color.mono), new SpecialBoxStyle(color.back), true));
    }
}

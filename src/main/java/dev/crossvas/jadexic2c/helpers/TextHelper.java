package dev.crossvas.jadexic2c.helpers;

import dev.crossvas.jadexic2c.elements.SpecialTextElement;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import snownee.jade.api.ITooltip;
import snownee.jade.api.ui.Element;
import snownee.jade.api.ui.IElement;

public class TextHelper {

    public static void text(ITooltip tooltip, String text, Object... args) {
        tooltip.add(tooltip.getElementHelper().text(Component.translatable(text, args).withStyle(ChatFormatting.WHITE)));
    }

    public static void text(ITooltip tooltip, Component component) {
        IElement textElement = tooltip.getElementHelper().text(component);
        tooltip.add(textElement);
    }

    public static void text(ITooltip tooltip, Component component, IElement.Align align) {
        IElement textElement = tooltip.getElementHelper().text(component).align(align);
        tooltip.add(textElement);
    }

    public static void text(ITooltip tooltip, Component component, boolean centered) {
        Element textElement = new SpecialTextElement(component).centered(centered);
        tooltip.add(textElement);
    }

    public static void appendText(ITooltip tooltip, Component component) {
        tooltip.append(tooltip.getElementHelper().text(component));
    }

    public static void appendText(ITooltip tooltip, String text, ChatFormatting style, Object... args) {
        tooltip.append(tooltip.getElementHelper().text(Component.translatable(text, args).withStyle(style)));
    }
}

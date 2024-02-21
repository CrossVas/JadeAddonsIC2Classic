package dev.crossvas.jadexic2c.helpers;

import dev.crossvas.jadexic2c.elements.SpecialItemStackElement;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec2;
import snownee.jade.api.ITooltip;
import snownee.jade.api.ui.IElement;

import java.util.List;

public class PluginHelper {

    public static void spacerX(ITooltip tooltip, int x) {
        tooltip.add(tooltip.getElementHelper().spacer(x, 0));
    }

    public static void spacerY(ITooltip tooltip, int y) {
        tooltip.add(tooltip.getElementHelper().spacer(0, y));
    }

    public static void spacerXY(ITooltip tooltip, int x, int y) {
        tooltip.add(tooltip.getElementHelper().spacer(x, y));
    }

    public static void item(ITooltip tooltip, ItemStack stack, int x, int y, IElement.Align align, boolean centered) {
        tooltip.add(new SpecialItemStackElement(stack, align, x, y).centered(centered).size(new Vec2(0, 0)));
    }

    public static void itemCentered(ITooltip tooltip, ItemStack stack, int y) {
        tooltip.add(new SpecialItemStackElement(stack, IElement.Align.LEFT, 0, y).centered(true).size(new Vec2(0, 0)));
    }

    public static void grid(ITooltip iTooltip, String text, ChatFormatting style, List<ItemStack> stackList) {
        int counter = 0;
        if (!stackList.isEmpty()) {
            TextHelper.text(iTooltip, Component.translatable(text).withStyle(style));
            PluginHelper.spacerY(iTooltip, 2);
            for (ItemStack stack : stackList) {
                if (counter < 7) {
                    iTooltip.append(iTooltip.getElementHelper().item(stack));
                    counter++;
                    if (counter == 6) {
                        counter = 0;
                        TextHelper.text(iTooltip, Component.empty());
                    }
                }
            }
            PluginHelper.spacerY(iTooltip, 2);
        }
    }

    public static ChatFormatting getColor(int index) {
        return switch (index) {
            case 0 -> ChatFormatting.AQUA;
            case 1 -> ChatFormatting.RED;
            case 2 -> ChatFormatting.YELLOW;
            case 3 -> ChatFormatting.BLUE;
            case 4 -> ChatFormatting.LIGHT_PURPLE;
            case 5 -> ChatFormatting.GREEN;
            default -> ChatFormatting.WHITE;
        };
    }
}

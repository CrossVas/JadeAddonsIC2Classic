package dev.crossvas.jadexic2c.helpers;

import dev.crossvas.jadexic2c.elements.SpecialItemStackElement;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec2;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;
import snownee.jade.api.ITooltip;
import snownee.jade.api.ui.IElement;

import java.util.List;

public class PluginHelper {

    public static int getColorForFluid(FluidStack fluid) {
        return fluid.getFluid() == Fluids.LAVA ? -29925 : IClientFluidTypeExtensions.of(fluid.getFluid()).getTintColor() | -16777216;
    }

    public static void spacerX(ITooltip tooltip, int x, boolean append) {
        if (append) {
            tooltip.append(tooltip.getElementHelper().spacer(x, 0));
        } else {
            tooltip.add(tooltip.getElementHelper().spacer(x, 0));
        }
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
                        spacerY(iTooltip, 0);
                    }
                }
            }
            PluginHelper.spacerY(iTooltip, 2);
        }
    }

    public static void gridFluid(ITooltip iTooltip, String text, ChatFormatting style, List<FluidStack> stackList) {
        int counter = 0;
        if (!stackList.isEmpty()) {
            TextHelper.text(iTooltip, Component.translatable(text).withStyle(style));
            PluginHelper.spacerY(iTooltip, 2);
            for (FluidStack stack : stackList) {
                if (counter < 7) {
                    iTooltip.append(iTooltip.getElementHelper().fluid(stack));
                    spacerX(iTooltip, 3, true);
                    counter++;
                    if (counter == 6) {
                        counter = 0;
                        spacerY(iTooltip, 0);
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

    public static ChatFormatting getTextColorFromDropChance(int dropChance) {
        ChatFormatting color = ChatFormatting.GRAY;
        if (dropChance >= 90) {
            color = ChatFormatting.GREEN;
        }
        if (dropChance <= 90 && dropChance > 75) {
            color = ChatFormatting.YELLOW;
        }
        if (dropChance <= 75 && dropChance > 50) {
            color = ChatFormatting.GOLD;
        }
        if (dropChance <= 50 && dropChance > 35) {
            color = ChatFormatting.RED;
        }
        if (dropChance <= 35) {
            color = ChatFormatting.DARK_RED;
        }
        return color;
    }
}

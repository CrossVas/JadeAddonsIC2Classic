package dev.crossvas.jadexic2c.helpers;

import ic2.api.util.DirectionList;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;
import snownee.jade.api.ITooltip;

public class PluginHelper {

    public static Component getSides(DirectionList directionList) {
        Component component = Component.empty();
        if (directionList != null) {
            String[] sides = directionList.toString().replaceAll("\\[", "").replaceAll("]", "")
                    .replaceAll("north", ChatFormatting.YELLOW + "N")
                    .replaceAll("south", ChatFormatting.BLUE + "S")
                    .replaceAll("east", ChatFormatting.GREEN + "E")
                    .replaceAll("west", ChatFormatting.LIGHT_PURPLE + "W")
                    .replaceAll("down", ChatFormatting.AQUA + "D")
                    .replaceAll("up", ChatFormatting.RED + "U").split(",", -1);

            for (String side : sides) {
                component = component.copy().append(side);
            }
            return component;
        }
        return component;
    }

    public static int getColorForFluid(FluidStack fluid) {
        return fluid.getFluid() == Fluids.LAVA ? -29925 : IClientFluidTypeExtensions.of(fluid.getFluid()).getTintColor() | -16777216;
    }

    public static void spacerY(ITooltip tooltip, int y) {
        tooltip.add(tooltip.getElementHelper().spacer(0, y));
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

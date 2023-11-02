package dev.crossvas.jadexic2c.utils;

import dev.crossvas.jadexic2c.utils.removals.TankRender;
import ic2.core.utils.math.ColorUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.ITooltip;
import snownee.jade.api.ui.IElementHelper;
import snownee.jade.api.ui.IProgressStyle;

import java.util.List;

public class Helpers {

    public static void space_y(ITooltip tooltip, int y) {
        tooltip.add(tooltip.getElementHelper().spacer(1, y));
    }

    public static void barLiteral(ITooltip tooltip, int current, int max, Component text, ColorMix color) {
        tooltip.add(tooltip.getElementHelper().progress((float) current / max, text, new CustomStyle().color(color.aColor, color.bColor), new CustomBox(color.back), true));
    }

    public static void barLiteral(ITooltip tooltip, int current, int max, Component text, int mainColor) {
        tooltip.add(tooltip.getElementHelper().progress((float) current / max, text, new CustomStyle().color(mainColor, ColorUtils.darker(mainColor)), new CustomBox(ColorUtils.doubleDarker(mainColor)), true));
    }

    public static void barLiteral(ITooltip tooltip, int current, int max, String text, ColorMix color) {
        tooltip.add(tooltip.getElementHelper().progress((float) current / max, Component.literal(text).withStyle(ChatFormatting.WHITE), new CustomStyle().color(color.aColor, color.bColor), new CustomBox(color.back), true));
    }

    public static void bar(ITooltip tooltip, int current, int max, String text, ColorMix color) {
        tooltip.add(tooltip.getElementHelper().progress((float) current / max, Component.translatable(text, current, max).withStyle(ChatFormatting.WHITE), new CustomStyle().color(color.aColor, color.bColor), new CustomBox(color.back), true));
    }

    public static void monoBarLiteral(ITooltip tooltip, int current, int max, Component text, ColorMix color) {
        tooltip.add(tooltip.getElementHelper().progress((float) current / max, text, new CustomStyle().color(color.mono, color.mono), new CustomBox(color.back), true));
    }

    public static void monoBar(ITooltip tooltip, int current, int max, String text, ColorMix color) {
        tooltip.add(tooltip.getElementHelper().progress((float) current / max, Component.translatable(text, current, max).withStyle(ChatFormatting.WHITE), new CustomStyle().color(color.mono, color.mono), new CustomBox(color.back), true));
    }

    public static void text(ITooltip tooltip, Component component) {
        tooltip.add(tooltip.getElementHelper().text(component.copy()));
    }

    public static void text(ITooltip tooltip, String text, Object... args) {
        tooltip.add(tooltip.getElementHelper().text(Component.translatable(text, args).withStyle(ChatFormatting.WHITE)));
    }

    public static void appendText(ITooltip tooltip, Component component) {
        tooltip.append(tooltip.getElementHelper().text(component.copy()));
    }

    public static void appendText(ITooltip tooltip, String text, Object... args) {
        tooltip.append(tooltip.getElementHelper().text(Component.translatable(text, args).withStyle(ChatFormatting.WHITE)));
    }

    public static void grid(ITooltip iTooltip, String text, ChatFormatting style, List<ItemStack> stackList) {
        int counter = 0;
        if (!stackList.isEmpty()) {
            Helpers.text(iTooltip, Component.translatable(text).withStyle(style));
            Helpers.space_y(iTooltip, 3);
            for (ItemStack stack : stackList) {
                if (counter < 7) {
                    iTooltip.append(iTooltip.getElementHelper().item(stack));
                    counter++;
                    if (counter == 6) {
                        counter = 0;
                        Helpers.text(iTooltip, "");
                    }
                }
            }
        }
    }

    public static void loadTankData(CompoundTag serverData, BlockEntity entity) {
        if (entity instanceof IFluidHandler fluidHandler) {
            loadTankData(fluidHandler, serverData);
        } else {
            entity.getCapability(ForgeCapabilities.FLUID_HANDLER).ifPresent( handler -> loadTankData(handler, serverData));
        }
    }

    public static void loadTankData(IFluidHandler fluidHandler, CompoundTag serverData) {
        ListTag fluidList = new ListTag();
        for (int i = 0; i < fluidHandler.getTanks(); i++) {
            FluidStack fluid = fluidHandler.getFluidInTank(i);
            CompoundTag tankData = fluid.writeToNBT(new CompoundTag());
            tankData.putInt("tankCapacity", fluidHandler.getTankCapacity(i));
            fluidList.add(tankData);
        }
        if (!fluidList.isEmpty()) {
            serverData.put("TanksData", fluidList);
        }
    }

    public static void addClientTankFromTag(ITooltip tooltip, BlockAccessor accessor) {
        if (accessor.getServerData().contains("TanksData")) {
            TankRender.TANK_REMOVAL.add(accessor.getBlock());
            IElementHelper helper = tooltip.getElementHelper();
            ListTag tankTags = accessor.getServerData().getList("TanksData", Tag.TAG_COMPOUND);
            for (Tag tankTag : tankTags) {
                CompoundTag tank = (CompoundTag) tankTag;
                int capacity = tank.getInt("tankCapacity");
                FluidStack fluidStack = FluidStack.loadFluidStackFromNBT(tank);
                if (fluidStack.getAmount() > 0) {
                    IProgressStyle progressStyle = helper.progressStyle().overlay(helper.fluid(fluidStack));
                    tooltip.add(helper.progress((float) fluidStack.getAmount() / capacity, Component.translatable("ic2.barrel.info.fluid", fluidStack.getDisplayName(), Formatter.formatInt(fluidStack.getAmount(), String.valueOf(capacity).length() - 1), capacity / 1000).withStyle(ChatFormatting.WHITE), progressStyle,
                            new CustomBox(fluidStack.getFluid() == Fluids.LAVA ? ColorUtils.doubleDarker(-29925) : ColorUtils.doubleDarker(IClientFluidTypeExtensions.of(fluidStack.getFluid()).getTintColor())), false));
                }
            }
        }
    }
}

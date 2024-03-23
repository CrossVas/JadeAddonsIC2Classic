package dev.crossvas.jadexic2c.helpers;

import dev.crossvas.jadexic2c.elements.SpecialBoxStyle;
import dev.crossvas.jadexic2c.info.removals.TankRender;
import ic2.core.utils.math.ColorUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
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

public class TankHelper {

    public static void addTank(ITooltip tooltip, FluidStack fluidStack, int capacity) {
        IElementHelper helper = tooltip.getElementHelper();
        IProgressStyle progressStyle = helper.progressStyle().overlay(helper.fluid(fluidStack));
        tooltip.add(helper.progress((float) fluidStack.getAmount() / capacity, Component.translatable("ic2.barrel.info.fluid", fluidStack.getDisplayName(), Formatter.formatInt(fluidStack.getAmount(), String.valueOf(capacity).length() - 1), capacity / 1000).withStyle(ChatFormatting.WHITE), progressStyle,
                new SpecialBoxStyle(fluidStack.getFluid() == Fluids.LAVA ? ColorUtils.doubleDarker(-29925) : ColorUtils.doubleDarker(IClientFluidTypeExtensions.of(fluidStack.getFluid()).getTintColor())), true));
    }

    public static void loadTankData(CompoundTag serverData, BlockEntity entity) {
        if (entity instanceof IFluidHandler fluidHandler) {
            loadTankData(fluidHandler, serverData);
        } else {
            entity.getCapability(ForgeCapabilities.FLUID_HANDLER).ifPresent(handler -> loadTankData(handler, serverData));
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
                    tooltip.add(helper.progress((float) fluidStack.getAmount() / capacity, Component.translatable("ic2.barrel.info.fluid", fluidStack.getDisplayName(), Formatter.formatNumber(fluidStack.getAmount(), String.valueOf(fluidStack.getAmount()).length() - 1), Formatter.formatNumber(capacity, String.valueOf(capacity).length() - 1)).withStyle(ChatFormatting.WHITE), progressStyle,
                            new SpecialBoxStyle(fluidStack.getFluid() == Fluids.LAVA ? ColorUtils.doubleDarker(-29925) : ColorUtils.doubleDarker(IClientFluidTypeExtensions.of(fluidStack.getFluid()).getTintColor())), false));
                }
            }
        }
    }
}

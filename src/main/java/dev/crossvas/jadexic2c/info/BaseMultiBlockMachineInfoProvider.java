package dev.crossvas.jadexic2c.info;

import dev.crossvas.jadexic2c.JadeIC2CPluginHandler;
import dev.crossvas.jadexic2c.helpers.BarHelper;
import dev.crossvas.jadexic2c.helpers.IHelper;
import dev.crossvas.jadexic2c.helpers.TankHelper;
import dev.crossvas.jadexic2c.helpers.TextHelper;
import dev.crossvas.jadexic2c.utils.ColorMix;
import ic2.api.energy.EnergyNet;
import ic2.core.block.base.tiles.BaseMultiElectricTileEntity;
import ic2.core.block.base.tiles.BaseTileEntity;
import ic2.core.block.base.tiles.impls.machine.multi.BaseMultiMachineTileEntity;
import ic2.core.block.machines.tiles.hv.PressureAlloyFurnaceTileEntity;
import ic2.core.utils.math.ColorUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

import java.text.DecimalFormat;

public enum BaseMultiBlockMachineInfoProvider implements IHelper<BlockEntity> {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (!shouldAddInfo(blockAccessor, "BaseMultBlockInfo")) {
            return;
        }

        CompoundTag tag = getData(blockAccessor, "BaseMultBlockInfo");
        if (blockAccessor.getBlockEntity() instanceof BaseTileEntity tile) {
            // check for multiblock tiles
            if (tile instanceof BaseMultiMachineTileEntity multiTile) {
                addMultiInfo(multiTile, iTooltip, tag);
                TankHelper.addClientTankFromTag(iTooltip, blockAccessor);
            }
        }
    }

    public static void addMultiInfo(BaseMultiMachineTileEntity blockEntity, ITooltip iTooltip, CompoundTag tag) {
        TextHelper.text(iTooltip, "ic2.probe.eu.tier.name", EnergyNet.INSTANCE.getDisplayTier(blockEntity.getTier()));
        TextHelper.text(iTooltip, "ic2.probe.eu.max_in.name", blockEntity.getMaxInput());
        TextHelper.text(iTooltip, "ic2.probe.eu.usage.name", tag.getInt("energyPerTick"));

        if (blockEntity instanceof PressureAlloyFurnaceTileEntity furnace) {
            int speed = tag.getInt("speed");
            int maxSpeed = furnace.getMaxSpeed();
            Component speedName = furnace.getSpeedName();
            double scaledProgress = (double) speed / maxSpeed;
            if (speed > 0) {
                BarHelper.bar(iTooltip, speed, maxSpeed, speedName.plainCopy().append(": " + new DecimalFormat().format(scaledProgress * 100.0) + "%").withStyle(ChatFormatting.WHITE), ColorMix.ORANGE);
            }
        }

        if (!blockEntity.isValid) {
            long time = blockEntity.clockTime(512);
            BarHelper.bar(iTooltip, (int) time, 512, Component.literal("Next Reform: ").append(String.valueOf(512 - time)).append(" Ticks").withStyle(ChatFormatting.WHITE), ColorUtils.GRAY);
        }
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, ServerPlayer serverPlayer, Level level, BlockEntity blockEntity, boolean b) {
        if (blockEntity instanceof BaseMultiElectricTileEntity tile) {
            if (tile instanceof BaseMultiMachineTileEntity multiTile) {
                CompoundTag tag = new CompoundTag();
                TankHelper.loadTankData(compoundTag, multiTile);
                tag.putInt("energyPerTick", multiTile.getEnergyPerTick());

                if (multiTile instanceof PressureAlloyFurnaceTileEntity furnace) {
                    tag.putInt("speed", furnace.getSpeed());
                }
                compoundTag.put("BaseMultBlockInfo", tag);
            }
        }
    }

    @Override
    public ResourceLocation getUid() {
        return JadeIC2CPluginHandler.EU_READER_INFO;
    }
}

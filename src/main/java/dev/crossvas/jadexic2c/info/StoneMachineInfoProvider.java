package dev.crossvas.jadexic2c.info;

import dev.crossvas.jadexic2c.JadeIC2CPluginHandler;
import dev.crossvas.jadexic2c.helpers.BarHelper;
import dev.crossvas.jadexic2c.helpers.IHelper;
import dev.crossvas.jadexic2c.helpers.TankHelper;
import dev.crossvas.jadexic2c.helpers.TextHelper;
import ic2.core.block.base.tiles.BaseInventoryTileEntity;
import ic2.core.block.machines.tiles.nv.StoneBasicMachineTileEntity;
import ic2.core.block.machines.tiles.nv.StoneCannerTileEntity;
import ic2.core.block.machines.tiles.nv.StoneWoodGassifierTileEntity;
import ic2.core.utils.helpers.Formatters;
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

public enum StoneMachineInfoProvider implements IHelper<BlockEntity> {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (!shouldAddInfo(blockAccessor, "StoneMachineInfo")) {
            return;
        }

        CompoundTag tag = getData(blockAccessor, "StoneMachineInfo");

        if (blockAccessor.getBlockEntity() instanceof BaseInventoryTileEntity tile) {

            int fuel;
            int maxFuel;
            float progress;
            float maxProgress;

            if (tile instanceof StoneBasicMachineTileEntity) {
                fuel = tag.getInt("stoneFuel");
                maxFuel = tag.getInt("stoneMaxFuel");
                progress = tag.getFloat("stoneProgress");
                maxProgress = tag.getFloat("stoneMaxProgress");
                if (fuel > 0) {
                    BarHelper.bar(iTooltip, fuel, maxFuel, Component.translatable("ic2.probe.fuel.storage.name").append(String.valueOf(fuel)), ColorUtils.DARK_GRAY);
                }
                if (progress > 0) {
                    BarHelper.bar(iTooltip, (int) progress, (int) maxProgress,
                            Component.translatable("ic2.probe.progress.full.name", (int) progress, (int) maxProgress).append(" t").withStyle(ChatFormatting.WHITE), ColorUtils.BLUE);
                }
            }

            if (tile instanceof StoneWoodGassifierTileEntity gas) {
                TextHelper.text(iTooltip, "ic2.probe.pump.pressure", 25);
                TextHelper.text(iTooltip, "ic2.probe.pump.amount", Formatters.EU_FORMAT.format(900L));
                fuel = tag.getInt("gasFuel");
                maxFuel = tag.getInt("gasMaxFuel");
                progress = tag.getFloat("gasProgress");
                maxProgress = tag.getFloat("gasMaxProgress");
                if (fuel > 0) {
                    BarHelper.bar(iTooltip, fuel, maxFuel, Component.translatable("ic2.probe.fuel.storage.name").append(String.valueOf(fuel)), ColorUtils.DARK_GRAY);
                }
                if (progress > 0) {
                    BarHelper.bar(iTooltip, (int) progress, (int) maxProgress,
                            Component.translatable("ic2.probe.progress.full.name", (int) progress, (int) maxProgress).append(" t").withStyle(ChatFormatting.WHITE), ColorUtils.BLUE);
                }
                TankHelper.addClientTankFromTag(iTooltip, blockAccessor);
            }

            if (tile instanceof StoneCannerTileEntity canner) {
                fuel = tag.getInt("canFuel");
                maxFuel = tag.getInt("canMaxFuel");
                progress = tag.getFloat("canProgress");
                maxProgress = tag.getFloat("canMaxProgress");
                if (fuel > 0) {
                    BarHelper.bar(iTooltip, fuel, maxFuel, Component.translatable("ic2.probe.fuel.storage.name").append(String.valueOf(fuel)), ColorUtils.DARK_GRAY);
                }
                if (progress > 0) {
                    BarHelper.bar(iTooltip, (int) progress, (int) maxProgress,
                            Component.translatable("ic2.probe.progress.full.name", (int) progress, (int) maxProgress).append(" t").withStyle(ChatFormatting.WHITE), ColorUtils.BLUE);
                }
            }
        }
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, ServerPlayer serverPlayer, Level level, BlockEntity blockEntity, boolean b) {
        if (blockEntity instanceof BaseInventoryTileEntity tile) {
            CompoundTag tag = new CompoundTag();
            if (tile instanceof StoneBasicMachineTileEntity stone) {
                tag.putInt("stoneFuel", stone.getFuel());
                tag.putInt("stoneMaxFuel", stone.getMaxFuel());
                tag.putFloat("stoneProgress", stone.getProgress());
                tag.putFloat("stoneMaxProgress", stone.getMaxProgress());
            } else if (tile instanceof StoneWoodGassifierTileEntity gas) {
                tag.putInt("gasFuel", gas.getFuel());
                tag.putInt("gasMaxFuel", gas.getMaxFuel());
                tag.putFloat("gasProgress", gas.getProgress());
                tag.putFloat("gasMaxProgress", gas.getMaxProgress());
                TankHelper.loadTankData(compoundTag, gas);
            } else if (tile instanceof StoneCannerTileEntity canner) {
                tag.putInt("canFuel", canner.getFuel());
                tag.putInt("canMaxFuel", canner.getMaxFuel());
                tag.putFloat("canProgress", canner.getProgress());
                tag.putFloat("canMaxProgress", canner.getMaxProgress());
            }
            compoundTag.put("StoneMachineInfo", tag);
        }
    }

    @Override
    public ResourceLocation getUid() {
        return JadeIC2CPluginHandler.EU_READER_INFO;
    }
}

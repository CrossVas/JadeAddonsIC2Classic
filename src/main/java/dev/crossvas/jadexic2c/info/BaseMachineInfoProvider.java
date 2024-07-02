package dev.crossvas.jadexic2c.info;

import dev.crossvas.jadexic2c.JadePluginHandler;
import dev.crossvas.jadexic2c.helpers.BarHelper;
import dev.crossvas.jadexic2c.base.interfaces.IHelper;
import dev.crossvas.jadexic2c.helpers.TankHelper;
import dev.crossvas.jadexic2c.helpers.TextHelper;
import ic2.api.energy.EnergyNet;
import ic2.core.block.base.tiles.impls.machine.single.BaseAdvMachineTileEntity;
import ic2.core.block.base.tiles.impls.machine.single.BaseMachineTileEntity;
import ic2.core.block.machines.tiles.lv.RareEarthExtractorTileEntity;
import ic2.core.block.machines.tiles.mv.RareEarthCentrifugeTileEntity;
import ic2.core.block.machines.tiles.mv.RefineryTileEntity;
import ic2.core.block.machines.tiles.mv.SlowGrinderTileEntity;
import ic2.core.block.machines.tiles.mv.VacuumCannerTileEntity;
import ic2.core.utils.helpers.Formatters;
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

public enum BaseMachineInfoProvider implements IHelper<BlockEntity> {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (!shouldAddInfo(blockAccessor, "BaseMachineInfo")) {
            return;
        }

        CompoundTag tag = getData(blockAccessor, "BaseMachineInfo");
        if (blockAccessor.getBlockEntity() instanceof BaseMachineTileEntity tile) {
            TextHelper.text(iTooltip, "ic2.probe.eu.tier.name", EnergyNet.INSTANCE.getDisplayTier(tile.getTier()));
            TextHelper.text(iTooltip, "ic2.probe.eu.max_in.name", tile.getMaxInput());
            TextHelper.text(iTooltip, "ic2.probe.eu.usage.name", tag.getInt("energyPerTick"));
            if (tile instanceof SlowGrinderTileEntity grinder) {
                TextHelper.text(iTooltip, "ic2.probe.scrap.chance.name", grinder.getChance(0.25F) * 100.0F);
            }
            if (tile instanceof RefineryTileEntity) {
                TankHelper.addClientTankFromTag(iTooltip, blockAccessor);
            }

            int speed;
            int maxSpeed;
            Component speedName;
            double scaledProgress;
            float progress = tag.getFloat("progress");
            float maxProgress = tag.getFloat("maxProgress");
            float progressPerTick = tag.getFloat("progressPerTick");

            if (tile instanceof BaseAdvMachineTileEntity advancedTile) {
                if (tag.getInt("speed") > 0) {
                    speed = tag.getInt("speed");
                    maxSpeed = advancedTile.getMaxSpeed();
                    speedName = advancedTile.getSpeedName();
                    scaledProgress = (double) speed / maxSpeed;
                    BarHelper.bar(iTooltip, speed, maxSpeed, speedName.plainCopy().append(": " + new DecimalFormat().format(scaledProgress * 100.0) + "%").withStyle(ChatFormatting.WHITE), -295680);
                }
            }
            if (tile instanceof VacuumCannerTileEntity vacuum) {
                if (tag.getInt("speed") > 0) {
                    speed = tag.getInt("speed");
                    maxSpeed = vacuum.getMaxSpeed();
                    speedName = vacuum.getSpeedName();
                    scaledProgress = (double) speed / maxSpeed;
                    BarHelper.bar(iTooltip, speed, maxSpeed, speedName.plainCopy().append(": " + new DecimalFormat().format(scaledProgress * 100.0) + "%").withStyle(ChatFormatting.WHITE), -295680);
                }
            }

            if (tile instanceof RareEarthExtractorTileEntity) {
                float material = tag.getFloat("material");
                if (material > 0.0F) {
                    BarHelper.bar(iTooltip, (int) material, 1000, Component.translatable("ic2.probe.progress.material.name", Formatters.EU_READER_FORMAT.format(material)), -5829955);
                }
            } else if (tile instanceof RareEarthCentrifugeTileEntity extractor) {
                float material = tag.getFloat("material");
                if (material > 0.0F) {
                    BarHelper.bar(iTooltip, (int) material, 1000, Component.translatable("ic2.probe.progress.material.name", Formatters.EU_READER_FORMAT.format(material)), -5829955);
                }

                if (tag.getInt("speed") > 0) {
                    speed = tag.getInt("speed");
                    maxSpeed = extractor.getMaxSpeed();
                    speedName = extractor.getSpeedName();
                    scaledProgress = (double) speed / maxSpeed;
                    BarHelper.bar(iTooltip, speed, maxSpeed, speedName.plainCopy().append(": " + new DecimalFormat().format(scaledProgress * 100.0) + "%").withStyle(ChatFormatting.WHITE), -295680);
                }
            }
            if (progress > 0.0F) {
                int scaledOp = (int) Math.min(6.0E7F, progress / progressPerTick);
                int scaledMaxOp = (int) Math.min(6.0E7F, maxProgress / progressPerTick);
                BarHelper.bar(iTooltip, scaledOp, scaledMaxOp, Component.translatable("ic2.probe.progress.full.name", scaledOp, scaledMaxOp).append(" t").withStyle(ChatFormatting.WHITE), -16733185);
            }
        }
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, ServerPlayer serverPlayer, Level level, BlockEntity blockEntity, boolean b) {
        CompoundTag tag = new CompoundTag();
        if (blockEntity instanceof BaseMachineTileEntity tile) {
            tag.putInt("energyPerTick", tile.getEnergyPerTick());
            tag.putFloat("progress", tile.getProgress());
            tag.putBoolean("isActive", tile.isActive());
            tag.putFloat("maxProgress", tile.getMaxProgress());
            tag.putFloat("progressPerTick", tile.progressPerTick);
            if (tile instanceof RefineryTileEntity refinery) {
                TankHelper.loadTankData(compoundTag, refinery);
            }
        }
        if (blockEntity instanceof BaseAdvMachineTileEntity tile) {
            tag.putInt("speed", tile.getSpeed());
        }
        if (blockEntity instanceof RareEarthExtractorTileEntity extractor) {
            tag.putFloat("material", extractor.materialProgress);
        } else if (blockEntity instanceof RareEarthCentrifugeTileEntity extractor) {
            tag.putFloat("material", extractor.materialProgress);
            tag.putInt("speed", extractor.getSpeed());
        }
        compoundTag.put("BaseMachineInfo", tag);
    }

    @Override
    public ResourceLocation getUid() {
        return JadePluginHandler.EU_READER_INFO;
    }

}

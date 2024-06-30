package dev.crossvas.jadexic2c.info;

import dev.crossvas.jadexic2c.JadeIC2CPluginHandler;
import dev.crossvas.jadexic2c.helpers.Formatter;
import dev.crossvas.jadexic2c.helpers.IHelper;
import dev.crossvas.jadexic2c.helpers.TextHelper;
import ic2.api.energy.EnergyNet;
import ic2.core.block.base.tiles.BaseInventoryTileEntity;
import ic2.core.block.generators.tiles.WindmillTileEntity;
import ic2.core.inventory.filter.SpecialFilters;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum WindmillGenInfoProvider implements IHelper<BlockEntity> {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (!shouldAddInfo(blockAccessor, "WindmillInfo", SpecialFilters.EU_READER)) {
            return;
        }

        CompoundTag tag = getData(blockAccessor, "WindmillInfo");

        if (blockAccessor.getBlockEntity() instanceof BaseInventoryTileEntity tile) {
            if (tile instanceof WindmillTileEntity windmill) {
                TextHelper.text(iTooltip, "ic2.probe.eu.tier.name", EnergyNet.INSTANCE.getDisplayTier(windmill.getSourceTier()));
                TextHelper.text(iTooltip, "ic2.probe.eu.output.current.name", Formatter.formatNumber((double) tag.getFloat("production"), 3));
                TextHelper.text(iTooltip, "ic2.probe.eu.output.max.name", windmill.getMaxEnergyOutput());
            }
        }
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, ServerPlayer serverPlayer, Level level, BlockEntity blockEntity, boolean b) {
        if (blockEntity instanceof BaseInventoryTileEntity tile) {
            if (tile instanceof WindmillTileEntity windmill) {
                CompoundTag tag = new CompoundTag();
                tag.putFloat("production", windmill.getEUProduction());
                compoundTag.put("WindmillInfo", tag);
            }
        }
    }

    @Override
    public ResourceLocation getUid() {
        return JadeIC2CPluginHandler.TOOLTIP_RENDERER;
    }
}

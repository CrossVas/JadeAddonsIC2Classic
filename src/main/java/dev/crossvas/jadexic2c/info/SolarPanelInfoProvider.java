package dev.crossvas.jadexic2c.info;

import dev.crossvas.jadexic2c.JadeIC2CPluginHandler;
import dev.crossvas.jadexic2c.helpers.Formatter;
import dev.crossvas.jadexic2c.helpers.IHelper;
import dev.crossvas.jadexic2c.helpers.TextHelper;
import ic2.api.energy.EnergyNet;
import ic2.core.block.base.tiles.BaseInventoryTileEntity;
import ic2.core.block.generators.tiles.SolarPanelTileEntity;
import ic2.core.inventory.filter.SpecialFilters;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum SolarPanelInfoProvider implements IHelper<BlockEntity> {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (!shouldAddInfo(blockAccessor, "SolarPanelInfo", SpecialFilters.EU_READER)) {
            return;
        }

        CompoundTag tag = getData(blockAccessor, "SolarPanelInfo");

        if (blockAccessor.getBlockEntity() instanceof BaseInventoryTileEntity tile) {
            if (tile instanceof SolarPanelTileEntity panel) {
                float production = tag.getFloat("production");
                int maxProduction = tag.getInt("maxProduction");
                TextHelper.text(iTooltip, "ic2.probe.eu.tier.name", EnergyNet.INSTANCE.getDisplayTier(panel.getSourceTier()));
                TextHelper.text(iTooltip, "ic2.probe.eu.output.current.name", Formatter.formatNumber(production, 3));
                TextHelper.text(iTooltip, "ic2.probe.eu.output.max.name", maxProduction);
            }
        }
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, ServerPlayer serverPlayer, Level level, BlockEntity blockEntity, boolean b) {
        if (blockEntity instanceof BaseInventoryTileEntity tile) {
            if (tile instanceof SolarPanelTileEntity solar) {
                CompoundTag tag = new CompoundTag();
                tag.putFloat("production", solar.getEUProduction());
                tag.putInt("maxProduction", solar.getMaxEnergyOutput());
                compoundTag.put("SolarPanelInfo", tag);
            }
        }
    }

    @Override
    public ResourceLocation getUid() {
        return JadeIC2CPluginHandler.TOOLTIP_RENDERER;
    }
}

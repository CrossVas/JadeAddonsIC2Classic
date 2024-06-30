package dev.crossvas.jadexic2c.info;

import dev.crossvas.jadexic2c.JadeIC2CPluginHandler;
import dev.crossvas.jadexic2c.helpers.IHelper;
import dev.crossvas.jadexic2c.helpers.TextHelper;
import ic2.api.energy.EnergyNet;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.tiles.readers.IEUStorage;
import ic2.core.block.base.tiles.BaseTileEntity;
import ic2.core.block.cables.luminator.ConstructionLightTileEntity;
import ic2.core.block.cables.luminator.LuminatorTileEntity;
import ic2.core.inventory.filter.SpecialFilters;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum LuminatorInfoProvider implements IHelper<BlockEntity> {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (!shouldAddInfo(blockAccessor, "LuminatorInfo", SpecialFilters.EU_READER)) {
            return;
        }

        CompoundTag tag = getData(blockAccessor, "LuminatorInfo");
        if (blockAccessor.getBlockEntity() instanceof BaseTileEntity tile) {
            if (tile instanceof LuminatorTileEntity || tile instanceof ConstructionLightTileEntity) {
                TextHelper.text(iTooltip, "ic2.probe.eu.tier.name", EnergyNet.INSTANCE.getDisplayTier(((IEUStorage) tile).getTier()));
                TextHelper.text(iTooltip, "ic2.probe.eu.max_in.name", EnergyNet.INSTANCE.getPowerFromTier(((IEnergySink) tile).getSinkTier()));
                TextHelper.text(iTooltip, "ic2.probe.eu.usage.name", 0.1);

            }
            if (tile instanceof LuminatorTileEntity) {
                int lightLevel = tag.getInt("lightLevel");
                TextHelper.text(iTooltip, "ic2.probe.luminator.light.name", lightLevel);
            }
            if (tile instanceof ConstructionLightTileEntity) {
                boolean isActive = tag.getBoolean("isActive");
                TextHelper.text(iTooltip, "ic2.probe.luminator.light.name", isActive ? 15 : 0);
            }
        }
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, ServerPlayer serverPlayer, Level level, BlockEntity blockEntity, boolean b) {
        if (blockEntity instanceof BaseTileEntity tile) {
            CompoundTag tag = new CompoundTag();
            if (tile instanceof LuminatorTileEntity luminator) {
                tag.putInt("lightLevel", luminator.getLightLevel());
            } else if (blockEntity instanceof ConstructionLightTileEntity lightTile) {
                tag.putBoolean("isActive", lightTile.isActive());
            }
            compoundTag.put("LuminatorInfo", tag);
        }
    }

    @Override
    public ResourceLocation getUid() {
        return JadeIC2CPluginHandler.TOOLTIP_RENDERER;
    }
}

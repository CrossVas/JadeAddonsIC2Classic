package dev.crossvas.jadexic2c.info;

import dev.crossvas.jadexic2c.JadeIC2CPluginHandler;
import dev.crossvas.jadexic2c.helpers.IHelper;
import dev.crossvas.jadexic2c.helpers.TextHelper;
import dev.crossvas.jadexic2c.helpers.Formatter;
import ic2.api.energy.EnergyNet;
import ic2.core.block.base.tiles.BaseTileEntity;
import ic2.core.block.generators.tiles.WaveGenTileEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum WaveGenInfoProvider implements IHelper<BlockEntity> {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (!shouldAddInfo(blockAccessor, "WaveGenInfo")) {
            return;
        }

        CompoundTag tag = getData(blockAccessor, "WaveGenInfo");

        if (blockAccessor.getBlockEntity() instanceof BaseTileEntity tile) {
            if (tile instanceof WaveGenTileEntity waveGen) {
                float production = tag.getFloat("production");
                TextHelper.text(iTooltip, "ic2.probe.eu.tier.name", EnergyNet.INSTANCE.getDisplayTier(waveGen.getSourceTier()));
                TextHelper.text(iTooltip, "ic2.probe.eu.output.current.name", Formatter.formatNumber((double) production, 5));
                TextHelper.text(iTooltip, "ic2.probe.eu.output.max.name", waveGen.getMaxEnergyOutput());
            }
        }
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, ServerPlayer serverPlayer, Level level, BlockEntity blockEntity, boolean b) {
        if (blockEntity instanceof BaseTileEntity tile) {
            if (tile instanceof WaveGenTileEntity waveGen) {
                CompoundTag tag = new CompoundTag();
                tag.putFloat("production", waveGen.getEUProduction());
                compoundTag.put("WaveGenInfo", tag);
            }
        }
    }

    @Override
    public ResourceLocation getUid() {
        return JadeIC2CPluginHandler.EU_READER_INFO;
    }
}

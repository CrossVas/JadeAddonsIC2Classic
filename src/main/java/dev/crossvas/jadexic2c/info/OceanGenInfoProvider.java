package dev.crossvas.jadexic2c.info;

import dev.crossvas.jadexic2c.IHelper;
import dev.crossvas.jadexic2c.JadeIC2CPluginHandler;
import dev.crossvas.jadexic2c.utils.ColorMix;
import dev.crossvas.jadexic2c.utils.Helpers;
import ic2.api.energy.EnergyNet;
import ic2.core.block.base.tiles.BaseTileEntity;
import ic2.core.block.generators.tiles.OceanGeneratorTileEntity;
import ic2.probeplugin.base.ProbePluginHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum OceanGenInfoProvider implements IHelper {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (!shouldAddInfo(blockAccessor, "OceanGenInfo")) {
            return;
        }

        CompoundTag tag = getData(blockAccessor, "OceanGenInfo");
        if (blockAccessor.getBlockEntity() instanceof BaseTileEntity tile) {
            if (tile instanceof OceanGeneratorTileEntity gen) {
                float production = tag.getFloat("production");
                Helpers.text(iTooltip, "ic2.probe.eu.tier.name", EnergyNet.INSTANCE.getDisplayTier(gen.getSourceTier()));
                Helpers.text(iTooltip, "ic2.probe.eu.output.current.name", ProbePluginHelper.formatNumber((double) production, 5));
                Helpers.text(iTooltip, "ic2.probe.eu.output.max.name", gen.getMaxEnergyOutput());

                Helpers.bar(iTooltip, Integer.parseInt(ProbePluginHelper.formatInt(tag.getInt("water"), 4)), 1000, "ic2.probe.water.full.name", ColorMix.BLUE);
                Helpers.bar(iTooltip, Integer.parseInt(ProbePluginHelper.formatInt(tag.getInt("coral"), 4)), 50, "ic2.probe.corals.full.name", ColorMix.PURPLE);
            }
        }
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, ServerPlayer serverPlayer, Level level, BlockEntity blockEntity, boolean b) {
        CompoundTag tag = new CompoundTag();
        if (blockEntity instanceof BaseTileEntity tile) {
            if (tile instanceof OceanGeneratorTileEntity gen) {
                tag.putFloat("production", gen.getEUProduction());
                tag.putInt("water", gen.waterFound);
                tag.putInt("coral", gen.coralsFound);
            }
        }
        compoundTag.put("OceanGenInfo", tag);
    }

    @Override
    public ResourceLocation getUid() {
        return JadeIC2CPluginHandler.EU_READER_INFO;
    }
}

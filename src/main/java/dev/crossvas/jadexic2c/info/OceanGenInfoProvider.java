package dev.crossvas.jadexic2c.info;

import dev.crossvas.jadexic2c.JadePluginHandler;
import dev.crossvas.jadexic2c.helpers.BarHelper;
import dev.crossvas.jadexic2c.helpers.Formatter;
import dev.crossvas.jadexic2c.base.interfaces.IHelper;
import dev.crossvas.jadexic2c.helpers.TextHelper;
import ic2.api.energy.EnergyNet;
import ic2.core.block.base.tiles.BaseTileEntity;
import ic2.core.block.generators.tiles.OceanGeneratorTileEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum OceanGenInfoProvider implements IHelper<BlockEntity> {
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
                TextHelper.text(iTooltip, "ic2.probe.eu.tier.name", EnergyNet.INSTANCE.getDisplayTier(gen.getSourceTier()));
                TextHelper.text(iTooltip, "ic2.probe.eu.output.current.name", Formatter.formatNumber((double) production, 5));
                TextHelper.text(iTooltip, "ic2.probe.eu.output.max.name", gen.getMaxEnergyOutput());

                int water = Integer.parseInt(Formatter.formatInt(tag.getInt("water"), 4));
                int coral = Integer.parseInt(Formatter.formatInt(tag.getInt("coral"), 4));

                BarHelper.bar(iTooltip, water, 1000, Component.translatable("ic2.probe.water.full.name", water, 1000), -16733185);
                BarHelper.bar(iTooltip, coral, 50, Component.translatable("ic2.probe.corals.full.name", coral, 50), -5829955);
            }
        }
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, ServerPlayer serverPlayer, Level level, BlockEntity blockEntity, boolean b) {
        if (blockEntity instanceof BaseTileEntity tile) {
            if (tile instanceof OceanGeneratorTileEntity gen) {
                CompoundTag tag = new CompoundTag();
                tag.putFloat("production", gen.getEUProduction());
                tag.putInt("water", gen.waterFound);
                tag.putInt("coral", gen.coralsFound);
                compoundTag.put("OceanGenInfo", tag);
            }
        }
    }

    @Override
    public ResourceLocation getUid() {
        return JadePluginHandler.EU_READER_INFO;
    }
}

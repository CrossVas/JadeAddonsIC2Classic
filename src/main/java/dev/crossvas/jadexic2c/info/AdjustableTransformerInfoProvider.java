package dev.crossvas.jadexic2c.info;

import dev.crossvas.jadexic2c.JadeIC2CPluginHandler;
import dev.crossvas.jadexic2c.helpers.IHelper;
import dev.crossvas.jadexic2c.helpers.PluginHelper;
import dev.crossvas.jadexic2c.helpers.TextHelper;
import ic2.api.energy.EnergyNet;
import ic2.core.block.base.tiles.BaseTileEntity;
import ic2.core.block.storage.tiles.transformer.AdjustableTransformerTileEntity;
import ic2.core.inventory.filter.SpecialFilters;
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

public enum AdjustableTransformerInfoProvider implements IHelper<BlockEntity> {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (!shouldAddInfo(blockAccessor, "AdjTransformerInfo", SpecialFilters.EU_READER)) {
            return;
        }

        CompoundTag tag = getData(blockAccessor, "AdjTransformerInfo");
        if (blockAccessor.getBlockEntity() instanceof BaseTileEntity tile) {
            if (tile instanceof AdjustableTransformerTileEntity transformer) {
                int energyPacket = tag.getInt("energyPacket");
                int packetCount = tag.getInt("packetCount");

                TextHelper.text(iTooltip, "ic2.probe.eu.max_in.name", EnergyNet.INSTANCE.getPowerFromTier(transformer.getSinkTier()));
                TextHelper.text(iTooltip, "ic2.probe.eu.output.max.name", energyPacket);
                TextHelper.text(iTooltip, "ic2.probe.transformer.packets.name", packetCount);

                long averageOut = tag.getLong("averageOut");
                long packetsOut = tag.getLong("packetsOut");

                if (averageOut > 0) {
                    PluginHelper.spacerY(iTooltip, 3);
                    TextHelper.text(iTooltip, Component.translatable("tooltip.item.ic2.eu_reader.cable_flow", Formatters.EU_FORMAT.format(averageOut)).withStyle(ChatFormatting.AQUA));
                    TextHelper.text(iTooltip, Component.translatable("tooltip.item.ic2.eu_reader.packet_flow", Formatters.EU_FORMAT.format(packetsOut)).withStyle(ChatFormatting.AQUA));
                }
            }
        }
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, ServerPlayer serverPlayer, Level level, BlockEntity blockEntity, boolean b) {
        if (blockEntity instanceof BaseTileEntity tile) {
            if (tile instanceof AdjustableTransformerTileEntity transformer) {
                CompoundTag tag = new CompoundTag();
                tag.putInt("energyPacket", transformer.energyPacket);
                tag.putInt("packetCount", transformer.packetCount);

                CableInfoProvider.EnergyContainer result = CableInfoProvider.getContainer(transformer);
                tag.putLong("averageOut", result.getAverageOut());
                tag.putLong("packetsOut", result.getPacketsOut());
                compoundTag.put("AdjTransformerInfo", tag);
            }
        }
    }

    @Override
    public ResourceLocation getUid() {
        return JadeIC2CPluginHandler.TOOLTIP_RENDERER;
    }
}

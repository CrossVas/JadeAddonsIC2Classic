package dev.crossvas.jadexic2c.info;

import dev.crossvas.jadexic2c.JadeIC2CPluginHandler;
import dev.crossvas.jadexic2c.helpers.IHelper;
import dev.crossvas.jadexic2c.helpers.PluginHelper;
import dev.crossvas.jadexic2c.helpers.TextHelper;
import ic2.core.block.base.tiles.BaseTileEntity;
import ic2.core.block.base.tiles.impls.BaseTransformerTileEntity;
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

public enum TransformerInfoProvider implements IHelper<BlockEntity> {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (!shouldAddInfo(blockAccessor, "TransformerInfo", SpecialFilters.EU_READER)) {
            return;
        }

        CompoundTag tag = getData(blockAccessor, "TransformerInfo");
        if (blockAccessor.getBlockEntity() instanceof BaseTileEntity tile) {
            if (tile instanceof BaseTransformerTileEntity) {
                int lowOutput = tag.getInt("lowOut");
                int highOutput = tag.getInt("highOut");
                boolean isActive = tag.getBoolean("isActive");

                TextHelper.text(iTooltip, Component.translatable("ic2.probe.transformer.inverted").withStyle(ChatFormatting.GOLD).
                        append((isActive ? ChatFormatting.GREEN : ChatFormatting.RED) + String.valueOf(isActive)));

                TextHelper.text(iTooltip, "ic2.probe.eu.max_in.name", isActive ? lowOutput : highOutput);
                TextHelper.text(iTooltip, "ic2.probe.eu.output.max.name", isActive ? highOutput : lowOutput);
                TextHelper.text(iTooltip, "ic2.probe.transformer.packets.name", isActive ? 1 : 4);

                long averageOut = tag.getLong("averageOut");
                long packetsOut = tag.getLong("packetsOut");

                if (averageOut > 0) {
                    PluginHelper.spacerY(iTooltip, 3);
                    TextHelper.text(iTooltip, Component.translatable("tooltip.item.ic2.eu_reader.cable_flow", Formatters.EU_FORMAT.format((long) averageOut)).withStyle(ChatFormatting.AQUA));
                    TextHelper.text(iTooltip, Component.translatable("tooltip.item.ic2.eu_reader.packet_flow", Formatters.EU_FORMAT.format((long) packetsOut)).withStyle(ChatFormatting.AQUA));
                }
            }
        }
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, ServerPlayer serverPlayer, Level level, BlockEntity blockEntity, boolean b) {
        if (blockEntity instanceof BaseTileEntity tile) {
            if (tile instanceof BaseTransformerTileEntity transformer) {
                CompoundTag tag = new CompoundTag();
                tag.putInt("lowOut", transformer.lowOutput);
                tag.putInt("highOut", transformer.highOutput);
                tag.putBoolean("isActive", transformer.isActive());

                CableInfoProvider.EnergyContainer result = CableInfoProvider.getContainer(transformer);
                tag.putLong("averageOut", result.getAverageOut());
                tag.putLong("packetsOut", result.getPacketsOut());
                compoundTag.put("TransformerInfo", tag);
            }
        }
    }

    @Override
    public ResourceLocation getUid() {
        return JadeIC2CPluginHandler.TOOLTIP_RENDERER;
    }
}

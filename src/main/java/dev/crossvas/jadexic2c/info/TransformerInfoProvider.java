package dev.crossvas.jadexic2c.info;

import dev.crossvas.jadexic2c.IHelper;
import dev.crossvas.jadexic2c.JadeIC2CPluginHandler;
import dev.crossvas.jadexic2c.utils.Helpers;
import ic2.core.block.base.tiles.BaseTileEntity;
import ic2.core.block.base.tiles.impls.BaseTransformerTileEntity;
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
        if (!shouldAddInfo(blockAccessor, "TransformerInfo")) {
            return;
        }

        CompoundTag tag = getData(blockAccessor, "TransformerInfo");
        if (blockAccessor.getBlockEntity() instanceof BaseTileEntity tile) {
            if (tile instanceof BaseTransformerTileEntity) {
                int lowOutput = tag.getInt("lowOut");
                int highOutput = tag.getInt("highOut");
                boolean isActive = tag.getBoolean("isActive");

                Helpers.text(iTooltip,"ic2.probe.eu.max_in.name", tile.isActive() ? lowOutput : highOutput);
                Helpers.text(iTooltip,"ic2.probe.eu.output.max.name", tile.isActive() ? highOutput : lowOutput);
                Helpers.text(iTooltip,"ic2.probe.transformer.packets.name", isActive ? 1 : 4);

                long averageOut = tag.getLong("averageOut");
                long packetsOut = tag.getLong("packetsOut");

                if (averageOut > 0) {
                    Helpers.text(iTooltip, Component.translatable("tooltip.item.ic2.eu_reader.cable_flow", Formatters.EU_FORMAT.format((long)averageOut)).withStyle(ChatFormatting.AQUA));
                    Helpers.text(iTooltip, Component.translatable("tooltip.item.ic2.eu_reader.packet_flow", Formatters.EU_FORMAT.format((long)packetsOut)).withStyle(ChatFormatting.AQUA));
                }
            }
        }
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, ServerPlayer serverPlayer, Level level, BlockEntity blockEntity, boolean b) {
        CompoundTag tag = new CompoundTag();
        if (blockEntity instanceof BaseTileEntity tile) {
            if (tile instanceof BaseTransformerTileEntity transformer) {
                tag.putInt("lowOut", transformer.lowOutput);
                tag.putInt("highOut", transformer.highOutput);
                tag.putBoolean("isActive", transformer.isActive());

                CableInfoProvider.EnergyContainer result = CableInfoProvider.getContainer(transformer);
                tag.putLong("averageOut", result.getAverageOut());
                tag.putLong("packetsOut", result.getPacketsOut());
            }
        }
        compoundTag.put("TransformerInfo", tag);
    }

    @Override
    public ResourceLocation getUid() {
        return JadeIC2CPluginHandler.EU_READER_INFO;
    }
}

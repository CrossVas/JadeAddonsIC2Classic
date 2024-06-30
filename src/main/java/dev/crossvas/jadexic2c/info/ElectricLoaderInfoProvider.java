package dev.crossvas.jadexic2c.info;

import dev.crossvas.jadexic2c.JadeIC2CPluginHandler;
import dev.crossvas.jadexic2c.helpers.IHelper;
import dev.crossvas.jadexic2c.helpers.PluginHelper;
import dev.crossvas.jadexic2c.helpers.TextHelper;
import ic2.api.energy.EnergyNet;
import ic2.core.block.base.tiles.BaseElectricTileEntity;
import ic2.core.block.base.tiles.impls.BaseElectricLoaderTileEntity;
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

public enum ElectricLoaderInfoProvider implements IHelper<BlockEntity> {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (!shouldAddInfo(blockAccessor, "ElectricLoaderInfo")) {
            return;
        }

        CompoundTag tag = getData(blockAccessor, "ElectricLoaderInfo");
        if (blockAccessor.getBlockEntity() instanceof BaseElectricTileEntity tile) {
            if (tile instanceof BaseElectricLoaderTileEntity loader) {
                TextHelper.text(iTooltip, "ic2.probe.eu.tier.name", EnergyNet.INSTANCE.getDisplayTier(loader.getSinkTier()));
                TextHelper.text(iTooltip, "ic2.probe.eu.max_in.name", loader.getMaxInput());

                int averageIn = tag.getInt("averageIn");
                int packetsIn = tag.getInt("packetsIn");
                if (averageIn > 0) {
                    PluginHelper.spacerY(iTooltip, 3);
                    TextHelper.text(iTooltip, Component.translatable("tooltip.item.ic2.eu_reader.cable_flow_in", Formatters.EU_FORMAT.format((long) averageIn)).withStyle(ChatFormatting.AQUA));
                    TextHelper.text(iTooltip, Component.translatable("tooltip.item.ic2.eu_reader.packet_flow_in", Formatters.EU_FORMAT.format((long) packetsIn)).withStyle(ChatFormatting.AQUA));
                }
            }
        }

    }

    @Override
    public void appendServerData(CompoundTag compoundTag, ServerPlayer serverPlayer, Level level, BlockEntity blockEntity, boolean b) {
        if (blockEntity instanceof BaseElectricTileEntity tile) {
            if (tile instanceof BaseElectricLoaderTileEntity loader) {
                CompoundTag tag = new CompoundTag();
                CableInfoProvider.EnergyContainer result = CableInfoProvider.getContainer(loader);
                tag.putInt("averageIn", result.getAverageIn());
                tag.putInt("packetsIn", result.getPacketsIn());
                compoundTag.put("ElectricLoaderInfo", tag);
            }
        }
    }

    @Override
    public ResourceLocation getUid() {
        return JadeIC2CPluginHandler.TOOLTIP_RENDERER;
    }
}

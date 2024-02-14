package dev.crossvas.jadexic2c.info;

import dev.crossvas.jadexic2c.IHelper;
import dev.crossvas.jadexic2c.JadeIC2CPluginHandler;
import dev.crossvas.jadexic2c.utils.Helpers;
import ic2.api.energy.EnergyNet;
import ic2.core.block.base.tiles.BaseInventoryTileEntity;
import ic2.core.block.base.tiles.impls.BaseElectricUnloaderTileEntity;
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

public enum ElectricUnloaderInfoProvider implements IHelper<BlockEntity> {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (!shouldAddInfo(blockAccessor, "ElectricUnloaderInfo")) {
            return;
        }

        CompoundTag tag = getData(blockAccessor, "ElectricUnloaderInfo");
        if (blockAccessor.getBlockEntity() instanceof BaseInventoryTileEntity tile) {
            if (tile instanceof BaseElectricUnloaderTileEntity unloader) {
                Helpers.text(iTooltip, "ic2.probe.eu.tier.name", EnergyNet.INSTANCE.getDisplayTier(unloader.getSourceTier()));
                Helpers.text(iTooltip, "ic2.probe.eu.output.max.name", unloader.getMaxEnergyOutput());
                Helpers.text(iTooltip, "ic2.probe.transformer.packets.name", 10);

                int averageOut = tag.getInt("averageOut");
                int packetsOut = tag.getInt("packetsOut");
                if (averageOut > 0) {
                    Helpers.text(iTooltip, Component.translatable("tooltip.item.ic2.eu_reader.cable_flow_out", Formatters.EU_FORMAT.format((long)averageOut)).withStyle(ChatFormatting.AQUA));
                    Helpers.text(iTooltip, Component.translatable("tooltip.item.ic2.eu_reader.packet_flow_out", Formatters.EU_FORMAT.format((long)packetsOut)).withStyle(ChatFormatting.AQUA));
                }
            }
        }

    }

    @Override
    public void appendServerData(CompoundTag compoundTag, ServerPlayer serverPlayer, Level level, BlockEntity blockEntity, boolean b) {
        if (blockEntity instanceof BaseInventoryTileEntity tile) {
            if (tile instanceof BaseElectricUnloaderTileEntity unloader) {
                CompoundTag tag = new CompoundTag();
                CableInfoProvider.EnergyContainer result = CableInfoProvider.getContainer(unloader);
                tag.putInt("averageOut", result.getAverageOut());
                tag.putInt("packetsOut", result.getPacketsOut());
                compoundTag.put("ElectricUnloaderInfo", tag);
            }
        }
    }

    @Override
    public ResourceLocation getUid() {
        return JadeIC2CPluginHandler.EU_READER_INFO;
    }
}

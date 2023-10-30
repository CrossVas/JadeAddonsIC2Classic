package dev.crossvas.jadexic2c.info;

import dev.crossvas.jadexic2c.IHelper;
import dev.crossvas.jadexic2c.JadeIC2CPluginHandler;
import dev.crossvas.jadexic2c.utils.ColorMix;
import dev.crossvas.jadexic2c.utils.Helpers;
import ic2.api.energy.EnergyNet;
import ic2.core.block.base.tiles.BaseInventoryTileEntity;
import ic2.core.block.base.tiles.impls.BaseBatteryStationTileEntity;
import ic2.core.utils.helpers.Formatters;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.apache.commons.lang3.time.DurationFormatUtils;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum BatteryStationInfoProvider implements IHelper {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (!shouldAddInfo(blockAccessor, "BatteryStationInfo")) {
            return;
        }

        CompoundTag tag = getData(blockAccessor, "BatteryStationInfo");
        if (blockAccessor.getBlockEntity() instanceof BaseInventoryTileEntity tile) {
            if (tile instanceof BaseBatteryStationTileEntity station) {
                Helpers.text(iTooltip,"ic2.probe.eu.tier.name", EnergyNet.INSTANCE.getDisplayTier(station.getSourceTier()));
                Helpers.text(iTooltip,"ic2.probe.eu.output.max.name", station.getMaxEnergyOutput());
                int averageOut = tag.getInt("averageOut");
                int packetsOut = tag.getInt("packetsOut");
                if (averageOut > 0) {
                    Helpers.text(iTooltip, Component.translatable("tooltip.item.ic2.eu_reader.cable_flow_out", Formatters.EU_FORMAT.format((long)averageOut)).withStyle(ChatFormatting.AQUA));
                    Helpers.text(iTooltip, Component.translatable("tooltip.item.ic2.eu_reader.packet_flow_out", Formatters.EU_FORMAT.format((long)packetsOut)).withStyle(ChatFormatting.AQUA));
                }

                int missingEnergy = tag.getInt("missingEnergy");

                if (missingEnergy > 0) {
                    int slots = 0;

                    int i;
                    for(i = 0; i < 16; ++i) {
                        if (!tile.getStackInSlot(i).isEmpty()) {
                            ++slots;
                        }
                    }

                    i = Math.min(station.averager.getAverage(), missingEnergy);
                    Helpers.monoBarLiteral(iTooltip, slots - station.getChargingSlots(), slots, Component.translatable("ic2.probe.discharging.eta.name",
                            DurationFormatUtils.formatDuration(i <= 0 ? 0L : (missingEnergy / i * 50L), "HH:mm:ss")).withStyle(ChatFormatting.WHITE), ColorMix.BLUE);

                }
            }
        }
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, ServerPlayer serverPlayer, Level level, BlockEntity blockEntity, boolean b) {
        CompoundTag tag = new CompoundTag();
        if (blockEntity instanceof BaseInventoryTileEntity tile) {
            if (tile instanceof BaseBatteryStationTileEntity station) {
                CableInfoProvider.EnergyContainer result = CableInfoProvider.getContainer(station);
                tag.putInt("averageOut", result.getAverageOut());
                tag.putInt("packetsOut", result.getPacketsOut());
                tag.putInt("missingEnergy", station.getMissingEnergy().getIntKey());
            }
        }
        compoundTag.put("BatteryStationInfo", tag);
    }

    @Override
    public ResourceLocation getUid() {
        return JadeIC2CPluginHandler.EU_READER_INFO;
    }
}

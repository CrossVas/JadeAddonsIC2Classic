package dev.crossvas.jadexic2c.info;

import dev.crossvas.jadexic2c.JadeIC2CPluginHandler;
import dev.crossvas.jadexic2c.helpers.BarHelper;
import dev.crossvas.jadexic2c.helpers.IHelper;
import dev.crossvas.jadexic2c.helpers.PluginHelper;
import dev.crossvas.jadexic2c.helpers.TextHelper;
import ic2.api.energy.EnergyNet;
import ic2.api.items.electric.ElectricItem;
import ic2.core.block.base.tiles.BaseInventoryTileEntity;
import ic2.core.block.base.tiles.impls.BaseBatteryStationTileEntity;
import ic2.core.utils.helpers.Formatters;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.apache.commons.lang3.time.DurationFormatUtils;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum BatteryStationInfoProvider implements IHelper<BlockEntity> {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (!shouldAddInfo(blockAccessor, "BatteryStationInfo")) {
            return;
        }

        CompoundTag tag = getData(blockAccessor, "BatteryStationInfo");
        if (blockAccessor.getBlockEntity() instanceof BaseInventoryTileEntity tile) {
            if (tile instanceof BaseBatteryStationTileEntity station) {
                TextHelper.text(iTooltip, "ic2.probe.eu.tier.name", EnergyNet.INSTANCE.getDisplayTier(station.getSourceTier()));
                TextHelper.text(iTooltip, "ic2.probe.eu.output.max.name", station.getMaxEnergyOutput());
                long missingEnergy = tag.getLong("missingEnergy");
                int maxTransfer = tag.getInt("maxTransfer");

                if (missingEnergy > 0) {
                    int chargeEnergy = (int) Math.min(maxTransfer, missingEnergy);
                    TextHelper.text(iTooltip, Component.translatable("ic2.probe.chargingBench.eta.name",
                            DurationFormatUtils.formatDuration(chargeEnergy <= 0 ? 0L : (missingEnergy / chargeEnergy * 50L), "HH:mm:ss")).withStyle(ChatFormatting.GOLD));
                }

                long maxCapacity = tag.getLong("maxCapacity");
                long capacity = tag.getLong("capacity");
                int averageIn = tag.getInt("averageIn");

                if (capacity > 0) {
                    int dischargeEnergy = (int) Math.min(averageIn, capacity);
                    BarHelper.bar(iTooltip, (int) capacity, (int) maxCapacity, Component.translatable("ic2.probe.discharging.eta.name",
                            DurationFormatUtils.formatDuration(dischargeEnergy <= 0 ? 0L : (capacity / dischargeEnergy * 50L), "HH:mm:ss")).withStyle(ChatFormatting.WHITE), -16733185);
                }

                int averageOut = tag.getInt("averageOut");
                int packetsOut = tag.getInt("packetsOut");
                if (averageOut > 0) {
                    PluginHelper.spacerY(iTooltip, 3);
                    TextHelper.text(iTooltip, Component.translatable("tooltip.item.ic2.eu_reader.cable_flow_out", Formatters.EU_FORMAT.format(averageOut)).withStyle(ChatFormatting.AQUA));
                    TextHelper.text(iTooltip, Component.translatable("tooltip.item.ic2.eu_reader.packet_flow_out", Formatters.EU_FORMAT.format(packetsOut)).withStyle(ChatFormatting.AQUA));
                }
            }
        }
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, ServerPlayer serverPlayer, Level level, BlockEntity blockEntity, boolean b) {
        if (blockEntity instanceof BaseInventoryTileEntity tile) {
            if (tile instanceof BaseBatteryStationTileEntity station) {
                CompoundTag tag = new CompoundTag();
                long maxCapacity = 0;
                for (int i = 0; i < station.getChargingSlots(); i++) {
                    ItemStack battery = station.getStackInSlot(i);
                    if (!battery.isEmpty()) {
                        maxCapacity += ElectricItem.MANAGER.getCapacity(battery);
                    }
                }

                ItemStack toCharge = station.getStackInSlot(16);
                long missingEnergy = ElectricItem.MANAGER.getCapacity(toCharge) - ElectricItem.MANAGER.getCharge(toCharge);

                tag.putLong("missingEnergy", missingEnergy);
                tag.putInt("maxTransfer", ElectricItem.MANAGER.getTransferLimit(toCharge));

                tag.putLong("maxCapacity", maxCapacity);
                tag.putLong("capacity", station.getMissingEnergy().getIntKey());
                tag.putInt("averageIn", station.getMissingEnergy().getIntValue());

                CableInfoProvider.EnergyContainer result = CableInfoProvider.getContainer(station);
                tag.putInt("averageOut", result.getAverageOut());
                tag.putInt("packetsOut", result.getPacketsOut());
                compoundTag.put("BatteryStationInfo", tag);
            }
        }
    }

    @Override
    public ResourceLocation getUid() {
        return JadeIC2CPluginHandler.EU_READER_INFO;
    }
}

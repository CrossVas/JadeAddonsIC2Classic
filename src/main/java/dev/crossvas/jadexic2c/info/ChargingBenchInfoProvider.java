package dev.crossvas.jadexic2c.info;

import dev.crossvas.jadexic2c.JadePluginHandler;
import dev.crossvas.jadexic2c.helpers.BarHelper;
import dev.crossvas.jadexic2c.helpers.IHelper;
import dev.crossvas.jadexic2c.helpers.PluginHelper;
import dev.crossvas.jadexic2c.helpers.TextHelper;
import ic2.api.energy.EnergyNet;
import ic2.api.items.electric.ElectricItem;
import ic2.core.block.base.tiles.BaseElectricTileEntity;
import ic2.core.block.base.tiles.impls.BaseChargingBenchTileEntity;
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

public enum ChargingBenchInfoProvider implements IHelper<BlockEntity> {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (!shouldAddInfo(blockAccessor, "ChargingBenchInfo")) {
            return;
        }

        CompoundTag tag = getData(blockAccessor, "ChargingBenchInfo");
        if (blockAccessor.getBlockEntity() instanceof BaseElectricTileEntity tile) {
            if (tile instanceof BaseChargingBenchTileEntity bench) {
                TextHelper.text(iTooltip, "ic2.probe.eu.tier.name", EnergyNet.INSTANCE.getDisplayTier(bench.getSinkTier()));
                TextHelper.text(iTooltip, "ic2.probe.eu.max_in.name", bench.getMaxInput());

                int missingEnergy = tag.getInt("missingEnergy");
                int benchAverageOut = tag.getInt("benchTransferOut");
                if (missingEnergy > 0) {
                    int i = Math.min(benchAverageOut, missingEnergy);
                    TextHelper.text(iTooltip, Component.translatable("ic2.probe.chargingBench.eta.name",
                            DurationFormatUtils.formatDuration(i <= 0 ? 0L : (missingEnergy / i * 50L), "HH:mm:ss")).withStyle(ChatFormatting.GOLD));
                }

                int toDischargeEnergy = tag.getInt("toDischarge");
                int transferLimit = tag.getInt("transferLimit");
                int maxCapacity = tag.getInt("maxCapacity");
                if (toDischargeEnergy > 0) {
                    int dischargeEnergy = Math.min(transferLimit, toDischargeEnergy);
                    BarHelper.bar(iTooltip, toDischargeEnergy, maxCapacity, Component.translatable("ic2.probe.discharging.eta.name",
                            DurationFormatUtils.formatDuration(dischargeEnergy <= 0 ? 0L : (toDischargeEnergy / dischargeEnergy * 50L), "HH:mm:ss")).withStyle(ChatFormatting.WHITE), -16733185);
                }

                int averageIn = tag.getInt("averageIn");
                int packetsIn = tag.getInt("packetsIn");
                if (averageIn > 0) {
                    PluginHelper.spacerY(iTooltip, 3);
                    TextHelper.text(iTooltip, Component.translatable("tooltip.item.ic2.eu_reader.cable_flow_in", Formatters.EU_FORMAT.format(averageIn)).withStyle(ChatFormatting.AQUA));
                    TextHelper.text(iTooltip, Component.translatable("tooltip.item.ic2.eu_reader.packet_flow_in", Formatters.EU_FORMAT.format(packetsIn)).withStyle(ChatFormatting.AQUA));
                }
            }
        }
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, ServerPlayer serverPlayer, Level level, BlockEntity blockEntity, boolean b) {
        if (blockEntity instanceof BaseElectricTileEntity tile) {
            if (tile instanceof BaseChargingBenchTileEntity bench) {
                CompoundTag tag = new CompoundTag();
                ItemStack battery = bench.getStackInSlot(16);
                tag.putInt("toDischarge", ElectricItem.MANAGER.getCharge(battery));
                tag.putInt("transferLimit", ElectricItem.MANAGER.getTransferLimit(battery));
                tag.putInt("maxCapacity", ElectricItem.MANAGER.getCapacity(battery));

                int missingEnergy = 0;
                for (int i = 0; i < 16; i++) {
                    ItemStack toCharge = bench.getStackInSlot(i);
                    if (!toCharge.isEmpty()) {
                        missingEnergy += (ElectricItem.MANAGER.getCapacity(toCharge) - ElectricItem.MANAGER.getCharge(toCharge));
                    }
                }

                tag.putInt("missingEnergy", missingEnergy);
                tag.putInt("benchTransferOut", bench.getMissingEnergy().getIntValue());
                CableInfoProvider.EnergyContainer result = CableInfoProvider.getContainer(bench);
                tag.putInt("averageIn", result.getAverageIn());
                tag.putInt("packetsIn", result.getPacketsIn());
                compoundTag.put("ChargingBenchInfo", tag);
            }
        }
    }

    @Override
    public ResourceLocation getUid() {
        return JadePluginHandler.EU_READER_INFO;
    }
}

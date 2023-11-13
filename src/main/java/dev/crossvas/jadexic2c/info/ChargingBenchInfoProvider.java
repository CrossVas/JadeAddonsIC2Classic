package dev.crossvas.jadexic2c.info;

import dev.crossvas.jadexic2c.IHelper;
import dev.crossvas.jadexic2c.JadeIC2CPluginHandler;
import dev.crossvas.jadexic2c.utils.ColorMix;
import dev.crossvas.jadexic2c.utils.Helpers;
import ic2.api.energy.EnergyNet;
import ic2.core.block.base.tiles.BaseElectricTileEntity;
import ic2.core.block.base.tiles.impls.BaseChargingBenchTileEntity;
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
                Helpers.text(iTooltip, "ic2.probe.eu.tier.name", EnergyNet.INSTANCE.getDisplayTier(bench.getSinkTier()));
                Helpers.text(iTooltip, "ic2.probe.eu.max_in.name", bench.getMaxInput());

                int averageIn = tag.getInt("averageIn");
                int packetsIn = tag.getInt("packetsIn");
                if (averageIn > 0) {
                    Helpers.text(iTooltip, Component.translatable("tooltip.item.ic2.eu_reader.cable_flow_in", Formatters.EU_FORMAT.format((long)averageIn)).withStyle(ChatFormatting.AQUA));
                    Helpers.text(iTooltip, Component.translatable("tooltip.item.ic2.eu_reader.packet_flow_in", Formatters.EU_FORMAT.format((long)packetsIn)).withStyle(ChatFormatting.AQUA));
                }

                int missingEnergy = tag.getInt("missingEnergy");
                if (missingEnergy > 0) {
                    int slots = 0;
                    int i;
                    for (i = 0; i < 16; ++i) {
                        if (!bench.getStackInSlot(i).isEmpty()) {
                            ++slots;
                        }
                    }

                    i = Math.min(bench.averager.getAverage(), missingEnergy);
                    Helpers.monoBarLiteral(iTooltip, slots - bench.getChargingSlots(), slots, Component.translatable("ic2.probe.chargingBench.eta.name",
                            DurationFormatUtils.formatDuration(i <= 0 ? 0L : (missingEnergy / i * 50L), "HH:mm:ss")).withStyle(ChatFormatting.WHITE), ColorMix.BLUE);
                }
            }
        }
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, ServerPlayer serverPlayer, Level level, BlockEntity blockEntity, boolean b) {
        CompoundTag tag = new CompoundTag();
        if (blockEntity instanceof BaseElectricTileEntity tile) {
            if (tile instanceof BaseChargingBenchTileEntity bench) {
                tag.putInt("missingEnergy", bench.getMissingEnergy().getIntKey());
                CableInfoProvider.EnergyContainer result = CableInfoProvider.getContainer(bench);
                tag.putInt("averageIn", result.getAverageIn());
                tag.putInt("packetsIn", result.getPacketsIn());
            }
        }
        compoundTag.put("ChargingBenchInfo", tag);
    }

    @Override
    public ResourceLocation getUid() {
        return JadeIC2CPluginHandler.EU_READER_INFO;
    }
}

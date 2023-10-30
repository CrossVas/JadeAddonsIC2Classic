package dev.crossvas.jadexic2c.info;

import dev.crossvas.jadexic2c.IHelper;
import dev.crossvas.jadexic2c.JadeIC2CPluginHandler;
import dev.crossvas.jadexic2c.utils.Helpers;
import ic2.api.energy.EnergyNet;
import ic2.core.block.base.tiles.BaseTileEntity;
import ic2.core.block.base.tiles.impls.BaseChargePadTileEntity;
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

public enum ChargePadInfoProvider implements IHelper {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (!canHandle(blockAccessor.getPlayer())) {
            return;
        }
        if (!blockAccessor.getServerData().contains("ChargePadInfo")) {
            return;
        }

        CompoundTag tag = blockAccessor.getServerData().getCompound("ChargePadInfo");

        if (blockAccessor.getBlockEntity() instanceof BaseTileEntity tile) {
            if (tile instanceof BaseChargePadTileEntity pad) {
                int maxInput = tag.getInt("maxInput");
                int transferLimit = tag.getInt("transferLimit");
                float range = tag.getFloat("range");

                Helpers.text(iTooltip,"ic2.probe.eu.tier.name", EnergyNet.INSTANCE.getDisplayTier(pad.getSinkTier()));
                Helpers.text(iTooltip,"ic2.probe.eu.max_in.name", maxInput);
                Helpers.text(iTooltip,"ic2.probe.chargepad.transferrate.name", transferLimit);
                Helpers.text(iTooltip,"ic2.probe.chargepad.radius.name", range + 1.0F);

                int averageIn = tag.getInt("averageIn");
                int packetsIn = tag.getInt("packetsIn");
                if (averageIn > 0) {
                    Helpers.text(iTooltip, Component.translatable("tooltip.item.ic2.eu_reader.cable_flow_in", Formatters.EU_FORMAT.format((long)averageIn)).withStyle(ChatFormatting.AQUA));
                    Helpers.text(iTooltip, Component.translatable("tooltip.item.ic2.eu_reader.packet_flow_in", Formatters.EU_FORMAT.format((long)packetsIn)).withStyle(ChatFormatting.AQUA));
                }
            }
        }
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, ServerPlayer serverPlayer, Level level, BlockEntity blockEntity, boolean b) {
        CompoundTag tag = new CompoundTag();
        if (blockEntity instanceof BaseTileEntity tile) {
            if (tile instanceof BaseChargePadTileEntity pad) {
                tag.putInt("maxInput", pad.maxInput);
                tag.putInt("transferLimit", pad.transferLimit);
                tag.putFloat("range", pad.range);
                CableInfoProvider.EnergyContainer result = CableInfoProvider.getContainer(pad);
                tag.putInt("averageIn", result.getAverageIn());
                tag.putInt("packetsIn", result.getPacketsIn());
            }
        }
        compoundTag.put("ChargePadInfo", tag);
    }

    @Override
    public ResourceLocation getUid() {
        return JadeIC2CPluginHandler.EU_READER_INFO;
    }
}

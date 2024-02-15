package dev.crossvas.jadexic2c.info;

import dev.crossvas.jadexic2c.helpers.IHelper;
import dev.crossvas.jadexic2c.JadeIC2CPluginHandler;
import dev.crossvas.jadexic2c.helpers.PluginHelper;
import dev.crossvas.jadexic2c.helpers.TextHelper;
import ic2.api.util.DirectionList;
import ic2.core.block.storage.tiles.RedirectorMasterTileEntity;
import ic2.core.block.storage.tiles.RedirectorSlaveTileEntity;
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

public enum RedirectorSlaveInfoProvider implements IHelper<BlockEntity> {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (!shouldAddInfo(blockAccessor, "RedirectorSlaveInfo")) {
            return;
        }

        CompoundTag tag = getData(blockAccessor, "RedirectorSlaveInfo");

        if (blockAccessor.getBlockEntity() instanceof RedirectorSlaveTileEntity slave) {
            BlockEntity blockEntity = DirectionList.getNeighborTile(slave, slave.getFacing());
            if (blockEntity instanceof RedirectorMasterTileEntity master) {
                TextHelper.text(iTooltip, "ic2.probe.redirector.slave.info", master.shares[slave.getFacing().getOpposite().get3DDataValue()]);
            }

            int averageOut = tag.getInt("averageOut");
            int packetsOut = tag.getInt("packetsOut");

            if (averageOut > 0) {
                PluginHelper.spacerY(iTooltip, 5);
                TextHelper.text(iTooltip, Component.translatable("tooltip.item.ic2.eu_reader.cable_flow", Formatters.EU_FORMAT.format((long)averageOut)).withStyle(ChatFormatting.AQUA));
                TextHelper.text(iTooltip, Component.translatable("tooltip.item.ic2.eu_reader.packet_flow", Formatters.EU_FORMAT.format((long)packetsOut)).withStyle(ChatFormatting.AQUA));
            }
        }
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, ServerPlayer serverPlayer, Level level, BlockEntity blockEntity, boolean b) {
        if (blockEntity instanceof RedirectorSlaveTileEntity slave) {
            CompoundTag tag = new CompoundTag();
            CableInfoProvider.EnergyContainer result = CableInfoProvider.getContainer(slave);
            tag.putInt("averageOut", result.getAverageOut());
            tag.putInt("packetsOut", result.getPacketsOut());
            compoundTag.put("RedirectorSlaveInfo", tag);
        }
    }

    @Override
    public ResourceLocation getUid() {
        return JadeIC2CPluginHandler.EU_READER_INFO;
    }
}

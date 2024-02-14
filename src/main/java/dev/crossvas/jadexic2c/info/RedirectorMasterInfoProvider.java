package dev.crossvas.jadexic2c.info;

import dev.crossvas.jadexic2c.IHelper;
import dev.crossvas.jadexic2c.JadeIC2CPluginHandler;
import dev.crossvas.jadexic2c.utils.Helpers;
import ic2.api.util.DirectionList;
import ic2.core.block.storage.tiles.RedirectorMasterTileEntity;
import ic2.core.utils.helpers.Formatters;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum RedirectorMasterInfoProvider implements IHelper<BlockEntity> {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (!shouldAddInfo(blockAccessor, "RedirectorMasterInfo")) {
            return;
        }

        CompoundTag tag = getData(blockAccessor, "RedirectorMasterInfo");

        if (blockAccessor.getBlockEntity() instanceof RedirectorMasterTileEntity master) {
            for (Direction side : DirectionList.ALL) {
                int value = master.shares[side.get3DDataValue()];
                if (value > 0) {
                    Helpers.text(iTooltip, DirectionList.getName(side).append(": " + value + "%").withStyle(ChatFormatting.WHITE));
                }
            }

            int averageIn = tag.getInt("averageIn");
            int packetsIn = tag.getInt("packetsIn");

            if (averageIn > 0) {
                Helpers.space_y(iTooltip, 5);
                Helpers.text(iTooltip, Component.translatable("tooltip.item.ic2.eu_reader.cable_flow", Formatters.EU_FORMAT.format((long)averageIn)).withStyle(ChatFormatting.AQUA));
                Helpers.text(iTooltip, Component.translatable("tooltip.item.ic2.eu_reader.packet_flow", Formatters.EU_FORMAT.format((long)packetsIn)).withStyle(ChatFormatting.AQUA));
            }
        }
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, ServerPlayer serverPlayer, Level level, BlockEntity blockEntity, boolean b) {
        if (blockEntity instanceof RedirectorMasterTileEntity master) {
            CompoundTag tag = new CompoundTag();
            CableInfoProvider.EnergyContainer result = CableInfoProvider.getContainer(master);
            tag.putInt("averageIn", result.getAverageIn());
            tag.putInt("packetsIn", result.getPacketsIn());
            compoundTag.put("RedirectorMasterInfo", tag);
        }
    }

    @Override
    public ResourceLocation getUid() {
        return JadeIC2CPluginHandler.EU_READER_INFO;
    }
}

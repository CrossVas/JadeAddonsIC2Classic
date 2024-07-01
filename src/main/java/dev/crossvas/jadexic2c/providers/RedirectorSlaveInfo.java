package dev.crossvas.jadexic2c.providers;

import dev.crossvas.jadexic2c.base.IInfoProvider;
import dev.crossvas.jadexic2c.base.IJadeHelper;
import dev.crossvas.jadexic2c.helpers.EnergyContainer;
import ic2.api.util.DirectionList;
import ic2.core.block.storage.tiles.RedirectorMasterTileEntity;
import ic2.core.block.storage.tiles.RedirectorSlaveTileEntity;
import ic2.core.utils.helpers.Formatters;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class RedirectorSlaveInfo implements IInfoProvider {

    public static final RedirectorSlaveInfo THIS = new RedirectorSlaveInfo();

    @Override
    public void addInfo(IJadeHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof RedirectorSlaveTileEntity slave) {
            BlockEntity neighborTile = DirectionList.getNeighborTile(slave, slave.getFacing());
            if (neighborTile instanceof RedirectorMasterTileEntity master) {
                text(helper, "ic2.probe.redirector.slave.info", master.shares[slave.getFacing().getOpposite().get3DDataValue()]);
            }

            EnergyContainer averages = EnergyContainer.getContainer(slave);
            int avrOut = averages.getAverageOut();
            int pOut = averages.getPacketsOut();

            if (avrOut > 0) {
                helper.addPaddingElement(0, 3);
                text(helper, ChatFormatting.AQUA, Component.translatable("tooltip.item.ic2.eu_reader.cable_flow", Formatters.EU_FORMAT.format(avrOut)));
                text(helper, ChatFormatting.AQUA, Component.translatable("tooltip.item.ic2.eu_reader.packet_flow", Formatters.EU_FORMAT.format(pOut)));
            }
        }
    }
}

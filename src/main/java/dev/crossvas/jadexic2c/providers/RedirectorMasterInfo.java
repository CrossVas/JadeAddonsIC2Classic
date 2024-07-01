package dev.crossvas.jadexic2c.providers;

import dev.crossvas.jadexic2c.base.IJadeHelper;
import dev.crossvas.jadexic2c.helpers.EnergyContainer;
import ic2.api.util.DirectionList;
import ic2.core.block.storage.tiles.RedirectorMasterTileEntity;
import ic2.core.utils.helpers.Formatters;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class RedirectorMasterInfo extends EUReaderInfoProvider {

    public static final RedirectorMasterInfo THIS = new RedirectorMasterInfo();

    @Override
    public void addInfo(IJadeHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof RedirectorMasterTileEntity master) {
            for (Direction side : DirectionList.ALL) {
                int value = master.shares[side.get3DDataValue()];
                if (value > 0) {
                    text(helper, DirectionList.getName(side).append(": " + value + "%"));
                }
            }

            EnergyContainer averages = EnergyContainer.getContainer(master);
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

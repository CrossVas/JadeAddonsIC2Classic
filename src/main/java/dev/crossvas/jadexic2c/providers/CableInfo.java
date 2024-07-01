package dev.crossvas.jadexic2c.providers;

import dev.crossvas.jadexic2c.base.IInfoProvider;
import dev.crossvas.jadexic2c.base.IJadeHelper;
import dev.crossvas.jadexic2c.helpers.EnergyContainer;
import ic2.core.block.cables.CableTileEntity;
import ic2.core.utils.helpers.Formatters;
import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class CableInfo implements IInfoProvider {

    public static final CableInfo THIS = new CableInfo();

    @Override
    public void addInfo(IJadeHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof CableTileEntity cable) {
            text(helper, "tooltip.item.ic2.eu_reader.cable_limit", cable.getConductorBreakdownEnergy() - 1);
            text(helper, "tooltip.item.ic2.eu_reader.cable_loss", Formatters.CABLE_LOSS_FORMAT.format(cable.getConductionLoss()));
            EnergyContainer result = EnergyContainer.getContainer(cable);
            int averageOut = result.getAverageOut();
            int packetOut = result.getPacketsOut();
            if (averageOut > 0) {
                helper.addPaddingElement(0, 3);
                text(helper, ChatFormatting.AQUA, "tooltip.item.ic2.eu_reader.cable_flow", Formatters.EU_FORMAT.format(averageOut));
                text(helper, ChatFormatting.AQUA, "tooltip.item.ic2.eu_reader.packet_flow", Formatters.EU_FORMAT.format(packetOut));
            }
        }
    }
}

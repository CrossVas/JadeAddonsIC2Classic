package dev.crossvas.lookingat.ic2c.base.providers;

import dev.crossvas.lookingat.ic2c.base.interfaces.IInfoProvider;
import dev.crossvas.lookingat.ic2c.base.interfaces.IHelper;
import dev.crossvas.lookingat.ic2c.helpers.EnergyContainer;
import ic2.core.block.cables.CableTileEntity;
import ic2.core.utils.helpers.Formatters;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class CableInfo implements IInfoProvider {

    public static final CableInfo THIS = new CableInfo();

    @Override
    public void addInfo(IHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof CableTileEntity cable) {
            defaultText(helper, "tooltip.item.ic2.eu_reader.cable_limit", cable.getConductorBreakdownEnergy() - 1);
            defaultText(helper, "tooltip.item.ic2.eu_reader.cable_loss", Formatters.CABLE_LOSS_FORMAT.format(cable.getConductionLoss()));
            EnergyContainer container = EnergyContainer.getContainer(cable);
            addCableAverages(helper, container.getAverageOut(), container.getPacketsOut());
        }
    }
}

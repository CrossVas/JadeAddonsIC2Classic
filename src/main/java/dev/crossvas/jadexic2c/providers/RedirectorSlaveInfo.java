package dev.crossvas.jadexic2c.providers;

import dev.crossvas.jadexic2c.base.interfaces.IInfoProvider;
import dev.crossvas.jadexic2c.base.interfaces.IJadeHelper;
import dev.crossvas.jadexic2c.helpers.EnergyContainer;
import ic2.api.util.DirectionList;
import ic2.core.block.storage.tiles.RedirectorMasterTileEntity;
import ic2.core.block.storage.tiles.RedirectorSlaveTileEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class RedirectorSlaveInfo implements IInfoProvider {

    public static final RedirectorSlaveInfo THIS = new RedirectorSlaveInfo();

    @Override
    public void addInfo(IJadeHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof RedirectorSlaveTileEntity slave) {
            BlockEntity neighborTile = DirectionList.getNeighborTile(slave, slave.getFacing());
            if (neighborTile instanceof RedirectorMasterTileEntity master) {
                defaultText(helper, "ic2.probe.redirector.slave.info", master.shares[slave.getFacing().getOpposite().get3DDataValue()]);
            }

            EnergyContainer container = EnergyContainer.getContainer(slave);
            addStats(helper, player, () -> addCableAverages(helper, container.getAverageOut(), container.getPacketsOut()));
        }
    }
}

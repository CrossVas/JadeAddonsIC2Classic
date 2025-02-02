package ic2.jadeplugin.providers;

import ic2.api.util.DirectionList;
import ic2.core.block.storage.tiles.RedirectorMasterTileEntity;
import ic2.core.block.storage.tiles.RedirectorSlaveTileEntity;
import ic2.jadeplugin.base.JadeHelper;
import ic2.jadeplugin.base.interfaces.IInfoProvider;
import ic2.jadeplugin.helpers.EnergyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class RedirectorSlaveInfo implements IInfoProvider {

    public static final RedirectorSlaveInfo THIS = new RedirectorSlaveInfo();

    @Override
    public void addInfo(JadeHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof RedirectorSlaveTileEntity slave) {
            BlockEntity neighborTile = DirectionList.getNeighborTile(slave, slave.getFacing());
            if (neighborTile instanceof RedirectorMasterTileEntity master) {
                helper.defaultText("ic2.probe.redirector.slave.info", master.shares[slave.getFacing().getOpposite().get3DDataValue()]);
            }

            EnergyContainer container = EnergyContainer.getContainer(slave);
            helper.addStats(player, () -> helper.addCableAverages(container.getAverageOut(), container.getPacketsOut()));
        }
    }
}

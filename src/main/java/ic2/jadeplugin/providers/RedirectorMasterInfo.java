package ic2.jadeplugin.providers;

import ic2.api.util.DirectionList;
import ic2.core.block.storage.tiles.RedirectorMasterTileEntity;
import ic2.jadeplugin.base.JadeHelper;
import ic2.jadeplugin.base.interfaces.IInfoProvider;
import ic2.jadeplugin.helpers.EnergyContainer;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class RedirectorMasterInfo implements IInfoProvider {

    public static final RedirectorMasterInfo THIS = new RedirectorMasterInfo();

    @Override
    public void addInfo(JadeHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof RedirectorMasterTileEntity master) {
            for (Direction side : DirectionList.ALL) {
                int value = master.shares[side.get3DDataValue()];
                if (value > 0) {
                    helper.text(DirectionList.getName(side).append(": " + value + "%"));
                }
            }

            EnergyContainer container = EnergyContainer.getContainer(master);
            helper.addCableAverages(container.getAverageIn(), container.getPacketsIn());
        }
    }
}

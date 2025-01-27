package dev.crossvas.jadexic2c.providers;

import dev.crossvas.jadexic2c.base.JadeHelper;
import dev.crossvas.jadexic2c.base.interfaces.IInfoProvider;
import dev.crossvas.jadexic2c.helpers.EnergyContainer;
import ic2.api.util.DirectionList;
import ic2.core.block.storage.tiles.RedirectorMasterTileEntity;
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
            helper.addStats(player, () -> helper.addCableAverages(container.getAverageIn(), container.getPacketsIn()));
        }
    }
}

package dev.crossvas.jadexic2c.providers;

import dev.crossvas.jadexic2c.base.JadeHelper;
import dev.crossvas.jadexic2c.base.interfaces.IInfoProvider;
import ic2.core.block.base.tiles.BaseLinkingTileEntity;
import ic2.core.block.storage.tiles.tank.TankTileEntity;
import ic2.core.utils.math.ColorUtils;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class DynamicTankInfo implements IInfoProvider {

    public static final DynamicTankInfo THIS = new DynamicTankInfo();

    @Override
    public void addInfo(JadeHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof TankTileEntity tank) {
            helper.addTankInfo(tank);
            if (!tank.isValid || tank.isDynamic()) {
                long time = tank.clockTime(512);
                helper.bar((int) time, 512, translate("ic2.multiblock.reform.next", 512 - time), ColorUtils.GRAY);
            }
        }
        if (blockEntity instanceof BaseLinkingTileEntity linkingTile) {
            BlockEntity master = linkingTile.getMaster();
            if (master instanceof TankTileEntity tank) {
                helper.addTankInfo(tank);
            }
        }
    }
}

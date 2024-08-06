package dev.crossvas.lookingat.ic2c.base.providers;

import dev.crossvas.lookingat.ic2c.base.LookingAtCommonHandler;
import dev.crossvas.lookingat.ic2c.base.interfaces.IInfoProvider;
import dev.crossvas.lookingat.ic2c.base.interfaces.IHelper;
import ic2.core.block.base.tiles.BaseLinkingTileEntity;
import ic2.core.block.storage.tiles.tank.TankTileEntity;
import ic2.core.utils.math.ColorUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class DynamicTankInfo implements IInfoProvider {

    public static final DynamicTankInfo THIS = new DynamicTankInfo();

    @Override
    public void addInfo(IHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof TankTileEntity tank) {
            LookingAtCommonHandler.addTankInfo(helper, tank);
            if (!tank.isValid || tank.isDynamic()) {
                long time = tank.clockTime(512);
                bar(helper, (int) time, 512, Component.translatable("ic2.multiblock.reform.next", 512 - time), ColorUtils.GRAY);
            }
        }
        if (blockEntity instanceof BaseLinkingTileEntity linkingTile) {
            BlockEntity master = linkingTile.getMaster();
            if (master instanceof TankTileEntity tank) {
                LookingAtCommonHandler.addTankInfo(helper, tank);
            }
        }
    }
}

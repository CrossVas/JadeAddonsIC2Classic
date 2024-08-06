package dev.crossvas.lookingat.ic2c.base.providers;

import dev.crossvas.lookingat.ic2c.base.LookingAtCommonHandler;
import dev.crossvas.lookingat.ic2c.base.interfaces.IInfoProvider;
import dev.crossvas.lookingat.ic2c.base.interfaces.IHelper;
import ic2.core.block.storage.tiles.tank.BaseValveTileEntity;
import ic2.core.block.storage.tiles.tank.PushingValveTileEntity;
import ic2.core.utils.helpers.Formatters;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class PushingValveInfo implements IInfoProvider {

    public static final PushingValveInfo THIS = new PushingValveInfo();

    @Override
    public void addInfo(IHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof BaseValveTileEntity baseValve) {
            LookingAtCommonHandler.TANK_REMOVAL.add(baseValve);
            if (baseValve instanceof PushingValveTileEntity) {
                defaultText(helper, "ic2.probe.pump.pressure", 100);
                defaultText(helper, "ic2.probe.pump.amount", Formatters.EU_FORMAT.format(2000L));
            }
        }
    }
}

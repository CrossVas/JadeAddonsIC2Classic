package ic2.jadeplugin.providers;

import ic2.core.block.storage.tiles.tank.BaseValveTileEntity;
import ic2.core.block.storage.tiles.tank.PushingValveTileEntity;
import ic2.core.utils.helpers.Formatters;
import ic2.jadeplugin.base.JadeHelper;
import ic2.jadeplugin.base.interfaces.IInfoProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class PushingValveInfo implements IInfoProvider {

    public static final PushingValveInfo THIS = new PushingValveInfo();

    @Override
    public void addInfo(JadeHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof BaseValveTileEntity baseValve) {
            JadeHelper.TANK_REMOVAL.add(baseValve);
            if (baseValve instanceof PushingValveTileEntity) {
                helper.defaultText("ic2.probe.pump.pressure", 100);
                helper.defaultText("ic2.probe.pump.amount", Formatters.EU_FORMAT.format(2000L));
            }
        }
    }
}

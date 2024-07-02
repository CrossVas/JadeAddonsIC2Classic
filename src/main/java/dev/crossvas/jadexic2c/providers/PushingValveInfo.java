package dev.crossvas.jadexic2c.providers;

import dev.crossvas.jadexic2c.base.interfaces.IInfoProvider;
import dev.crossvas.jadexic2c.base.interfaces.IJadeHelper;
import ic2.core.block.storage.tiles.tank.PushingValveTileEntity;
import ic2.core.utils.helpers.Formatters;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class PushingValveInfo implements IInfoProvider {

    public static final PushingValveInfo THIS = new PushingValveInfo();

    @Override
    public void addInfo(IJadeHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof PushingValveTileEntity) {
            defaultText(helper, "ic2.probe.pump.pressure", 100);
            defaultText(helper, "ic2.probe.pump.amount", Formatters.EU_FORMAT.format(2000L));
        }
    }
}

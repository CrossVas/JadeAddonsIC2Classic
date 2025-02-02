package dev.crossvas.jadexic2c.providers;

import dev.crossvas.jadexic2c.base.JadeHelper;
import dev.crossvas.jadexic2c.base.interfaces.IInfoProvider;
import ic2.core.block.machines.tiles.lv.PumpTileEntity;
import ic2.core.utils.helpers.Formatters;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class PumpInfo implements IInfoProvider {

    public static final PumpInfo THIS = new PumpInfo();

    @Override
    public void addInfo(JadeHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof PumpTileEntity pump) {
            helper.maxIn(pump.getMaxInput());
            helper.usage(pump.getPumpCost());
            helper.defaultText("ic2.probe.pump.pressure", 100);
            helper.defaultText("ic2.probe.pump.amount", Formatters.EU_FORMAT.format(800L));

            int progress = pump.getPumpProgress();
            int maxProgress = pump.getPumpMaxProgress();
            if (progress > 0) {
                helper.bar(progress, maxProgress, translate("ic2.probe.progress.full.name", progress, maxProgress).append("t"), -16733185);
            }
            helper.addTankInfo(pump);
        }
    }
}

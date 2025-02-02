package ic2.jadeplugin.providers;

import ic2.core.block.machines.tiles.ev.ElectricFisherTileEntity;
import ic2.core.utils.helpers.Formatters;
import ic2.core.utils.math.ColorUtils;
import ic2.jadeplugin.base.JadeHelper;
import ic2.jadeplugin.base.interfaces.IInfoProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class ElectricFisherInfo implements IInfoProvider {

    public static final ElectricFisherInfo THIS = new ElectricFisherInfo();

    @Override
    public void addInfo(JadeHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof ElectricFisherTileEntity fisher) {
            helper.maxIn(fisher.getMaxInput());
            helper.usage(150);

            int progress = (int) fisher.getProgress();
            int maxProgress = (int) fisher.getMaxProgress();
            if (progress > 0) {
                helper.bar(progress, maxProgress, translate("ic2.probe.progress.full.name", Formatters.EU_READER_FORMAT.format(progress), maxProgress), -16733185);
            }
            if (!fisher.isValid || fisher.isDynamic()) {
                long time = fisher.clockTime(512);
                helper.bar((int) time, 512, translate("ic2.multiblock.reform.next", 512 - time), ColorUtils.GRAY);
            }
        }
    }
}

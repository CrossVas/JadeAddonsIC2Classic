package ic2.jadeplugin.providers;

import ic2.core.block.machines.tiles.lv.WoodGassifierTileEntity;
import ic2.core.utils.helpers.Formatters;
import ic2.jadeplugin.base.JadeHelper;
import ic2.jadeplugin.base.interfaces.IInfoProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class ElectricWoodGassifierInfo implements IInfoProvider {

    public static final ElectricWoodGassifierInfo THIS = new ElectricWoodGassifierInfo();

    @Override
    public void addInfo(JadeHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof WoodGassifierTileEntity woodGassifier) {
            helper.maxIn(woodGassifier.getMaxInput());
            helper.usage(1);
            helper.defaultText("ic2.probe.pump.pressure", 25);
            helper.defaultText("ic2.probe.pump.amount", Formatters.EU_FORMAT.format(1800L));
            float progress = woodGassifier.getProgress();
            float maxProgress = woodGassifier.getMaxProgress();
            if (progress > 0) {
                helper.bar((int) progress, (int) maxProgress, Component.translatable("ic2.probe.progress.full.name", (int) progress, (int) maxProgress), -16733185);
            }
            helper.addTankInfo(woodGassifier);
        }
    }
}

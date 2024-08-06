package dev.crossvas.lookingat.ic2c.base.providers;

import dev.crossvas.lookingat.ic2c.base.LookingAtCommonHandler;
import dev.crossvas.lookingat.ic2c.base.interfaces.IInfoProvider;
import dev.crossvas.lookingat.ic2c.base.interfaces.IHelper;
import ic2.api.energy.EnergyNet;
import ic2.core.block.machines.tiles.lv.PumpTileEntity;
import ic2.core.utils.helpers.Formatters;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class PumpInfo implements IInfoProvider {

    public static final PumpInfo THIS = new PumpInfo();

    @Override
    public void addInfo(IHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof PumpTileEntity pump) {
            defaultText(helper, "ic2.probe.eu.tier.name", EnergyNet.INSTANCE.getDisplayTier(pump.getTier()));
            defaultText(helper, "ic2.probe.eu.max_in.name", pump.getMaxInput());
            defaultText(helper, "ic2.probe.eu.usage.name", pump.getPumpCost());
            defaultText(helper, "ic2.probe.pump.pressure", 100);
            defaultText(helper, "ic2.probe.pump.amount", Formatters.EU_FORMAT.format(800L));

            int progress = pump.getPumpProgress();
            int maxProgress = pump.getPumpMaxProgress();
            if (progress > 0) {
                bar(helper, progress, maxProgress, Component.translatable("ic2.probe.progress.full.name", progress, maxProgress).append("t"), -16733185);
            }
            LookingAtCommonHandler.addTankInfo(helper, pump);
        }
    }
}

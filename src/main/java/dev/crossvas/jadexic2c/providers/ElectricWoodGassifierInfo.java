package dev.crossvas.jadexic2c.providers;

import dev.crossvas.jadexic2c.JadeCommonHandler;
import dev.crossvas.jadexic2c.base.interfaces.IInfoProvider;
import dev.crossvas.jadexic2c.base.interfaces.IJadeHelper;
import ic2.api.energy.EnergyNet;
import ic2.core.block.machines.tiles.lv.WoodGassifierTileEntity;
import ic2.core.utils.helpers.Formatters;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class ElectricWoodGassifierInfo implements IInfoProvider {

    public static final ElectricWoodGassifierInfo THIS = new ElectricWoodGassifierInfo();

    @Override
    public void addInfo(IJadeHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof WoodGassifierTileEntity woodGassifier) {
            defaultText(helper, "ic2.probe.eu.tier.name", EnergyNet.INSTANCE.getDisplayTier(woodGassifier.getTier()));
            defaultText(helper, "ic2.probe.eu.max_in.name", woodGassifier.getMaxInput());
            defaultText(helper, "ic2.probe.eu.usage.name", 1);
            defaultText(helper, "ic2.probe.pump.pressure", 25);
            defaultText(helper, "ic2.probe.pump.amount", Formatters.EU_FORMAT.format(1800L));
            float progress = woodGassifier.getProgress();
            float maxProgress = woodGassifier.getMaxProgress();
            if (progress > 0) {
                bar(helper, (int) progress, (int) maxProgress, Component.translatable("ic2.probe.progress.full.name", (int) progress, (int) maxProgress), -16733185);
            }
            JadeCommonHandler.addTankInfo(helper, woodGassifier);
        }
    }
}

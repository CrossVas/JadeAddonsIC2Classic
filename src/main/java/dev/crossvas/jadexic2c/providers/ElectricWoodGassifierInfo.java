package dev.crossvas.jadexic2c.providers;

import dev.crossvas.jadexic2c.base.JadeHelper;
import dev.crossvas.jadexic2c.base.interfaces.IInfoProvider;
import ic2.api.energy.EnergyNet;
import ic2.core.block.machines.tiles.lv.WoodGassifierTileEntity;
import ic2.core.utils.helpers.Formatters;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class ElectricWoodGassifierInfo implements IInfoProvider {

    public static final ElectricWoodGassifierInfo THIS = new ElectricWoodGassifierInfo();

    @Override
    public void addInfo(JadeHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof WoodGassifierTileEntity woodGassifier) {
            helper.defaultText("ic2.probe.eu.tier.name", EnergyNet.INSTANCE.getDisplayTier(woodGassifier.getTier()));
            helper.defaultText("ic2.probe.eu.max_in.name", woodGassifier.getMaxInput());
            helper.defaultText("ic2.probe.eu.usage.name", 1);
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

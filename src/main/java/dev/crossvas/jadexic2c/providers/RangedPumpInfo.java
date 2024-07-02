package dev.crossvas.jadexic2c.providers;

import dev.crossvas.jadexic2c.JadeCommonHandler;
import dev.crossvas.jadexic2c.base.interfaces.IInfoProvider;
import dev.crossvas.jadexic2c.base.interfaces.IJadeHelper;
import ic2.api.energy.EnergyNet;
import ic2.core.block.machines.tiles.mv.RangedPumpTileEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class RangedPumpInfo implements IInfoProvider {

    public static final RangedPumpInfo THIS = new RangedPumpInfo();

    @Override
    public void addInfo(IJadeHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof RangedPumpTileEntity ranged) {
            defaultText(helper, "ic2.probe.eu.tier.name", EnergyNet.INSTANCE.getDisplayTier(ranged.getTier()));
            defaultText(helper, "ic2.probe.eu.max_in.name", ranged.getMaxInput());
            defaultText(helper, "ic2.probe.eu.usage.name", 10);

            defaultText(helper, Component.translatable(ranged.isOperating() ? "ic2.probe.miner.mining.name" : "ic2.probe.miner.retracting.name"));
            int y = ranged.getPipeTip().getY();
            bar(helper, y, ranged.getPosition().getY(), Component.translatable("ic2.probe.pump.progress.name", y), -10996205);
            JadeCommonHandler.addTankInfo(helper, ranged);
        }
    }
}

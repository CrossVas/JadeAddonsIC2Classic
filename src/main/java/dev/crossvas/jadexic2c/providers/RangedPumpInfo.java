package dev.crossvas.jadexic2c.providers;

import dev.crossvas.jadexic2c.base.JadeHelper;
import dev.crossvas.jadexic2c.base.interfaces.IInfoProvider;
import ic2.api.energy.EnergyNet;
import ic2.core.block.machines.tiles.mv.RangedPumpTileEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class RangedPumpInfo implements IInfoProvider {

    public static final RangedPumpInfo THIS = new RangedPumpInfo();

    @Override
    public void addInfo(JadeHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof RangedPumpTileEntity ranged) {
            helper.defaultText("ic2.probe.eu.tier.name", EnergyNet.INSTANCE.getDisplayTier(ranged.getTier()));
            helper.defaultText("ic2.probe.eu.max_in.name", ranged.getMaxInput());
            helper.defaultText("ic2.probe.eu.usage.name", 10);

            helper.defaultText(translate(ranged.isOperating() ? "ic2.probe.miner.mining.name" : "ic2.probe.miner.retracting.name"));
            int y = ranged.getPipeTip().getY();
            helper.bar(y, ranged.getPosition().getY(), translate("ic2.probe.pump.progress.name", y), -10996205);
            helper.addTankInfo(ranged);
        }
    }
}

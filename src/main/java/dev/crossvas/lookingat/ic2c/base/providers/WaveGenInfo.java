package dev.crossvas.lookingat.ic2c.base.providers;

import dev.crossvas.lookingat.ic2c.base.interfaces.IInfoProvider;
import dev.crossvas.lookingat.ic2c.base.interfaces.IHelper;
import dev.crossvas.lookingat.ic2c.helpers.Formatter;
import ic2.api.energy.EnergyNet;
import ic2.core.block.generators.tiles.WaveGenTileEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class WaveGenInfo implements IInfoProvider {

    public static final WaveGenInfo THIS = new WaveGenInfo();

    @Override
    public void addInfo(IHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof WaveGenTileEntity waveGen) {
            defaultText(helper, "ic2.probe.eu.tier.name", EnergyNet.INSTANCE.getDisplayTier(waveGen.getSourceTier()));
            defaultText(helper, "ic2.probe.eu.output.current.name", Formatter.formatNumber(waveGen.getEUProduction(), 3));
            defaultText(helper, "ic2.probe.eu.output.max.name", waveGen.getMaxEnergyOutput());
        }
    }
}

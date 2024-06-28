package dev.crossvas.jadexic2c.providers;

import dev.crossvas.jadexic2c.base.IJadeHelper;
import dev.crossvas.jadexic2c.helpers.Formatter;
import ic2.api.energy.EnergyNet;
import ic2.core.block.generators.tiles.WaveGenTileEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class WaveGenInfo extends EUReaderInfoProvider {

    public static final WaveGenInfo THIS = new WaveGenInfo();

    @Override
    public void addInfo(IJadeHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof WaveGenTileEntity waveGen) {
            text(helper, "ic2.probe.eu.tier.name", EnergyNet.INSTANCE.getDisplayTier(waveGen.getSourceTier()));
            text(helper, "ic2.probe.eu.output.current.name", Formatter.formatNumber(waveGen.getEUProduction(), 3));
            text(helper, "ic2.probe.eu.tier.name", "ic2.probe.eu.output.max.name", waveGen.getMaxEnergyOutput());
        }
    }
}

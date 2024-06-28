package dev.crossvas.jadexic2c.providers;

import dev.crossvas.jadexic2c.base.IJadeHelper;
import dev.crossvas.jadexic2c.helpers.Formatter;
import ic2.api.energy.EnergyNet;
import ic2.core.block.generators.tiles.SolarPanelTileEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class SolarPanelInfo extends EUReaderInfoProvider {

    public static final SolarPanelInfo THIS = new SolarPanelInfo();

    @Override
    public void addInfo(IJadeHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof SolarPanelTileEntity solarPanel) {
            text(helper, "ic2.probe.eu.tier.name", EnergyNet.INSTANCE.getDisplayTier(solarPanel.getSourceTier()));
            text(helper, "ic2.probe.eu.output.current.name", Formatter.formatNumber(solarPanel.getEUProduction(), 3));
            text(helper, "ic2.probe.eu.output.max.name", solarPanel.getMaxEnergyOutput());
        }
    }
}

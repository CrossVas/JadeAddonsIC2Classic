package dev.crossvas.jadexic2c.providers;

import dev.crossvas.jadexic2c.base.JadeHelper;
import dev.crossvas.jadexic2c.base.interfaces.IInfoProvider;
import dev.crossvas.jadexic2c.helpers.Formatter;
import ic2.api.energy.EnergyNet;
import ic2.core.block.generators.tiles.SolarPanelTileEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class SolarPanelInfo implements IInfoProvider {

    public static final SolarPanelInfo THIS = new SolarPanelInfo();

    @Override
    public void addInfo(JadeHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof SolarPanelTileEntity solarPanel) {
            helper.defaultText("ic2.probe.eu.tier.name", EnergyNet.INSTANCE.getDisplayTier(solarPanel.getSourceTier()));
            helper.defaultText("ic2.probe.eu.output.current.name", Formatter.formatNumber(solarPanel.getEUProduction(), 3));
            helper.defaultText("ic2.probe.eu.output.max.name", solarPanel.getMaxEnergyOutput());
        }
    }
}

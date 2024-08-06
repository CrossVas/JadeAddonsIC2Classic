package dev.crossvas.lookingat.ic2c.base.providers;

import dev.crossvas.lookingat.ic2c.base.interfaces.IInfoProvider;
import dev.crossvas.lookingat.ic2c.base.interfaces.IHelper;
import dev.crossvas.lookingat.ic2c.helpers.Formatter;
import ic2.api.energy.EnergyNet;
import ic2.core.block.generators.tiles.SolarPanelTileEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class SolarPanelInfo implements IInfoProvider {

    public static final SolarPanelInfo THIS = new SolarPanelInfo();

    @Override
    public void addInfo(IHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof SolarPanelTileEntity solarPanel) {
            defaultText(helper, "ic2.probe.eu.tier.name", EnergyNet.INSTANCE.getDisplayTier(solarPanel.getSourceTier()));
            defaultText(helper, "ic2.probe.eu.output.current.name", Formatter.formatNumber(solarPanel.getEUProduction(), 3));
            defaultText(helper, "ic2.probe.eu.output.max.name", solarPanel.getMaxEnergyOutput());
        }
    }
}

package dev.crossvas.jadexic2c.providers;

import dev.crossvas.jadexic2c.JadeCommonHandler;
import dev.crossvas.jadexic2c.base.IJadeHelper;
import dev.crossvas.jadexic2c.helpers.Formatter;
import ic2.api.energy.EnergyNet;
import ic2.core.block.generators.tiles.SteamTurbineTileEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class SteamTurbineInfo extends EUReaderInfoProvider {

    public static final SteamTurbineInfo THIS = new SteamTurbineInfo();

    @Override
    public void addInfo(IJadeHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof SteamTurbineTileEntity turbine) {
            text(helper, "ic2.probe.eu.tier.name", EnergyNet.INSTANCE.getDisplayTier(turbine.getSourceTier()));
            text(helper, "ic2.probe.eu.output.current.name", Formatter.formatNumber(turbine.getEUProduction(), 3));
            text(helper, "ic2.probe.eu.output.max.name", turbine.getMaxEnergyOutput());
            JadeCommonHandler.addTankInfo(helper, turbine);
        }
    }
}

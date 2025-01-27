package dev.crossvas.jadexic2c.providers;

import dev.crossvas.jadexic2c.base.JadeHelper;
import dev.crossvas.jadexic2c.base.interfaces.IInfoProvider;
import dev.crossvas.jadexic2c.helpers.Formatter;
import ic2.api.energy.EnergyNet;
import ic2.core.block.generators.tiles.SteamTurbineTileEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class SteamTurbineInfo implements IInfoProvider {

    public static final SteamTurbineInfo THIS = new SteamTurbineInfo();

    @Override
    public void addInfo(JadeHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof SteamTurbineTileEntity turbine) {
            helper.defaultText("ic2.probe.eu.tier.name", EnergyNet.INSTANCE.getDisplayTier(turbine.getSourceTier()));
            helper.defaultText("ic2.probe.eu.output.current.name", Formatter.formatNumber(turbine.getEUProduction(), 3));
            helper.defaultText("ic2.probe.eu.output.max.name", turbine.getMaxEnergyOutput());
            helper.addTankInfo(turbine);
        }
    }
}

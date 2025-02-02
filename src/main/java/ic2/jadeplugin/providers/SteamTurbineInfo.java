package ic2.jadeplugin.providers;

import ic2.core.block.generators.tiles.SteamTurbineTileEntity;
import ic2.jadeplugin.base.JadeHelper;
import ic2.jadeplugin.base.interfaces.IInfoProvider;
import ic2.jadeplugin.helpers.Formatter;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class SteamTurbineInfo implements IInfoProvider {

    public static final SteamTurbineInfo THIS = new SteamTurbineInfo();

    @Override
    public void addInfo(JadeHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof SteamTurbineTileEntity turbine) {
            helper.tier(turbine.getSourceTier());
            helper.defaultText("ic2.probe.eu.output.current.name", Formatter.formatNumber(turbine.getEUProduction(), 3));
            helper.maxOut(turbine.getMaxEnergyOutput());
            helper.addTankInfo(turbine);
        }
    }
}

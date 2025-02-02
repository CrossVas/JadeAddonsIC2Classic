package ic2.jadeplugin.providers;

import ic2.core.block.generators.tiles.SolarPanelTileEntity;
import ic2.jadeplugin.base.JadeHelper;
import ic2.jadeplugin.base.interfaces.IInfoProvider;
import ic2.jadeplugin.helpers.Formatter;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class SolarPanelInfo implements IInfoProvider {

    public static final SolarPanelInfo THIS = new SolarPanelInfo();

    @Override
    public void addInfo(JadeHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof SolarPanelTileEntity solarPanel) {
            helper.tier(solarPanel.getSourceTier());
            helper.defaultText("ic2.probe.eu.output.current.name", Formatter.formatNumber(solarPanel.getEUProduction(), 3));
            helper.maxOut(solarPanel.getMaxEnergyOutput());
        }
    }
}

package ic2.jadeplugin.providers;

import ic2.core.block.generators.tiles.WaveGenTileEntity;
import ic2.jadeplugin.base.JadeHelper;
import ic2.jadeplugin.base.interfaces.IInfoProvider;
import ic2.jadeplugin.helpers.Formatter;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class WaveGenInfo implements IInfoProvider {

    public static final WaveGenInfo THIS = new WaveGenInfo();

    @Override
    public void addInfo(JadeHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof WaveGenTileEntity waveGen) {
            helper.tier(waveGen.getSourceTier());
            helper.defaultText("ic2.probe.eu.output.current.name", Formatter.formatNumber(waveGen.getEUProduction(), 3));
            helper.maxOut(waveGen.getMaxEnergyOutput());
        }
    }
}

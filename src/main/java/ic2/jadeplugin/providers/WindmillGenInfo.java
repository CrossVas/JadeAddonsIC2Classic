package ic2.jadeplugin.providers;

import ic2.core.block.generators.tiles.WindmillTileEntity;
import ic2.jadeplugin.base.JadeHelper;
import ic2.jadeplugin.base.interfaces.IInfoProvider;
import ic2.jadeplugin.helpers.Formatter;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class WindmillGenInfo implements IInfoProvider {

    public static final WindmillGenInfo THIS = new WindmillGenInfo();

    @Override
    public void addInfo(JadeHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof WindmillTileEntity windmill) {
            float euProduction = Math.max(0, windmill.getEUProduction()); // because -0.001 is a thing
            helper.tier(windmill.getSourceTier());
            helper.defaultText("ic2.probe.eu.output.current.name", Formatter.formatNumber(euProduction, 3));
            helper.maxOut(windmill.getMaxEnergyOutput());
        }
    }
}

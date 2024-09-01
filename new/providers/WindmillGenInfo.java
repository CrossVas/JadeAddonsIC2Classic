package dev.crossvas.jadexic2c.providers;

import dev.crossvas.jadexic2c.base.interfaces.IInfoProvider;
import dev.crossvas.jadexic2c.base.interfaces.IJadeHelper;
import dev.crossvas.jadexic2c.helpers.Formatter;
import ic2.api.energy.EnergyNet;
import ic2.core.block.generators.tiles.WindmillTileEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class WindmillGenInfo implements IInfoProvider {

    public static final WindmillGenInfo THIS = new WindmillGenInfo();

    @Override
    public void addInfo(IJadeHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof WindmillTileEntity windmill) {
            float euProduction = Math.max(0, windmill.getEUProduction()); // because -0.001 is a thing
            defaultText(helper, "ic2.probe.eu.tier.name", EnergyNet.INSTANCE.getDisplayTier(windmill.getSourceTier()));
            defaultText(helper, "ic2.probe.eu.output.current.name", Formatter.formatNumber(euProduction, 3));
            defaultText(helper, "ic2.probe.eu.output.max.name", windmill.getMaxEnergyOutput());
        }
    }
}

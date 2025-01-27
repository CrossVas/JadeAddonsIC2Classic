package dev.crossvas.jadexic2c.providers;

import dev.crossvas.jadexic2c.base.JadeHelper;
import dev.crossvas.jadexic2c.base.interfaces.IInfoProvider;
import dev.crossvas.jadexic2c.helpers.Formatter;
import ic2.api.energy.EnergyNet;
import ic2.core.block.generators.tiles.OceanGeneratorTileEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class OceanGenInfo implements IInfoProvider {

    public static final OceanGenInfo THIS = new OceanGenInfo();

    @Override
    public void addInfo(JadeHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof OceanGeneratorTileEntity oceanGen) {
            helper.defaultText("ic2.probe.eu.tier.name", EnergyNet.INSTANCE.getDisplayTier(oceanGen.getSourceTier()));
            helper.defaultText("ic2.probe.eu.output.current.name", Formatter.formatNumber(oceanGen.getEUProduction(), 3));
            helper.defaultText("ic2.probe.eu.output.max.name", oceanGen.getMaxEnergyOutput());

            int water = Integer.parseInt(Formatter.formatInt(oceanGen.waterFound, 4));
            int coral = Integer.parseInt(Formatter.formatInt(oceanGen.coralsFound, 4));
            if (water > 0) {
                helper.bar(water, 1000, translate("ic2.probe.water.full.name", water, 1000), -16733185);
            }
            if (coral > 0) {
                helper.bar(coral, 50, translate("ic2.probe.corals.full.name", coral, 50), -5829955);
            }
        }
    }
}

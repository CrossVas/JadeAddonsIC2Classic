package dev.crossvas.jadexic2c.providers;

import dev.crossvas.jadexic2c.base.interfaces.IInfoProvider;
import dev.crossvas.jadexic2c.base.interfaces.IJadeHelper;
import ic2.api.energy.EnergyNet;
import ic2.core.block.transport.fluid.tiles.ElectricPipePumpTileEntity;
import ic2.core.block.transport.fluid.tiles.SimplePipePumpTileEntity;
import ic2.core.utils.helpers.Formatters;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class PipePumpInfo implements IInfoProvider {

    public static final PipePumpInfo THIS = new PipePumpInfo();

    @Override
    public void addInfo(IJadeHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof SimplePipePumpTileEntity simplePump) {
            if (simplePump instanceof ElectricPipePumpTileEntity electricPump) {
                defaultText(helper, "ic2.probe.eu.tier.name", EnergyNet.INSTANCE.getDisplayTier(electricPump.getTier()));
                defaultText(helper, "ic2.probe.eu.max_in.name", EnergyNet.INSTANCE.getPowerFromTier(electricPump.getTier()));
            }
            defaultText(helper, "ic2.probe.pump.pressure", simplePump.getPressure());
            defaultText(helper, "ic2.probe.pump.amount", Formatters.EU_FORMAT.format(simplePump.getDrainAmount() / 20));
        }
    }
}

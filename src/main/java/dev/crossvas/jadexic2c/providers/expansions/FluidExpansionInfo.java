package dev.crossvas.jadexic2c.providers.expansions;

import dev.crossvas.jadexic2c.base.JadeHelper;
import dev.crossvas.jadexic2c.base.interfaces.IInfoProvider;
import ic2.core.block.machines.tiles.nv.TankExpansionTileEntity;
import ic2.core.inventory.filter.IFilter;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class FluidExpansionInfo implements IInfoProvider {

    public static final FluidExpansionInfo THIS = new FluidExpansionInfo();

    @Override
    public void addInfo(JadeHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof TankExpansionTileEntity tank) {
            helper.addTankInfo(tank);
        }
    }

    @Override
    public IFilter getFilter() {
        return ALWAYS;
    }
}

package dev.crossvas.jadexic2c.providers.expansions;

import dev.crossvas.jadexic2c.JadeCommonHandler;
import dev.crossvas.jadexic2c.base.interfaces.IInfoProvider;
import dev.crossvas.jadexic2c.base.interfaces.IJadeHelper;
import ic2.core.block.machines.tiles.nv.TankExpansionTileEntity;
import ic2.core.inventory.filter.IFilter;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class FluidExpansionInfo implements IInfoProvider {

    public static final FluidExpansionInfo THIS = new FluidExpansionInfo();

    @Override
    public void addInfo(IJadeHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof TankExpansionTileEntity tank) {
            JadeCommonHandler.addTankInfo(helper, tank);
        }
    }

    @Override
    public IFilter getFilter() {
        return ALWAYS;
    }
}

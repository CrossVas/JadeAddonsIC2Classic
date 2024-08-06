package dev.crossvas.lookingat.ic2c.base.providers.expansions;

import dev.crossvas.lookingat.ic2c.base.LookingAtCommonHandler;
import dev.crossvas.lookingat.ic2c.base.interfaces.IInfoProvider;
import dev.crossvas.lookingat.ic2c.base.interfaces.IHelper;
import ic2.core.block.machines.tiles.nv.TankExpansionTileEntity;
import ic2.core.inventory.filter.IFilter;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class FluidExpansionInfo implements IInfoProvider {

    public static final FluidExpansionInfo THIS = new FluidExpansionInfo();

    @Override
    public void addInfo(IHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof TankExpansionTileEntity tank) {
            LookingAtCommonHandler.addTankInfo(helper, tank);
        }
    }

    @Override
    public IFilter getFilter() {
        return ALWAYS;
    }
}

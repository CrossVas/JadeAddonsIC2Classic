package ic2.jadeplugin.providers.expansions;

import ic2.core.block.machines.tiles.nv.TankExpansionTileEntity;
import ic2.core.inventory.filter.IFilter;
import ic2.jadeplugin.base.JadeHelper;
import ic2.jadeplugin.base.interfaces.IInfoProvider;
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

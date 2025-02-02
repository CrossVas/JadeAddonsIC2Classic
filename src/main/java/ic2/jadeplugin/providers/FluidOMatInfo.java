package ic2.jadeplugin.providers;

import ic2.core.block.personal.tile.FluidOMatTileEntity;
import ic2.jadeplugin.base.JadeHelper;
import ic2.jadeplugin.base.interfaces.IInfoProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class FluidOMatInfo implements IInfoProvider {

    public static final FluidOMatInfo THIS = new FluidOMatInfo();

    @Override
    public void addInfo(JadeHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof FluidOMatTileEntity fluidOMat) {
            helper.addTankInfo(fluidOMat);
        }
    }
}

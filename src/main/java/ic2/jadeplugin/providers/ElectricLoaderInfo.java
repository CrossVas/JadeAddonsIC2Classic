package ic2.jadeplugin.providers;

import ic2.core.block.base.tiles.impls.BaseElectricLoaderTileEntity;
import ic2.jadeplugin.base.JadeHelper;
import ic2.jadeplugin.base.interfaces.IInfoProvider;
import ic2.jadeplugin.helpers.EnergyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class ElectricLoaderInfo implements IInfoProvider {

    public static final ElectricLoaderInfo THIS = new ElectricLoaderInfo();

    @Override
    public void addInfo(JadeHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof BaseElectricLoaderTileEntity loader) {
            helper.maxIn(loader.getMaxInput());
            EnergyContainer container = EnergyContainer.getContainer(loader);
            helper.addAveragesIn(container);
        }
    }
}

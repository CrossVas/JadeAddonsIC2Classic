package ic2.jadeplugin.providers;

import ic2.core.block.base.tiles.impls.BaseElectricUnloaderTileEntity;
import ic2.jadeplugin.base.JadeHelper;
import ic2.jadeplugin.base.interfaces.IInfoProvider;
import ic2.jadeplugin.helpers.EnergyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class ElectricUnloaderInfo implements IInfoProvider {

    public static final ElectricUnloaderInfo THIS = new ElectricUnloaderInfo();

    @Override
    public void addInfo(JadeHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof BaseElectricUnloaderTileEntity unloader) {
            helper.maxOut(unloader.getMaxEnergyOutput());
            helper.defaultText("ic2.probe.transformer.packets.name", 10);
            EnergyContainer container = EnergyContainer.getContainer(unloader);
            helper.addAveragesOut(container);
        }
    }
}

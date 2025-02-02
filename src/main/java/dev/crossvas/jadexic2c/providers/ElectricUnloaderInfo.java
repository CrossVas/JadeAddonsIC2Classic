package dev.crossvas.jadexic2c.providers;

import dev.crossvas.jadexic2c.base.JadeHelper;
import dev.crossvas.jadexic2c.base.interfaces.IInfoProvider;
import dev.crossvas.jadexic2c.helpers.EnergyContainer;
import ic2.api.energy.EnergyNet;
import ic2.core.block.base.tiles.impls.BaseElectricUnloaderTileEntity;
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
            helper.addStats(player, () -> helper.addAveragesOut(container));
        }
    }
}

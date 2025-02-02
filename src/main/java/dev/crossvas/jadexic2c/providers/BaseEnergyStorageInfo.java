package dev.crossvas.jadexic2c.providers;

import dev.crossvas.jadexic2c.base.JadeHelper;
import dev.crossvas.jadexic2c.base.interfaces.IInfoProvider;
import dev.crossvas.jadexic2c.helpers.EnergyContainer;
import ic2.api.energy.EnergyNet;
import ic2.core.block.base.tiles.impls.BaseEnergyStorageTileEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class BaseEnergyStorageInfo implements IInfoProvider {

    public static final BaseEnergyStorageInfo THIS = new BaseEnergyStorageInfo();

    @Override
    public void addInfo(JadeHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof BaseEnergyStorageTileEntity energyStorage) {
            helper.tier(energyStorage.getSourceTier());
            helper.defaultText("info.energy.io", EnergyNet.INSTANCE.getPowerFromTier(energyStorage.getSourceTier()));

            EnergyContainer container = EnergyContainer.getContainer(energyStorage);
            helper.addStats(player, () -> helper.addAveragesFull(container));
        }
    }
}

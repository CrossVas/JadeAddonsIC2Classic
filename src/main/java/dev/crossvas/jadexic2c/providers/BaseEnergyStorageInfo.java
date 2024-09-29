package dev.crossvas.jadexic2c.providers;

import dev.crossvas.jadexic2c.base.interfaces.IInfoProvider;
import dev.crossvas.jadexic2c.base.interfaces.IJadeHelper;
import dev.crossvas.jadexic2c.helpers.EnergyContainer;
import ic2.api.energy.EnergyNet;
import ic2.core.block.base.tiles.impls.BaseEnergyStorageTileEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class BaseEnergyStorageInfo implements IInfoProvider {

    public static final BaseEnergyStorageInfo THIS = new BaseEnergyStorageInfo();

    @Override
    public void addInfo(IJadeHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof BaseEnergyStorageTileEntity energyStorage) {
            defaultText(helper, "ic2.probe.eu.tier.name", EnergyNet.INSTANCE.getDisplayTier(energyStorage.getSourceTier()));
            defaultText(helper, "ic2.probe.eu.max_in.name", EnergyNet.INSTANCE.getPowerFromTier(energyStorage.getTier()));
            defaultText(helper, "ic2.probe.eu.output.name", energyStorage.getProvidedEnergy());

            EnergyContainer container = EnergyContainer.getContainer(energyStorage);
            addStats(helper, player, () -> addAveragesFull(helper, container));
        }
    }
}

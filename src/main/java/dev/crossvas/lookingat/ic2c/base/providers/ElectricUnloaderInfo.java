package dev.crossvas.lookingat.ic2c.base.providers;

import dev.crossvas.lookingat.ic2c.base.interfaces.IInfoProvider;
import dev.crossvas.lookingat.ic2c.base.interfaces.IHelper;
import dev.crossvas.lookingat.ic2c.helpers.EnergyContainer;
import ic2.api.energy.EnergyNet;
import ic2.core.block.base.tiles.impls.BaseElectricUnloaderTileEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class ElectricUnloaderInfo implements IInfoProvider {

    public static final ElectricUnloaderInfo THIS = new ElectricUnloaderInfo();

    @Override
    public void addInfo(IHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof BaseElectricUnloaderTileEntity unloader) {
            defaultText(helper, "ic2.probe.eu.tier.name", EnergyNet.INSTANCE.getDisplayTier(unloader.getSourceTier()));
            defaultText(helper, "ic2.probe.eu.output.max.name", unloader.getMaxEnergyOutput());
            defaultText(helper, "ic2.probe.transformer.packets.name", 10);
            EnergyContainer container = EnergyContainer.getContainer(unloader);
            addAveragesOut(helper, container);
        }
    }
}

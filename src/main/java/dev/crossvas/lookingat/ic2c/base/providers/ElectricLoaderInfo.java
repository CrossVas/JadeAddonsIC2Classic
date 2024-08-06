package dev.crossvas.lookingat.ic2c.base.providers;

import dev.crossvas.lookingat.ic2c.base.interfaces.IInfoProvider;
import dev.crossvas.lookingat.ic2c.base.interfaces.IHelper;
import dev.crossvas.lookingat.ic2c.helpers.EnergyContainer;
import ic2.api.energy.EnergyNet;
import ic2.core.block.base.tiles.impls.BaseElectricLoaderTileEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class ElectricLoaderInfo implements IInfoProvider {

    public static final ElectricLoaderInfo THIS = new ElectricLoaderInfo();

    @Override
    public void addInfo(IHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof BaseElectricLoaderTileEntity loader) {
            defaultText(helper, "ic2.probe.eu.tier.name", EnergyNet.INSTANCE.getDisplayTier(loader.getSinkTier()));
            defaultText(helper, "ic2.probe.eu.max_in.name", loader.getMaxInput());
            EnergyContainer container = EnergyContainer.getContainer(loader);
            addAveragesIn(helper, container);

        }
    }
}

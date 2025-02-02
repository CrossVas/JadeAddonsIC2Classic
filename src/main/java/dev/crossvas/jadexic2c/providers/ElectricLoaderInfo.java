package dev.crossvas.jadexic2c.providers;

import dev.crossvas.jadexic2c.base.JadeHelper;
import dev.crossvas.jadexic2c.base.interfaces.IInfoProvider;
import dev.crossvas.jadexic2c.helpers.EnergyContainer;
import ic2.api.energy.EnergyNet;
import ic2.core.block.base.tiles.impls.BaseElectricLoaderTileEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class ElectricLoaderInfo implements IInfoProvider {

    public static final ElectricLoaderInfo THIS = new ElectricLoaderInfo();

    @Override
    public void addInfo(JadeHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof BaseElectricLoaderTileEntity loader) {
            helper.maxIn(loader.getMaxInput());
            EnergyContainer container = EnergyContainer.getContainer(loader);
            helper.addStats(player, () -> helper.addAveragesIn(container));
        }
    }
}

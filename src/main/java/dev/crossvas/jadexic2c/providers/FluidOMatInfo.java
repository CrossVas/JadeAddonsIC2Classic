package dev.crossvas.jadexic2c.providers;

import dev.crossvas.jadexic2c.base.JadeHelper;
import dev.crossvas.jadexic2c.base.interfaces.IInfoProvider;
import ic2.core.block.personal.tile.FluidOMatTileEntity;
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

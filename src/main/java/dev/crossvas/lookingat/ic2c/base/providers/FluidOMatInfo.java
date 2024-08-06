package dev.crossvas.lookingat.ic2c.base.providers;

import dev.crossvas.lookingat.ic2c.base.LookingAtCommonHandler;
import dev.crossvas.lookingat.ic2c.base.interfaces.IInfoProvider;
import dev.crossvas.lookingat.ic2c.base.interfaces.IHelper;
import ic2.core.block.personal.tile.FluidOMatTileEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class FluidOMatInfo implements IInfoProvider {

    public static final FluidOMatInfo THIS = new FluidOMatInfo();

    @Override
    public void addInfo(IHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof FluidOMatTileEntity fluidOMat) {
            LookingAtCommonHandler.addTankInfo(helper, fluidOMat);
        }
    }
}

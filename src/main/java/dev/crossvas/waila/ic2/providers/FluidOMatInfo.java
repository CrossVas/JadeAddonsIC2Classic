package dev.crossvas.waila.ic2.providers;

import dev.crossvas.jadexic2c.base.JadeCommonHandler;
import dev.crossvas.jadexic2c.base.interfaces.IInfoProvider;
import dev.crossvas.jadexic2c.base.interfaces.IJadeHelper;
import ic2.core.block.personal.tile.TileEntityFluidOMat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

public class FluidOMatInfo implements IInfoProvider {

    public static final FluidOMatInfo THIS = new FluidOMatInfo();

    @Override
    public void addInfo(IJadeHelper helper, TileEntity blockEntity, EntityPlayer player) {
        if (blockEntity instanceof TileEntityFluidOMat) {
            JadeCommonHandler.addTankInfo(helper, blockEntity);
        }
    }
}

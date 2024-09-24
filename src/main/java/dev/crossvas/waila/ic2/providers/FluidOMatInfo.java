package dev.crossvas.waila.ic2.providers;

import dev.crossvas.waila.ic2.base.WailaCommonHandler;
import dev.crossvas.waila.ic2.base.interfaces.IInfoProvider;
import dev.crossvas.waila.ic2.base.interfaces.IWailaHelper;
import ic2.core.block.personal.TileEntityFluidOMat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

public class FluidOMatInfo implements IInfoProvider {

    public static final FluidOMatInfo THIS = new FluidOMatInfo();

    @Override
    public void addInfo(IWailaHelper helper, TileEntity blockEntity, EntityPlayer player) {
        if (blockEntity instanceof TileEntityFluidOMat) {
            WailaCommonHandler.addTankInfo(helper, blockEntity);
        }
    }
}

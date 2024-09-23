package dev.crossvas.waila.ic2.providers;

import dev.crossvas.jadexic2c.base.interfaces.IInfoProvider;
import dev.crossvas.jadexic2c.base.interfaces.IJadeHelper;
import ic2.core.block.machine.low.TileEntitySeedManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

public class CropLibraryInfo implements IInfoProvider {

    public static final CropLibraryInfo THIS = new CropLibraryInfo();

    @Override
    public void addInfo(IJadeHelper helper, TileEntity blockEntity, EntityPlayer player) {
        if (blockEntity instanceof TileEntitySeedManager) {
            TileEntitySeedManager cropLibrary = (TileEntitySeedManager) blockEntity;
            text(helper, tier(cropLibrary.tier));
            text(helper, maxIn(cropLibrary.maxInput));
            text(helper, usage(1));
        }
    }
}

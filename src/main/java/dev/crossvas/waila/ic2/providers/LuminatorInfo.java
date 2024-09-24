package dev.crossvas.waila.ic2.providers;

import dev.crossvas.waila.ic2.base.interfaces.IInfoProvider;
import dev.crossvas.waila.ic2.base.interfaces.IWailaHelper;
import ic2.api.energy.EnergyNet;
import ic2.core.block.wiring.TileEntityLuminator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

public class LuminatorInfo implements IInfoProvider {

    public static final LuminatorInfo THIS = new LuminatorInfo();

    @Override
    public void addInfo(IWailaHelper helper, TileEntity blockEntity, EntityPlayer player) {
        if (blockEntity instanceof TileEntityLuminator) {
            TileEntityLuminator luminator = (TileEntityLuminator) blockEntity;
            text(helper, tier(luminator.getSinkTier()));
            text(helper, maxIn((int) EnergyNet.instance.getPowerFromTier(luminator.getSinkTier())));
            text(helper, usage(1));
        }
    }
}

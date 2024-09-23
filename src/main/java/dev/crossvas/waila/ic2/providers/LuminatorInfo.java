package dev.crossvas.waila.ic2.providers;

import dev.crossvas.jadexic2c.base.interfaces.IInfoProvider;
import dev.crossvas.jadexic2c.base.interfaces.IJadeHelper;
import ic2.api.energy.EnergyNet;
import ic2.core.block.wiring.tile.TileEntityLuminator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

public class LuminatorInfo implements IInfoProvider {

    public static final LuminatorInfo THIS = new LuminatorInfo();

    @Override
    public void addInfo(IJadeHelper helper, TileEntity blockEntity, EntityPlayer player) {
        if (blockEntity instanceof TileEntityLuminator) {
            TileEntityLuminator luminator = (TileEntityLuminator) blockEntity;
            text(helper, tier(luminator.getSinkTier()));
            text(helper, maxIn((int) EnergyNet.instance.getPowerFromTier(luminator.getSinkTier())));
            text(helper, usage(1));
        }
    }
}

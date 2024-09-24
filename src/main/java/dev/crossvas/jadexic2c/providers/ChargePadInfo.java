package dev.crossvas.jadexic2c.providers;

import dev.crossvas.jadexic2c.base.interfaces.IInfoProvider;
import dev.crossvas.jadexic2c.base.interfaces.IJadeHelper;
import dev.crossvas.jadexic2c.utils.EnergyContainer;
import ic2.core.block.base.tile.TileEntityChargePadBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

public class ChargePadInfo implements IInfoProvider {

    public static final ChargePadInfo THIS = new ChargePadInfo();

    @Override
    public void addInfo(IJadeHelper helper, TileEntity blockEntity, EntityPlayer player) {
        if (blockEntity instanceof TileEntityChargePadBase) {
            TileEntityChargePadBase pad = (TileEntityChargePadBase) blockEntity;
            int maxIn = pad.maxInput;
            int transfer = pad.transferlimit;
            int range = pad.extraRange;

            text(helper, tier(pad.tier));
            text(helper, maxIn(maxIn));
            text(helper, translatable("probe.energy.transfer", transfer));
            text(helper, translatable("probe.block.range", range + 1));
            EnergyContainer container = EnergyContainer.getContainer(pad);
            addStats(helper, player, () -> addAveragesIn(helper, container));
        }
    }
}

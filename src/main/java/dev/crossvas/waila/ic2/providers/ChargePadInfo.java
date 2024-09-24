package dev.crossvas.waila.ic2.providers;

import dev.crossvas.waila.ic2.base.interfaces.IInfoProvider;
import dev.crossvas.waila.ic2.base.interfaces.IWailaHelper;
import dev.crossvas.waila.ic2.utils.EnergyContainer;
import ic2.core.block.wiring.TileEntityChargePad;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

public class ChargePadInfo implements IInfoProvider {

    public static final ChargePadInfo THIS = new ChargePadInfo();

    @Override
    public void addInfo(IWailaHelper helper, TileEntity blockEntity, EntityPlayer player) {
        if (blockEntity instanceof TileEntityChargePad) {
            TileEntityChargePad pad = (TileEntityChargePad) blockEntity;
            int maxIn = pad.maxInput;
            int transfer = pad.transferlimit;
            int range = pad.extraRange;

            text(helper, tier(pad.tier));
            text(helper, maxIn(maxIn));
            text(helper, translate("probe.energy.transfer", transfer));
            text(helper, translate("probe.block.range", range + 1));
            EnergyContainer container = EnergyContainer.getContainer(pad);
            addStats(helper, player, () -> addAveragesIn(helper, container));
        }
    }
}

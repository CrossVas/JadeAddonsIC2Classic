package dev.crossvas.lookingat.ic2c.base.providers;

import dev.crossvas.lookingat.ic2c.base.interfaces.IInfoProvider;
import dev.crossvas.lookingat.ic2c.base.interfaces.IHelper;
import dev.crossvas.lookingat.ic2c.helpers.EnergyContainer;
import ic2.api.energy.EnergyNet;
import ic2.core.block.base.tiles.impls.BaseChargePadTileEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class ChargePadInfo implements IInfoProvider {

    public static final ChargePadInfo THIS = new ChargePadInfo();

    @Override
    public void addInfo(IHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof BaseChargePadTileEntity chargePad) {
            int maxIn = chargePad.maxInput;
            int transfer = chargePad.transferLimit;
            float range = chargePad.range;

            defaultText(helper, "ic2.probe.eu.tier.name", EnergyNet.INSTANCE.getDisplayTier(chargePad.getSinkTier()));
            defaultText(helper, "ic2.probe.eu.max_in.name", maxIn);
            defaultText(helper, "ic2.probe.chargepad.transferrate.name", transfer);
            defaultText(helper, "ic2.probe.chargepad.radius.name", range + 1.0F);
            EnergyContainer container = EnergyContainer.getContainer(chargePad);
            addAveragesIn(helper, container);
        }
    }
}

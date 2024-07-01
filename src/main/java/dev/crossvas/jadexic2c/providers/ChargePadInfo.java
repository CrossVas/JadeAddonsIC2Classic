package dev.crossvas.jadexic2c.providers;

import dev.crossvas.jadexic2c.base.IInfoProvider;
import dev.crossvas.jadexic2c.base.IJadeHelper;
import dev.crossvas.jadexic2c.helpers.EnergyContainer;
import ic2.api.energy.EnergyNet;
import ic2.core.block.base.tiles.impls.BaseChargePadTileEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class ChargePadInfo implements IInfoProvider {

    public static final ChargePadInfo THIS = new ChargePadInfo();

    @Override
    public void addInfo(IJadeHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof BaseChargePadTileEntity chargePad) {
            int maxIn = chargePad.maxInput;
            int transfer = chargePad.transferLimit;
            float range = chargePad.range;

            text(helper, "ic2.probe.eu.tier.name", EnergyNet.INSTANCE.getDisplayTier(chargePad.getSinkTier()));
            text(helper, "ic2.probe.eu.max_in.name", maxIn);
            text(helper, "ic2.probe.chargepad.transferrate.name", transfer);
            text(helper, "ic2.probe.chargepad.radius.name", range + 1.0F);
            EnergyContainer container = EnergyContainer.getContainer(chargePad);
            addAveragesIn(helper, container);
        }
    }
}

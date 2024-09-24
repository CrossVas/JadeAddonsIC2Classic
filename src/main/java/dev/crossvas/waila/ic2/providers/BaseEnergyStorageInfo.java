package dev.crossvas.waila.ic2.providers;

import dev.crossvas.waila.ic2.base.interfaces.IInfoProvider;
import dev.crossvas.waila.ic2.base.interfaces.IWailaHelper;
import dev.crossvas.waila.ic2.utils.EnergyContainer;
import ic2.api.energy.EnergyNet;
import ic2.core.block.wiring.TileEntityElectricBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

public class BaseEnergyStorageInfo implements IInfoProvider {

    public static final BaseEnergyStorageInfo THIS = new BaseEnergyStorageInfo();

    @Override
    public void addInfo(IWailaHelper helper, TileEntity blockEntity, EntityPlayer player) {
        if (blockEntity instanceof TileEntityElectricBlock) {
            TileEntityElectricBlock electricBlock = (TileEntityElectricBlock) blockEntity;
            text(helper, tier(electricBlock.getTier()));
            text(helper, translate("probe.energy.input.max", (int) EnergyNet.instance.getPowerFromTier(electricBlock.tier)));
            text(helper, translate("probe.energy.output.max", electricBlock.getOutputEnergyUnitsPerTick()));
            EnergyContainer container = EnergyContainer.getContainer(electricBlock);
            addStats(helper, player, () -> addAveragesFull(helper, container));
        }
    }
}

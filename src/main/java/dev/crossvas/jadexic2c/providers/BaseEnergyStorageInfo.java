package dev.crossvas.jadexic2c.providers;

import dev.crossvas.jadexic2c.base.interfaces.IInfoProvider;
import dev.crossvas.jadexic2c.base.interfaces.IJadeHelper;
import dev.crossvas.jadexic2c.utils.EnergyContainer;
import ic2.api.energy.EnergyNet;
import ic2.core.block.base.tile.TileEntityElectricBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

public class BaseEnergyStorageInfo implements IInfoProvider {

    public static final BaseEnergyStorageInfo THIS = new BaseEnergyStorageInfo();

    @Override
    public void addInfo(IJadeHelper helper, TileEntity blockEntity, EntityPlayer player) {
        if (blockEntity instanceof TileEntityElectricBlock) {
            TileEntityElectricBlock electricBlock = (TileEntityElectricBlock) blockEntity;
            text(helper, tier(electricBlock.getTier()));
            text(helper, translatable("probe.energy.input.max", (int) EnergyNet.instance.getPowerFromTier(electricBlock.tier)));
            text(helper, translatable("probe.energy.output.max", electricBlock.getMaxSendingEnergy()));
            EnergyContainer container = EnergyContainer.getContainer(electricBlock);
            addStats(helper, player, () -> addAveragesFull(helper, container));
        }
    }
}

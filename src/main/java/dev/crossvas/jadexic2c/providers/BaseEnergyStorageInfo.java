package dev.crossvas.jadexic2c.providers;

import dev.crossvas.jadexic2c.base.interfaces.IInfoProvider;
import dev.crossvas.jadexic2c.base.interfaces.IJadeHelper;
import dev.crossvas.jadexic2c.utils.EnergyContainer;
import ic2.api.energy.EnergyNet;
import ic2.core.block.base.tile.TileEntityElectricBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;

public class BaseEnergyStorageInfo implements IInfoProvider {

    public static final BaseEnergyStorageInfo THIS = new BaseEnergyStorageInfo();

    @Override
    public void addInfo(IJadeHelper helper, TileEntity blockEntity, EntityPlayer player) {
        if (blockEntity instanceof TileEntityElectricBlock) {
            TileEntityElectricBlock electricBlock = (TileEntityElectricBlock) blockEntity;
            text(helper, translatable("tileInfo.euTier.name", getDisplayTier(electricBlock.tier)));
            text(helper, translatable("itemInfo.electricMaxIn.name", (int) EnergyNet.instance.getPowerFromTier(electricBlock.tier)));
            text(helper, translatable("container.energyStorageOutput.name", electricBlock.getMaxSendingEnergy()));
            EnergyContainer container = EnergyContainer.getContainer(electricBlock);
            if (player.isSneaking()) {
                text(helper, translatable("ic2.probe.energy.stats.info").setStyle(new Style().setColor(TextFormatting.GREEN)), true);
                addAveragesFull(helper, container);
            } else {
                text(helper, translatable("ic2.probe.sneak.info").setStyle(new Style().setColor(TextFormatting.AQUA)), true);
            }
        }
    }
}

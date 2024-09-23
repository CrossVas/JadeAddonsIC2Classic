package dev.crossvas.jadexic2c.providers;

import dev.crossvas.jadexic2c.base.JadeCommonHandler;
import dev.crossvas.jadexic2c.base.interfaces.IInfoProvider;
import dev.crossvas.jadexic2c.base.interfaces.IJadeHelper;
import dev.crossvas.jadexic2c.utils.ColorUtils;
import ic2.core.block.machine.low.TileEntityStoneMacerator;
import ic2.core.block.machine.low.TileEntityWoodGasser;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

public class StoneMachineInfo implements IInfoProvider {

    public static final StoneMachineInfo THIS = new StoneMachineInfo();

    @Override
    public void addInfo(IJadeHelper helper, TileEntity blockEntity, EntityPlayer player) {
        if (blockEntity instanceof TileEntityStoneMacerator) {
            TileEntityStoneMacerator macerator = (TileEntityStoneMacerator) blockEntity;
            if (macerator.getFuel() > 0) {
                bar(helper, (int) macerator.getFuel(), (int) macerator.getMaxFuel(), translatable("probe.storage.fuel", (int) macerator.getFuel()), ColorUtils.DARK_GRAY);
            }
            if (macerator.getProgress() > 0) {
                bar(helper, (int) macerator.getProgress(), (int) macerator.getMaxProgress(), translatable("probe.progress.full.name", (int) macerator.getProgress(), (int) macerator.getMaxProgress()), -16733185);
            }
        }
        if (blockEntity instanceof TileEntityWoodGasser) {
            TileEntityWoodGasser gasser = (TileEntityWoodGasser) blockEntity;
            if (gasser.getFuel() > 0) {
                bar(helper, (int) gasser.getFuel(), (int) gasser.getMaxFuel(), translatable("probe.storage.fuel", (int) gasser.getFuel()), ColorUtils.DARK_GRAY);
            }
            JadeCommonHandler.addTankInfo(helper, gasser);
            if (gasser.getProgress() > 0) {
                bar(helper, (int) gasser.getProgress(), (int) gasser.getMaxProgress(), translatable("probe.progress.full.name", (int) gasser.getProgress(), (int) gasser.getMaxProgress()), -16733185);
            }
        }
    }
}

package dev.crossvas.waila.ic2.providers;

import dev.crossvas.waila.ic2.base.interfaces.IInfoProvider;
import dev.crossvas.waila.ic2.base.interfaces.IWailaHelper;
import dev.crossvas.waila.ic2.utils.ColorUtils;
import dev.crossvas.waila.ic2.utils.Formatter;
import ic2.api.energy.tile.IEnergyContainer;
import ic2.core.block.TileEntityBlock;
import ic2.core.block.machine.tileentity.TileEntityCharged;
import ic2.core.block.machine.tileentity.TileEntityElectrolyzer;
import ic2.core.block.machine.tileentity.TileEntityMatter;
import ic2.core.block.machine.tileentity.TileEntityTeleporter;
import ic2.core.block.wiring.TileEntityCreativeStorage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

public class EUStorageInfo implements IInfoProvider {

    public static final EUStorageInfo THIS = new EUStorageInfo();

    @Override
    public void addInfo(IWailaHelper helper, TileEntity blockEntity, EntityPlayer player) {
        if (blockEntity instanceof TileEntityBlock) {
            TileEntityBlock machine = (TileEntityBlock) blockEntity;
            if (machine instanceof TileEntityCreativeStorage) {
                bar(helper, 1, 1, translate("probe.energy.storage.name", "Infinite"), ColorUtils.RED);
            } else if (machine instanceof IEnergyContainer && !(machine instanceof TileEntityElectrolyzer || machine instanceof TileEntityCharged || machine instanceof TileEntityMatter)) {
                IEnergyContainer storage = (IEnergyContainer) blockEntity;
                int stored = storage.getStoredEnergy();
                int max = storage.getEnergyCapacity();
                bar(helper, stored, max, translate("probe.energy.storage.full.name", Formatter.formatInt(stored, 4), Formatter.formatInt(max, 4)), ColorUtils.RED);
            } else if (machine instanceof TileEntityTeleporter) {
                TileEntityTeleporter tp = (TileEntityTeleporter) machine;
                bar(helper, tp.getAvailableEnergy(), tp.getAvailableEnergy(), translate("probe.energy.storage.name", Formatter.formatInt(tp.getAvailableEnergy(), 4)), ColorUtils.RED);
            }
        }
    }
}

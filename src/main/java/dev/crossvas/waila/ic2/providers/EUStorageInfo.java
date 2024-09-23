package dev.crossvas.waila.ic2.providers;

import dev.crossvas.jadexic2c.base.interfaces.IInfoProvider;
import dev.crossvas.jadexic2c.base.interfaces.IJadeHelper;
import dev.crossvas.jadexic2c.utils.ColorUtils;
import dev.crossvas.jadexic2c.utils.Formatter;
import ic2.api.classic.tile.machine.IEUStorage;
import ic2.core.block.base.tile.TileEntityBlock;
import ic2.core.block.machine.high.TileEntityMassFabricator;
import ic2.core.block.machine.high.TileEntityTeleporter;
import ic2.core.block.machine.low.TileEntityElectrolyzer;
import ic2.core.block.machine.med.TileEntityChargedElectrolyzer;
import ic2.core.block.wiring.tile.TileEntityCreativeEnergyStorage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

public class EUStorageInfo implements IInfoProvider {

    public static final EUStorageInfo THIS = new EUStorageInfo();

    @Override
    public void addInfo(IJadeHelper helper, TileEntity blockEntity, EntityPlayer player) {
        if (blockEntity instanceof TileEntityBlock) {
            TileEntityBlock machine = (TileEntityBlock) blockEntity;
            if (machine instanceof TileEntityCreativeEnergyStorage) {
                bar(helper, 1, 1, translatable("probe.energy.storage.name", "Infinite"), ColorUtils.RED);
            } else if (machine instanceof IEUStorage && !(machine instanceof TileEntityElectrolyzer || machine instanceof TileEntityChargedElectrolyzer || machine instanceof TileEntityMassFabricator)) {
                IEUStorage storage = (IEUStorage) blockEntity;
                bar(helper, storage.getStoredEU(), storage.getMaxEU(), translatable("probe.energy.storage.full.name", Formatter.formatInt(storage.getStoredEU(), 4), Formatter.formatInt(storage.getMaxEU(), 4)), ColorUtils.RED);
            } else if (machine instanceof TileEntityTeleporter) {
                TileEntityTeleporter tp = (TileEntityTeleporter) machine;
                bar(helper, tp.getAvailableEnergy(), tp.getAvailableEnergy(), translatable("probe.energy.storage.name", Formatter.formatInt(tp.getAvailableEnergy(), 4)), ColorUtils.RED);
            }
        }
    }
}

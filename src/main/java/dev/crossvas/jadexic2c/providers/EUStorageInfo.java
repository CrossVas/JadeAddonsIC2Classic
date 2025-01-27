package dev.crossvas.jadexic2c.providers;

import dev.crossvas.jadexic2c.base.JadeHelper;
import dev.crossvas.jadexic2c.base.interfaces.IInfoProvider;
import dev.crossvas.jadexic2c.helpers.Formatter;
import ic2.api.tiles.readers.IEUStorage;
import ic2.core.block.base.tiles.BaseTileEntity;
import ic2.core.block.machines.tiles.hv.MassFabricatorTileEntity;
import ic2.core.block.machines.tiles.hv.TeleporterTileEntity;
import ic2.core.block.machines.tiles.lv.ElectrolyzerTileEntity;
import ic2.core.block.machines.tiles.mv.ChargedElectrolyzerTileEntity;
import ic2.core.block.storage.tiles.CreativeSourceTileEntity;
import ic2.core.utils.math.ColorUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class EUStorageInfo implements IInfoProvider {

    public static final EUStorageInfo THIS = new EUStorageInfo();

    @Override
    public void addInfo(JadeHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof BaseTileEntity baseTile) {
            if (baseTile instanceof CreativeSourceTileEntity) {
                helper.bar(1, 1, translate("ic2.probe.eu.storage.name", "Infinite").withStyle(ChatFormatting.WHITE), ColorUtils.CYAN);
            } else if (baseTile instanceof IEUStorage storage && !(baseTile instanceof ElectrolyzerTileEntity || baseTile instanceof ChargedElectrolyzerTileEntity || baseTile instanceof MassFabricatorTileEntity)) {
                helper.bar(storage.getStoredEU(), storage.getMaxEU(), translate("ic2.probe.eu.storage.full.name", Formatter.formatInt(storage.getStoredEU(), 4), Formatter.formatInt(storage.getMaxEU(), 4)), ColorUtils.CYAN);
            } else if (baseTile instanceof TeleporterTileEntity tp) {
                helper.bar((int) tp.getAvailableEnergy(), (int) tp.getMaxEnergy(), translate("ic2.probe.eu.storage.name", Formatter.formatInt((int) tp.getAvailableEnergy(), 4)).withStyle(ChatFormatting.WHITE), ColorUtils.CYAN);
            }
        }
    }
}

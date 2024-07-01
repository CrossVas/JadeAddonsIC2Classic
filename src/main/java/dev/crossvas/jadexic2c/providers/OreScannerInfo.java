package dev.crossvas.jadexic2c.providers;

import dev.crossvas.jadexic2c.base.IInfoProvider;
import dev.crossvas.jadexic2c.base.IJadeHelper;
import ic2.api.energy.EnergyNet;
import ic2.core.block.machines.tiles.hv.OreScannerTileEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class OreScannerInfo implements IInfoProvider {

    public static final OreScannerInfo THIS = new OreScannerInfo();

    @Override
    public void addInfo(IJadeHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof OreScannerTileEntity scanner) {
            text(helper, "ic2.probe.eu.tier.name", EnergyNet.INSTANCE.getDisplayTier(scanner.getTier()));
            text(helper, "ic2.probe.eu.max_in.name", scanner.getMaxInput());
            text(helper, "ic2.probe.eu.usage.name", 1000);

            int blocks = scanner.blocks;
            int maxBlocks = scanner.maxBlocks;

            if (blocks > 0) {
                helper.addBarElement(blocks, maxBlocks, Component.translatable("ic2.probe.progress.full.name", blocks / 25 / 20, maxBlocks / 25 / 20).append("s"), -16733185);
            }
        }
    }
}

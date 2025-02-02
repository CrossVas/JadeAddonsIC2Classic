package dev.crossvas.jadexic2c.providers;

import dev.crossvas.jadexic2c.base.JadeHelper;
import dev.crossvas.jadexic2c.base.interfaces.IInfoProvider;
import ic2.core.block.machines.tiles.hv.OreScannerTileEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class OreScannerInfo implements IInfoProvider {

    public static final OreScannerInfo THIS = new OreScannerInfo();

    @Override
    public void addInfo(JadeHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof OreScannerTileEntity scanner) {
            helper.maxIn(scanner.getMaxInput());
            helper.usage(1000);

            int blocks = scanner.blocks;
            int maxBlocks = scanner.maxBlocks;

            if (blocks > 0) {
                helper.bar(blocks, maxBlocks, translate("ic2.probe.progress.full.name", blocks / 25 / 20, maxBlocks / 25 / 20).append("s"), -16733185);
            }
        }
    }
}

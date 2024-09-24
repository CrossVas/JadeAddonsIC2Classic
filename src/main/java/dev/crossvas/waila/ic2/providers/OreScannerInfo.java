package dev.crossvas.waila.ic2.providers;

import dev.crossvas.waila.ic2.base.interfaces.IInfoProvider;
import dev.crossvas.waila.ic2.base.interfaces.IWailaHelper;
import dev.crossvas.waila.ic2.utils.ColorUtils;
import ic2.core.block.machine.tileentity.TileEntityOreScanner;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

public class OreScannerInfo implements IInfoProvider {

    public static final OreScannerInfo THIS = new OreScannerInfo();

    @Override
    public void addInfo(IWailaHelper helper, TileEntity blockEntity, EntityPlayer player) {
        if (blockEntity instanceof TileEntityOreScanner) {
            TileEntityOreScanner scanner = (TileEntityOreScanner) blockEntity;
            text(helper, tier(scanner.getSinkTier()));
            text(helper, maxIn(scanner.maxInput));
            text(helper, usage(1000));

            int blocks = scanner.currentBlocks;
            int maxBlocks = scanner.maxBlocks;

            if (blocks > 0) {
                bar(helper, blocks, maxBlocks, translate("probe.progress.full_misc.name", blocks / 25 / 20, maxBlocks / 25 / 20).appendText("s"), ColorUtils.PROGRESS);
            }
        }
    }
}

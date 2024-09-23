package dev.crossvas.jadexic2c.providers;

import dev.crossvas.jadexic2c.base.interfaces.IInfoProvider;
import dev.crossvas.jadexic2c.base.interfaces.IJadeHelper;
import ic2.core.block.machine.med.TileEntityOreScanner;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

public class OreScannerInfo implements IInfoProvider {

    public static final OreScannerInfo THIS = new OreScannerInfo();

    @Override
    public void addInfo(IJadeHelper helper, TileEntity blockEntity, EntityPlayer player) {
        if (blockEntity instanceof TileEntityOreScanner) {
            TileEntityOreScanner scanner = (TileEntityOreScanner) blockEntity;
            text(helper, tier(scanner.getTier()));
            text(helper, maxIn(scanner.maxInput));
            text(helper, usage(1000));

            int blocks = scanner.currentBlocks;
            int maxBlocks = scanner.maxBlocks;

            if (blocks > 0) {
                bar(helper, blocks, maxBlocks, translatable("probe.progress.full_misc.name", blocks / 25 / 20, maxBlocks / 25 / 20).appendText("s"), -16733185);
            }
        }
    }
}

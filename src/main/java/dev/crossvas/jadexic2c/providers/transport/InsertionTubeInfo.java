package dev.crossvas.jadexic2c.providers.transport;

import dev.crossvas.jadexic2c.base.interfaces.IInfoProvider;
import dev.crossvas.jadexic2c.base.interfaces.IJadeHelper;
import dev.crossvas.jadexic2c.helpers.TextFormatter;
import ic2.core.block.transport.item.tubes.InsertionTubeTileEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class InsertionTubeInfo implements IInfoProvider {

    public static final InsertionTubeInfo THIS = new InsertionTubeInfo();

    @Override
    public void addInfo(IJadeHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof InsertionTubeTileEntity insertionTube) {
            boolean insertExisting = insertionTube.onlyExistingOne;
            text(helper, TextFormatter.GOLD.translate("info.tube.existing", status(insertExisting)));
        }
    }
}

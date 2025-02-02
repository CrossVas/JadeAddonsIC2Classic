package ic2.jadeplugin.providers.transport;

import ic2.core.block.transport.item.tubes.InsertionTubeTileEntity;
import ic2.jadeplugin.base.JadeHelper;
import ic2.jadeplugin.base.interfaces.IInfoProvider;
import ic2.jadeplugin.helpers.TextFormatter;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class InsertionTubeInfo implements IInfoProvider {

    public static final InsertionTubeInfo THIS = new InsertionTubeInfo();

    @Override
    public void addInfo(JadeHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof InsertionTubeTileEntity insertionTube) {
            boolean insertExisting = insertionTube.onlyExistingOne;
            helper.text(TextFormatter.GOLD.translate("info.tube.existing", status(insertExisting)));
        }
    }
}

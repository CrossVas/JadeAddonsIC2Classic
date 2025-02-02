package ic2.jadeplugin.providers.transport;

import ic2.core.block.transport.item.tubes.PickupTubeTileEntity;
import ic2.jadeplugin.base.JadeHelper;
import ic2.jadeplugin.base.interfaces.IInfoProvider;
import ic2.jadeplugin.helpers.TextFormatter;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class PickupTubeInfo implements IInfoProvider {

    public static final PickupTubeInfo THIS = new PickupTubeInfo();

    @Override
    public void addInfo(JadeHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof PickupTubeTileEntity pickup) {
            boolean largeRadius = pickup.largeRadius;
            helper.paddingY(3);
            helper.text(TextFormatter.GOLD.translate("info.tube.radius", status(largeRadius)));
        }
    }
}

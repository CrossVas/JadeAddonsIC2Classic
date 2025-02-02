package ic2.jadeplugin.providers.transport;

import ic2.core.block.transport.item.tubes.TeleportTubeTileEntity;
import ic2.jadeplugin.base.JadeHelper;
import ic2.jadeplugin.base.interfaces.IInfoProvider;
import ic2.jadeplugin.helpers.TextFormatter;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class TeleportTubeInfo implements IInfoProvider {

    public static final TeleportTubeInfo THIS = new TeleportTubeInfo();

    @Override
    public void addInfo(JadeHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof TeleportTubeTileEntity tp) {
            String freq = tp.frequency;
            int state = tp.state;
            boolean isPrivate = (state & 4) != 0;
            boolean canSend = (state & 1) != 0;
            boolean canReceive = (state & 2) != 0;
            helper.text(TextFormatter.GOLD.translate("info.tube.private", status(isPrivate)));
            helper.text(TextFormatter.GOLD.translate("info.tube.send", status(canSend)));
            helper.text(TextFormatter.GOLD.translate("info.tube.receive", status(canReceive)));
            helper.text(TextFormatter.LIGHT_PURPLE.translate("info.tube.id", TextFormatter.YELLOW.literal(freq)));
        }
    }
}

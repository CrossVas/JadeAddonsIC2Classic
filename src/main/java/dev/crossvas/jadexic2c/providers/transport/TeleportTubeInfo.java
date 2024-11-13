package dev.crossvas.jadexic2c.providers.transport;

import dev.crossvas.jadexic2c.base.interfaces.IInfoProvider;
import dev.crossvas.jadexic2c.base.interfaces.IJadeHelper;
import dev.crossvas.jadexic2c.helpers.TextFormatter;
import ic2.core.block.transport.item.tubes.TeleportTubeTileEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class TeleportTubeInfo implements IInfoProvider {

    public static final TeleportTubeInfo THIS = new TeleportTubeInfo();

    @Override
    public void addInfo(IJadeHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof TeleportTubeTileEntity tp) {
            String freq = tp.frequency;
            int state = tp.state;
            boolean isPrivate = (state & 4) != 0;
            boolean canSend = (state & 1) != 0;
            boolean canReceive = (state & 2) != 0;
            text(helper, TextFormatter.GOLD.translate("info.tube.private", status(isPrivate)));
            text(helper, TextFormatter.GOLD.translate("info.tube.send", status(canSend)));
            text(helper, TextFormatter.GOLD.translate("info.tube.receive", status(canReceive)));
            text(helper, TextFormatter.LIGHT_PURPLE.translate("info.tube.id", TextFormatter.YELLOW.literal(freq)));
        }
    }
}

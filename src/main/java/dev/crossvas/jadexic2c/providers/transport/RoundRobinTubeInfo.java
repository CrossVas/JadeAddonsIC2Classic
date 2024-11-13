package dev.crossvas.jadexic2c.providers.transport;

import dev.crossvas.jadexic2c.base.interfaces.IInfoProvider;
import dev.crossvas.jadexic2c.base.interfaces.IJadeHelper;
import dev.crossvas.jadexic2c.helpers.PluginHelper;
import dev.crossvas.jadexic2c.helpers.TextFormatter;
import ic2.api.util.DirectionList;
import ic2.core.block.transport.item.tubes.RoundRobinTubeTileEntity;
import ic2.core.utils.helpers.SanityHelper;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class RoundRobinTubeInfo implements IInfoProvider {

    public static final RoundRobinTubeInfo THIS = new RoundRobinTubeInfo();

    @Override
    public void addInfo(IJadeHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof RoundRobinTubeTileEntity roundRobin) {
            paddingY(helper, 3);
            int[] size = roundRobin.cap;
            int currentIndex = roundRobin.currentIndex;
            int currentItem = roundRobin.currentItem;
            text(helper, TextFormatter.GOLD.translate("info.tube.round_robin"));
            text(helper, TextFormatter.GOLD.translate("ic2.probe.tube.robin.side", TextFormatter.getColor(currentIndex).component(DirectionList.getName(Direction.from3DDataValue(currentIndex)))));
            text(helper, TextFormatter.GOLD.translate("ic2.probe.tube.robin.count", TextFormatter.AQUA.literal(Math.max(0, currentItem) + "")));
            for (int i = 0; i < size.length; i++) {
                int count = size[i];
                if (count > 0) {
                    Direction side = Direction.from3DDataValue(i);
                    text(helper, TextFormatter.getColor(i).literal(SanityHelper.toPascalCase(side.getName()) + ": " + count));
                }
            }
        }
    }
}

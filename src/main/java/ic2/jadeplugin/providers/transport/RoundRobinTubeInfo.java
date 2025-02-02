package ic2.jadeplugin.providers.transport;

import ic2.api.util.DirectionList;
import ic2.core.block.transport.item.tubes.RoundRobinTubeTileEntity;
import ic2.core.utils.helpers.SanityHelper;
import ic2.jadeplugin.base.JadeHelper;
import ic2.jadeplugin.base.interfaces.IInfoProvider;
import ic2.jadeplugin.helpers.TextFormatter;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class RoundRobinTubeInfo implements IInfoProvider {

    public static final RoundRobinTubeInfo THIS = new RoundRobinTubeInfo();

    @Override
    public void addInfo(JadeHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof RoundRobinTubeTileEntity roundRobin) {
            helper.paddingY(3);
            int[] size = roundRobin.cap;
            int currentIndex = roundRobin.currentIndex;
            int currentItem = roundRobin.currentItem;
            helper.text(TextFormatter.GOLD.translate("info.tube.round_robin"));
            helper.text(TextFormatter.GOLD.translate("ic2.probe.tube.robin.side", TextFormatter.getColor(currentIndex).component(DirectionList.getName(Direction.from3DDataValue(currentIndex)))));
            helper.text(TextFormatter.GOLD.translate("ic2.probe.tube.robin.count", TextFormatter.AQUA.literal(Math.max(0, currentItem) + "")));
            for (int i = 0; i < size.length; i++) {
                int count = size[i];
                if (count > 0) {
                    Direction side = Direction.from3DDataValue(i);
                    helper.text(TextFormatter.getColor(i).literal(SanityHelper.toPascalCase(side.getName()) + ": " + count));
                }
            }
        }
    }
}

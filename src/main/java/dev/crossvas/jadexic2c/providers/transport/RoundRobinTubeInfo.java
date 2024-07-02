package dev.crossvas.jadexic2c.providers.transport;

import dev.crossvas.jadexic2c.base.interfaces.IInfoProvider;
import dev.crossvas.jadexic2c.base.interfaces.IJadeHelper;
import dev.crossvas.jadexic2c.helpers.PluginHelper;
import ic2.api.util.DirectionList;
import ic2.core.block.transport.item.tubes.RoundRobinTubeTileEntity;
import ic2.core.utils.helpers.SanityHelper;
import net.minecraft.ChatFormatting;
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
            text(helper, Component.translatable("ic2.probe.tube.robin.side", DirectionList.getName(Direction.from3DDataValue(currentIndex)).withStyle(PluginHelper.getColor(currentIndex))).withStyle(ChatFormatting.GOLD));
            text(helper, Component.translatable("ic2.probe.tube.robin.count", ChatFormatting.AQUA + String.valueOf(Math.max(0, currentItem))).withStyle(ChatFormatting.GOLD));
            text(helper, Component.translatable("ic2.tube.round_robin.info").withStyle(ChatFormatting.GOLD));
            for (int i = 0; i < size.length; i++) {
                int count = size[i];
                if (count > 0) {
                    Direction side = Direction.from3DDataValue(i);
                    text(helper, Component.literal(SanityHelper.toPascalCase(side.getName()) + ": " + count).withStyle(PluginHelper.getColor(i)));
                }
            }
        }
    }
}

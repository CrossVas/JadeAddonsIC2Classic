package dev.crossvas.lookingat.ic2c.base.providers.transport;

import dev.crossvas.lookingat.ic2c.base.interfaces.IInfoProvider;
import dev.crossvas.lookingat.ic2c.base.interfaces.IHelper;
import ic2.core.block.transport.item.tubes.TeleportTubeTileEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class TeleportTubeInfo implements IInfoProvider {

    public static final TeleportTubeInfo THIS = new TeleportTubeInfo();

    @Override
    public void addInfo(IHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof TeleportTubeTileEntity tp) {
            String freq = tp.frequency;
            int state = tp.state;
            boolean isPrivate = (state & 4) != 0;
            boolean canSend = (state & 1) != 0;
            boolean canReceive = (state & 2) != 0;
            text(helper, Component.translatable("ic2.tube.teleport.private", (isPrivate ? ChatFormatting.GREEN : ChatFormatting.RED) + String.valueOf(isPrivate)).withStyle(ChatFormatting.GOLD));
            text(helper, Component.translatable("ic2.tube.teleport.send", (canSend ? ChatFormatting.GREEN : ChatFormatting.RED) + String.valueOf(canSend)).withStyle(ChatFormatting.GOLD));
            text(helper, Component.translatable("ic2.tube.teleport.receive", (canReceive ? ChatFormatting.GREEN : ChatFormatting.RED) + String.valueOf(canReceive)).withStyle(ChatFormatting.GOLD));
            text(helper, Component.translatable("ic2.tube.teleport.info", ChatFormatting.YELLOW + freq).withStyle(ChatFormatting.LIGHT_PURPLE));
        }
    }
}

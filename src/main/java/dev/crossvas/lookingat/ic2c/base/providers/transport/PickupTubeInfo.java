package dev.crossvas.lookingat.ic2c.base.providers.transport;

import dev.crossvas.lookingat.ic2c.base.interfaces.IInfoProvider;
import dev.crossvas.lookingat.ic2c.base.interfaces.IHelper;
import ic2.core.block.transport.item.tubes.PickupTubeTileEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class PickupTubeInfo implements IInfoProvider {

    public static final PickupTubeInfo THIS = new PickupTubeInfo();

    @Override
    public void addInfo(IHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof PickupTubeTileEntity pickup) {
            boolean largeRadius = pickup.largeRadius;
            paddingY(helper, 3);
            text(helper, Component.translatable("ic2.tube.pickup.info", (largeRadius ? ChatFormatting.GREEN : ChatFormatting.RED) + String.valueOf(largeRadius)).withStyle(ChatFormatting.GOLD));
        }
    }
}

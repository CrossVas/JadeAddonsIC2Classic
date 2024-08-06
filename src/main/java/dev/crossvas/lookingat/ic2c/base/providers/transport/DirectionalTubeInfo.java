package dev.crossvas.lookingat.ic2c.base.providers.transport;

import dev.crossvas.lookingat.ic2c.base.interfaces.IInfoProvider;
import dev.crossvas.lookingat.ic2c.base.interfaces.IHelper;
import dev.crossvas.lookingat.ic2c.helpers.PluginHelper;
import ic2.core.block.transport.item.tubes.DirectionalTubeTileEntity;
import ic2.core.utils.helpers.SanityHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class DirectionalTubeInfo implements IInfoProvider {

    public static final DirectionalTubeInfo THIS = new DirectionalTubeInfo();

    @Override
    public void addInfo(IHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof DirectionalTubeTileEntity directional) {
            paddingY(helper, 3);
            int direction3DDataValue = directional.getFacing().get3DDataValue();
            String facingName = SanityHelper.toPascalCase(directional.getFacing().toString());
            text(helper, Component.translatable("ic2.tube.directional.info", PluginHelper.getColor(direction3DDataValue) + facingName).withStyle(ChatFormatting.GOLD));
            text(helper, Component.translatable("ic2.tube.blocked", (directional.isBlocked() ? ChatFormatting.GREEN : ChatFormatting.RED) + String.valueOf(directional.isBlocked())).withStyle(ChatFormatting.DARK_AQUA));
        }
    }
}

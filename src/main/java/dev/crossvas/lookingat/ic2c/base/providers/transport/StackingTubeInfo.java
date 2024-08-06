package dev.crossvas.lookingat.ic2c.base.providers.transport;

import dev.crossvas.lookingat.ic2c.base.interfaces.IInfoProvider;
import dev.crossvas.lookingat.ic2c.base.interfaces.IHelper;
import ic2.core.block.transport.item.tubes.StackingTubeTileEntity;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.List;

public class StackingTubeInfo implements IInfoProvider {

    public static final StackingTubeInfo THIS = new StackingTubeInfo();

    @Override
    public void addInfo(IHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof StackingTubeTileEntity stackingTube) {
            if (!stackingTube.cached.isEmpty()) {
                List<ItemStack> cached = new ObjectArrayList<>();
                for (StackingTubeTileEntity.StackingStack item : stackingTube.cached) {
                    cached.add(item.getStack());
                }
                addGrid(helper, cached, Component.translatable("ic2.probe.tube.cached").withStyle(ChatFormatting.GOLD));
            }
            boolean ignoreColored = stackingTube.ignoreColors;
            text(helper, Component.translatable("ic2.probe.tube.stacking.color").withStyle(ChatFormatting.GOLD)
                    .append((ignoreColored ? ChatFormatting.GREEN : ChatFormatting.RED) + String.valueOf(ignoreColored)));
            text(helper, Component.translatable("ic2.probe.tube.stacking.limit", Component.literal(stackingTube.stacking + "").withStyle(ChatFormatting.AQUA)).withStyle(ChatFormatting.GOLD));
        }
    }
}

package dev.crossvas.lookingat.ic2c.base.providers.transport;

import dev.crossvas.lookingat.ic2c.base.interfaces.IInfoProvider;
import dev.crossvas.lookingat.ic2c.base.interfaces.IHelper;
import ic2.core.block.transport.item.tubes.LimiterTubeTileEntity;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.List;

public class LimiterTubeInfo implements IInfoProvider {

    public static final LimiterTubeInfo THIS = new LimiterTubeInfo();

    @Override
    public void addInfo(IHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof LimiterTubeTileEntity limiter) {
            List<ItemStack> dyeStacks = new ObjectArrayList<>();
            for (DyeColor color : limiter.usedColors) {
                ItemStack stack = new ItemStack(DyeItem.byColor(color));
                dyeStacks.add(stack);
            }
            if (!dyeStacks.isEmpty()) {
                addGrid(helper, dyeStacks, Component.translatable("ic2.probe.tube.limiter.unblocked").withStyle(ChatFormatting.GOLD));
            }
        }
    }
}

package dev.crossvas.lookingat.ic2c.base.providers.transport;

import dev.crossvas.lookingat.ic2c.base.interfaces.IInfoProvider;
import dev.crossvas.lookingat.ic2c.base.interfaces.IHelper;
import ic2.core.block.transport.item.tubes.RequestTubeTileEntity;
import ic2.core.utils.helpers.StackUtil;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

public class RequestTubeInfo implements IInfoProvider {

    public static final RequestTubeInfo THIS = new RequestTubeInfo();

    @Override
    public void addInfo(IHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof RequestTubeTileEntity requester) {
            NonNullList<ItemStack> list;
            // general
            boolean redstoneControl = requester.redstoneRequest;
            text(helper, Component.translatable("ic2.tube.redstone.control", (redstoneControl ? ChatFormatting.GREEN : ChatFormatting.RED) + String.valueOf(redstoneControl)).withStyle(ChatFormatting.GOLD));
            // requests
            list = NonNullList.create();
            for (RequestTubeTileEntity.RequestEntry entry : requester.filters) {
                list.add(StackUtil.copyWithSize(entry.getStack(), entry.getAmount()));
            }
            if (!list.isEmpty()) {
                addGrid(helper, list, Component.translatable("ic2.probe.tube.requests").withStyle(ChatFormatting.GOLD));
            }
            // missing
            list = NonNullList.create();
            requester.getMissing(list);
            if (!list.isEmpty()) {
                addGrid(helper, list, Component.translatable("ic2.probe.tube.missing").withStyle(ChatFormatting.GOLD));
            }
            // requested
            list = NonNullList.create();
            for (Object2IntMap.Entry<ItemStack> entry : Object2IntMaps.fastIterable(requester.requested)) {
                list.add(StackUtil.copyWithSize(entry.getKey(), entry.getIntValue()));
            }
            if (!list.isEmpty()) {
                addGrid(helper, list, Component.translatable("ic2.probe.tube.requesting").withStyle(ChatFormatting.GOLD));
            }
            // insertion
            list = NonNullList.create();
            if (!requester.toInsert.isEmpty()) {
                list.addAll(requester.toInsert);
            }
            if (!list.isEmpty()) {
                addGrid(helper, list, Component.translatable("ic2.probe.tube.stuck").withStyle(ChatFormatting.GOLD));
            }
        }
    }
}

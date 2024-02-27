package dev.crossvas.jadexic2c.info.tubes;

import dev.crossvas.jadexic2c.JadeIC2CPluginHandler;
import dev.crossvas.jadexic2c.helpers.IHelper;
import dev.crossvas.jadexic2c.helpers.PluginHelper;
import dev.crossvas.jadexic2c.helpers.TextHelper;
import ic2.core.block.transport.item.tubes.RequestTubeTileEntity;
import ic2.core.utils.helpers.StackUtil;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

import java.util.ArrayList;
import java.util.List;

public class RequestTubeInfoProvider implements IHelper<BlockEntity> {

    public static final RequestTubeInfoProvider INSTANCE = new RequestTubeInfoProvider();

    public RequestTubeInfoProvider() {
    }

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (!shouldAddInfo(blockAccessor, "RequestTubeInfo")) {
            return;
        }

        CompoundTag tag = getData(blockAccessor, "RequestTubeInfo");
        if (blockAccessor.getBlockEntity() instanceof RequestTubeTileEntity) {
            // general
            boolean redstoneControl = tag.getBoolean("redstoneControl");
            TextHelper.text(iTooltip, Component.translatable("ic2.probe.tube.redstone.control").withStyle(ChatFormatting.GOLD).append((redstoneControl ? ChatFormatting.GREEN : ChatFormatting.RED) + String.valueOf(redstoneControl)));
            // requests
            ListTag requestsItemTag = tag.getList("RequestsItem", Tag.TAG_COMPOUND);
            List<ItemStack> requestsList = new ArrayList<>();
            requestsItemTag.forEach(request -> {
                CompoundTag requestsTag = (CompoundTag) request;
                ItemStack stack = ItemStack.of(requestsTag.getCompound("request"));
                stack.setCount(requestsTag.getInt("count"));
                requestsList.add(stack);
            });
            PluginHelper.grid(iTooltip, "ic2.probe.tube.requests", ChatFormatting.GOLD, requestsList);

            // missing
            ListTag missingItemTag = tag.getList("MissingItems", Tag.TAG_COMPOUND);
            List<ItemStack> missingList = new ArrayList<>();
            missingItemTag.forEach(missing -> {
                CompoundTag missingTag = (CompoundTag) missing;
                ItemStack stack = ItemStack.of(missingTag.getCompound("missing"));
                stack.setCount(missingTag.getInt("count"));
                missingList.add(stack);
            });
            PluginHelper.grid(iTooltip, "ic2.probe.tube.missing", ChatFormatting.GOLD, missingList);

            // requested
            ListTag requestedItemTag = tag.getList("RequestedItems", Tag.TAG_COMPOUND);
            List<ItemStack> requestedList = new ArrayList<>();
            requestedItemTag.forEach(requested -> {
                CompoundTag requestedTag = (CompoundTag) requested;
                ItemStack stack = ItemStack.of(requestedTag.getCompound("requested"));
                stack.setCount(requestedTag.getInt("count"));
                requestedList.add(stack);
            });
            PluginHelper.grid(iTooltip, "ic2.probe.tube.requesting", ChatFormatting.GOLD, requestedList);

            // insertion
            ListTag insertionItemTag = tag.getList("InsertionItems", Tag.TAG_COMPOUND);
            List<ItemStack> insertionList = new ArrayList<>();
            insertionItemTag.forEach(insertion -> {
                CompoundTag insertionTag = (CompoundTag) insertion;
                ItemStack stack = ItemStack.of(insertionTag.getCompound("inserted"));
                stack.setCount(insertionTag.getInt("count"));
                insertionList.add(stack);
            });
            PluginHelper.grid(iTooltip, "ic2.probe.tube.stuck", ChatFormatting.GOLD, insertionList);
        }
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, ServerPlayer serverPlayer, Level level, BlockEntity blockEntity, boolean b) {
        if (blockEntity instanceof RequestTubeTileEntity requestTube) {
            CompoundTag tag = new CompoundTag();
            ListTag itemsList = new ListTag();
            NonNullList<ItemStack> list = NonNullList.create();
            tag.putBoolean("redstoneControl", requestTube.redstoneRequest);
            for (RequestTubeTileEntity.RequestEntry requestEntry : requestTube.filters) {
                list.add(StackUtil.copyWithSize(requestEntry.getStack(), requestEntry.getAmount()));
            }

            for (ItemStack stack : list) {
                CompoundTag requestsTag = new CompoundTag();
                requestsTag.put("request", stack.save(new CompoundTag()));
                requestsTag.putInt("count", stack.getCount());
                itemsList.add(requestsTag);
            }
            if (!itemsList.isEmpty()) {
                tag.put("RequestsItem", itemsList);
            }

            // missing
            itemsList = new ListTag();
            list = NonNullList.create();
            requestTube.getMissing(list);

            for (ItemStack stack : list) {
                CompoundTag missingTag = new CompoundTag();
                missingTag.put("missing", stack.save(new CompoundTag()));
                missingTag.putInt("count", stack.getCount());
                itemsList.add(missingTag);
            }
            if (!itemsList.isEmpty()) {
                tag.put("MissingItems", itemsList);
            }

            // requested
            itemsList = new ListTag();
            list = NonNullList.create();
            for (Object2IntMap.Entry<ItemStack> entry : Object2IntMaps.fastIterable(requestTube.requested)) {
                list.add(StackUtil.copyWithSize(entry.getKey(), entry.getIntValue()));
            }

            for (ItemStack stack : list) {
                CompoundTag requestedTag = new CompoundTag();
                requestedTag.put("requested", stack.save(new CompoundTag()));
                requestedTag.putInt("count", stack.getCount());
                itemsList.add(requestedTag);
            }
            if (!itemsList.isEmpty()) {
                tag.put("RequestedItems", itemsList);
            }

            // insertion

            if (!requestTube.toInsert.isEmpty()) {
                for (ItemStack stack : requestTube.toInsert) {
                    CompoundTag insertedTag = new CompoundTag();
                    insertedTag.put("inserted", stack.save(new CompoundTag()));
                    insertedTag.putInt("count", stack.getCount());
                    itemsList.add(insertedTag);
                }
                tag.put("InsertionItems", itemsList);
            }
            compoundTag.put("RequestTubeInfo", tag);
        }
    }

    @Override
    public ResourceLocation getUid() {
        return JadeIC2CPluginHandler.EU_READER_INFO;
    }
}

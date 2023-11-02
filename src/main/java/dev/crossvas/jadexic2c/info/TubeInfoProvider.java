package dev.crossvas.jadexic2c.info;

import dev.crossvas.jadexic2c.IHelper;
import dev.crossvas.jadexic2c.JadeIC2CPluginHandler;
import dev.crossvas.jadexic2c.utils.Helpers;
import ic2.api.tiles.tubes.TransportedItem;
import ic2.core.block.base.tiles.BaseTileEntity;
import ic2.core.block.machines.recipes.ItemStackStrategy;
import ic2.core.block.transport.item.TubeTileEntity;
import ic2.core.block.transport.item.tubes.RequestTubeTileEntity;
import ic2.core.block.transport.item.tubes.StackingTubeTileEntity;
import ic2.core.utils.collection.NBTListWrapper;
import ic2.core.utils.helpers.StackUtil;
import it.unimi.dsi.fastutil.objects.Object2IntLinkedOpenCustomHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
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

public enum TubeInfoProvider implements IHelper {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (!shouldAddInfo(blockAccessor, "TubeInfo")) {
            return;
        }

        CompoundTag tag = getData(blockAccessor, "TubeInfo");
        if (blockAccessor.getBlockEntity() instanceof BaseTileEntity tile) {
            // transported
            if (tile instanceof TubeTileEntity tube) {
                Iterable<CompoundTag> itemsTagList = NBTListWrapper.wrap(tag.getList("TransportItems", 10), CompoundTag.class);
                List<ItemStack> transportedList = new ArrayList<>();
                itemsTagList.forEach(stackTag -> {
                    ItemStack stack = ItemStack.of(stackTag.getCompound("stack"));
                    stack.setCount(stackTag.getInt("count"));
                    transportedList.add(stack);
                });
                Helpers.grid(iTooltip, "ic2.probe.tube.transported", ChatFormatting.WHITE, transportedList);

                // cached
                if (tube instanceof StackingTubeTileEntity) {
                    Iterable<CompoundTag> stackedItemsTagList = NBTListWrapper.wrap(tag.getList("StackedItems", 10), CompoundTag.class);
                    List<ItemStack> cachedList = new ArrayList<>();
                    stackedItemsTagList.forEach(stacked -> {
                        ItemStack stack = ItemStack.of(stacked.getCompound("stacked"));
                        stack.setCount(stacked.getInt("count"));
                        cachedList.add(stack);
                    });
                    Helpers.grid(iTooltip, "ic2.probe.tube.cached", ChatFormatting.WHITE, cachedList);
                }

                if (tube instanceof RequestTubeTileEntity) {
                    // requests
                    Iterable<CompoundTag> stackedItemsTagList = NBTListWrapper.wrap(tag.getList("RequestsItem", 10), CompoundTag.class);
                    List<ItemStack> requestsList = new ArrayList<>();
                    stackedItemsTagList.forEach(request -> {
                        ItemStack stack = ItemStack.of(request.getCompound("request"));
                        stack.setCount(request.getInt("count"));
                        requestsList.add(stack);
                    });
                    Helpers.grid(iTooltip, "ic2.probe.tube.requests", ChatFormatting.WHITE, requestsList);

                    // missing
                    Iterable<CompoundTag> missingItemsTagList = NBTListWrapper.wrap(tag.getList("MissingItems", 10), CompoundTag.class);
                    List<ItemStack> missingList = new ArrayList<>();
                    missingItemsTagList.forEach(request -> {
                        ItemStack stack = ItemStack.of(request.getCompound("missing"));
                        stack.setCount(request.getInt("count"));
                        missingList.add(stack);
                    });
                    Helpers.grid(iTooltip, "ic2.probe.tube.missing", ChatFormatting.WHITE, missingList);

                    // requested
                    Iterable<CompoundTag> requestedItemsTagList = NBTListWrapper.wrap(tag.getList("RequestedItems", 10), CompoundTag.class);
                    List<ItemStack> requestedList = new ArrayList<>();
                    requestedItemsTagList.forEach(request -> {
                        ItemStack stack = ItemStack.of(request.getCompound("requested"));
                        stack.setCount(request.getInt("count"));
                        requestedList.add(stack);
                    });
                    Helpers.grid(iTooltip, "ic2.probe.tube.requesting", ChatFormatting.WHITE, requestedList);

                    // insertion
                    Iterable<CompoundTag> insertionItemsTagList = NBTListWrapper.wrap(tag.getList("InsertionItems", 10), CompoundTag.class);
                    List<ItemStack> insertionList = new ArrayList<>();
                    insertionItemsTagList.forEach(request -> {
                        ItemStack stack = ItemStack.of(request.getCompound("inserted"));
                        stack.setCount(request.getInt("count"));
                        insertionList.add(stack);
                    });
                    Helpers.grid(iTooltip, "ic2.probe.tube.stuck", ChatFormatting.WHITE, insertionList);
                }

            }
            // OMG, what is this
        }
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, ServerPlayer serverPlayer, Level level, BlockEntity blockEntity, boolean b) {
        CompoundTag tag = new CompoundTag();

        if (blockEntity instanceof BaseTileEntity tile) {
            if (tile instanceof TubeTileEntity tube) {
                ListTag itemsList;
                NonNullList<ItemStack> list;
                if (!tube.items.isEmpty()) {
                    itemsList = new ListTag();
                    list = NonNullList.create();
                    Object2IntLinkedOpenCustomHashMap<ItemStack> mapped = new Object2IntLinkedOpenCustomHashMap<>(ItemStackStrategy.INSTANCE);
                    int i = 0;

                    for(int m = tube.items.size(); i < m; ++i) {
                        TransportedItem item = tube.items.get(i);
                        mapped.addTo(StackUtil.copyWithSize(item.getServerStack(), 1), item.getServerStack().getCount());
                    }

                    NonNullList<ItemStack> finalList = list;
                    mapped.forEach((K, V) -> finalList.add(StackUtil.copyWithSize(K, V)));

                    for (ItemStack stack : list) {
                        CompoundTag stackTag = new CompoundTag();
                        stackTag.put("stack", stack.save(new CompoundTag()));
                        stackTag.putInt("count", stack.getCount());
                        itemsList.add(stackTag);
                    }
                    if (!itemsList.isEmpty()) {
                        tag.put("TransportItems", itemsList);
                    }
                }


                if (tube instanceof StackingTubeTileEntity stacking) {
                    // cached
                    if (!stacking.cached.isEmpty()) {
                        itemsList = new ListTag();
                        list = NonNullList.create();
                        for (StackingTubeTileEntity.StackingStack item : stacking.cached) {
                            list.add(StackUtil.copyWithSize(item.getStack(), item.getAmount()));
                        }

                        for (ItemStack stack : list) {
                            CompoundTag stackedTag = new CompoundTag();
                            stackedTag.put("stacked", stack.save(new CompoundTag()));
                            stackedTag.putInt("count", stack.getCount());
                            itemsList.add(stackedTag);
                        }
                        tag.put("StackedItems", itemsList);
                    }
                } else if (tube instanceof RequestTubeTileEntity requesting) {
                    // requests
                    itemsList = new ListTag();
                    list = NonNullList.create();
                    for (RequestTubeTileEntity.RequestEntry requestEntry : requesting.filters) {
                        list.add(StackUtil.copyWithSize(requestEntry.getStack(), requestEntry.getAmount()));
                    }

                    for (ItemStack stack : list) {
                        CompoundTag stackedTag = new CompoundTag();
                        stackedTag.put("request", stack.save(new CompoundTag()));
                        stackedTag.putInt("count", stack.getCount());
                        itemsList.add(stackedTag);
                    }
                    if (!itemsList.isEmpty()) {
                        tag.put("RequestsItem", itemsList);
                    }

                    // missing
                    itemsList = new ListTag();
                    list = NonNullList.create();
                    requesting.getMissing(list);

                    for (ItemStack stack : list) {
                        CompoundTag stackedTag = new CompoundTag();
                        stackedTag.put("missing", stack.save(new CompoundTag()));
                        stackedTag.putInt("count", stack.getCount());
                        itemsList.add(stackedTag);
                    }
                    if (!itemsList.isEmpty()) {
                        tag.put("MissingItems", itemsList);
                    }

                    // requested
                    itemsList = new ListTag();
                    list = NonNullList.create();
                    for (Object2IntMap.Entry<ItemStack> entry : Object2IntMaps.fastIterable(requesting.requested)) {
                        list.add(StackUtil.copyWithSize(entry.getKey(), entry.getIntValue()));
                    }

                    for (ItemStack stack : list) {
                        CompoundTag stackedTag = new CompoundTag();
                        stackedTag.put("requested", stack.save(new CompoundTag()));
                        stackedTag.putInt("count", stack.getCount());
                        itemsList.add(stackedTag);
                    }
                    if (!itemsList.isEmpty()) {
                        tag.put("RequestedItems", itemsList);
                    }

                    // insertion

                    if (!requesting.toInsert.isEmpty()) {
                        for (ItemStack stack : requesting.toInsert) {
                            CompoundTag stackedTag = new CompoundTag();
                            stackedTag.put("inserted", stack.save(new CompoundTag()));
                            stackedTag.putInt("count", stack.getCount());
                            itemsList.add(stackedTag);
                        }
                        tag.put("InsertionItems", itemsList);
                    }
                }
            }
        }

        compoundTag.put("TubeInfo", tag);
    }

    @Override
    public ResourceLocation getUid() {
        return JadeIC2CPluginHandler.EU_READER_INFO;
    }
}

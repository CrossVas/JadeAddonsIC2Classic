package dev.crossvas.jadexic2c.info;

import dev.crossvas.jadexic2c.IHelper;
import dev.crossvas.jadexic2c.JadeIC2CPluginHandler;
import dev.crossvas.jadexic2c.utils.Helpers;
import ic2.api.tiles.tubes.TransportedItem;
import ic2.core.block.base.tiles.BaseTileEntity;
import ic2.core.block.machines.recipes.ItemStackStrategy;
import ic2.core.block.transport.item.TubeTileEntity;
import ic2.core.block.transport.item.tubes.*;
import ic2.core.utils.collection.NBTListWrapper;
import ic2.core.utils.helpers.SanityHelper;
import ic2.core.utils.helpers.StackUtil;
import it.unimi.dsi.fastutil.objects.Object2IntLinkedOpenCustomHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec2;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum TubeInfoProvider implements IHelper<BlockEntity> {
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

                if (tube instanceof RoundRobinTubeTileEntity) {
                    Helpers.space_y(iTooltip, 3);
                    Helpers.text(iTooltip, Component.translatable("ic2.tube.round_robin.info").withStyle(ChatFormatting.GOLD));
                    int[] size = tag.getIntArray("size");
                    for (int i = 0; i < size.length; i++) {
                        int count = size[i];
                        if (count > 0) {
                            Direction side = Direction.from3DDataValue(i);
                            Helpers.text(iTooltip, Component.literal( SanityHelper.toPascalCase(side.getName()) + ": " + count).withStyle(getColor(i)));
                        }
                    }
                }

                if (tube instanceof DirectionalTubeTileEntity directional) {
                    Helpers.space_y(iTooltip, 3);
                    Direction facing = directional.getFacing();
                    String facingName = SanityHelper.toPascalCase(facing.getName());
                    Helpers.text(iTooltip, Component.translatable("ic2.tube.directional.info").withStyle(ChatFormatting.GOLD).append(getColor(facing.get3DDataValue()) + facingName));
                }

                if (tube instanceof FilterTubeTileEntity) {
                    Iterable<CompoundTag> filteredItemsTagList = NBTListWrapper.wrap(tag.getList("FilteredItems", 10), CompoundTag.class);
                    List<FilterTubeTileEntity.FilterEntry> filteredList = new ArrayList<>();
                    filteredItemsTagList.forEach(filter -> {
                        if (filter != null) {
                            filteredList.add(FilterTubeTileEntity.FilterEntry.read(filter.getCompound("filter")));
                        }
                    });
                    if (!filteredList.isEmpty()) {
                        Helpers.space_y(iTooltip, 3);
                        Object2ObjectOpenHashMap<Component, List<FilterTubeTileEntity.FilterEntry>> mappedFilter = new Object2ObjectOpenHashMap<>();
                        for (FilterTubeTileEntity.FilterEntry filterEntry : filteredList) {
                            Component side = getSides(filterEntry);
                            if (mappedFilter.containsKey(side)) {
                                List<FilterTubeTileEntity.FilterEntry> existing = new ArrayList<>(mappedFilter.get(side));
                                existing.add(filterEntry);
                                mappedFilter.put(side, existing);
                            } else {
                                mappedFilter.put(side, Collections.singletonList(filterEntry));
                            }
                        }

                        mappedFilter.keySet().forEach(side -> {
                            Helpers.text(iTooltip, Component.translatable("ic2.tube.filter.info").withStyle(ChatFormatting.GOLD));
                            for (FilterTubeTileEntity.FilterEntry entry : mappedFilter.get(side)) {
                                iTooltip.append(iTooltip.getElementHelper().item(entry.getStack()).translate(new Vec2(0, -5)));
                            }
                            iTooltip.append(iTooltip.getElementHelper().spacer(3, 0));
                            Helpers.appendText(iTooltip, "→ ");
                            Helpers.appendText(iTooltip, side);
                        });
                    }

                }

                if (tube instanceof FilteredExtractionTubeTileEntity) {
                    Helpers.space_y(iTooltip, 3);
                    Iterable<CompoundTag> extractionFilteredItemsTagList = NBTListWrapper.wrap(tag.getList("ExtractionFilteredItems", 10), CompoundTag.class);
                    List<FilteredExtractionTubeTileEntity.FilterEntry> extractionFilteredList = new ArrayList<>();
                    extractionFilteredItemsTagList.forEach(filter -> {
                        if (filter != null) {
                            extractionFilteredList.add(FilteredExtractionTubeTileEntity.FilterEntry.read(filter.getCompound("extractionFilter")));
                        }
                    });

                    if (!extractionFilteredList.isEmpty()) {
                        for (FilteredExtractionTubeTileEntity.FilterEntry entry : extractionFilteredList) {
                            boolean checkNBT = (entry.getFlags() & 16) != 0;
                            boolean checkFluid = (entry.getFlags() & 128) != 0;
                            boolean checkDurability = (entry.getFlags() & 256) != 0;
                            int keepItem = entry.getKeepItems();
                            Helpers.text(iTooltip, Component.translatable("ic2.tube.filter_no.info", extractionFilteredList.indexOf(entry) + 1).withStyle(ChatFormatting.GOLD));
                            iTooltip.append(iTooltip.getElementHelper().item(entry.getStack()).translate(new Vec2(0, -5)));
                            iTooltip.append(iTooltip.getElementHelper().text(Component.literal(" ")
                                    .append(checkNBT ? ChatFormatting.GREEN + "*nbt " : "")
                                    .append(checkFluid ? ChatFormatting.GREEN + "*fluid " : "")
                                    .append(checkDurability ? ChatFormatting.GREEN + "*meta " : "")
                                    .append(keepItem > 0 ? ChatFormatting.WHITE + "Keep: " + keepItem : "")));
                        }
                    }

                    boolean whitelist = tag.getBoolean("whitelist");
                    Helpers.text(iTooltip, Component.translatable("ic2.tube.extraction_filter_whitelist.info").withStyle(ChatFormatting.GOLD).append(" ")
                            .append((whitelist ? ChatFormatting.GREEN : ChatFormatting.RED) + String.valueOf(whitelist)));
                }

                if (tube instanceof TeleportTubeTileEntity) {
                    String freq = tag.getString("freq");
                    Helpers.space_y(iTooltip, 3);
                    Helpers.text(iTooltip, Component.translatable( "ic2.tube.teleport.info").withStyle(ChatFormatting.GOLD).append(ChatFormatting.YELLOW + freq));
                }

                if (tube instanceof PickupTubeTileEntity) {
                    boolean largeRadius = tag.getBoolean("largeRadius");
                    Helpers.space_y(iTooltip, 3);
                    Helpers.text(iTooltip, Component.translatable( "ic2.tube.pickup.info").withStyle(ChatFormatting.GOLD).append((largeRadius ? ChatFormatting.GREEN : ChatFormatting.RED) + String.valueOf(largeRadius)));
                }

                Iterable<CompoundTag> itemsTagList = NBTListWrapper.wrap(tag.getList("TransportItems", 10), CompoundTag.class);
                List<ItemStack> transportedList = new ArrayList<>();
                itemsTagList.forEach(stackTag -> {
                    ItemStack stack = ItemStack.of(stackTag.getCompound("stack"));
                    stack.setCount(stackTag.getInt("count"));
                    transportedList.add(stack);
                });
                Helpers.grid(iTooltip, "ic2.probe.tube.transported", ChatFormatting.WHITE, transportedList);
            }
            // OMG, what is this
        }
    }

    public static ChatFormatting getColor(int index) {
        return switch (index) {
            case 0 -> ChatFormatting.AQUA;
            case 1 -> ChatFormatting.RED;
            case 2 -> ChatFormatting.YELLOW;
            case 3 -> ChatFormatting.BLUE;
            case 4 -> ChatFormatting.LIGHT_PURPLE;
            case 5 -> ChatFormatting.GREEN;
            default -> ChatFormatting.WHITE;
        };
    }

    public static Component getSides(FilterTubeTileEntity.FilterEntry entry) {
        Component component = Component.empty();
        if (entry.getSides() != null) {
            String[] directionList = entry.getSides().toString().replaceAll("\\[", "").replaceAll("]", "")
                    .replaceAll("north", ChatFormatting.YELLOW + "N")
                    .replaceAll("south", ChatFormatting.BLUE + "S")
                    .replaceAll("east", ChatFormatting.GREEN + "E")
                    .replaceAll("west", ChatFormatting.LIGHT_PURPLE + "W")
                    .replaceAll("down", ChatFormatting.AQUA + "D")
                    .replaceAll("up", ChatFormatting.RED + "U").split(",", -1);

            for (String side : directionList) {
                component = component.copy().append(side);
            }
            return component;
        }
        return component;
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
                } else if (tube instanceof RoundRobinTubeTileEntity rrobin) {
                    tag.putIntArray("size", rrobin.cap);
                } else if (tube instanceof FilterTubeTileEntity filter) {
                    itemsList = new ListTag();
                    for (FilterTubeTileEntity.FilterEntry entry : filter.stacks) {
                        CompoundTag stackedTag = new CompoundTag();
                        stackedTag.put("filter", entry.write());
                        itemsList.add(stackedTag);
                    }
                    tag.put("FilteredItems", itemsList);
                } else if (tube instanceof FilteredExtractionTubeTileEntity filteredExtraction) {
                    itemsList = new ListTag();
                    for (FilteredExtractionTubeTileEntity.FilterEntry entry : filteredExtraction.filters) {
                        CompoundTag extractionFilteredTag = new CompoundTag();
                        extractionFilteredTag.put("extractionFilter", entry.save());
                        itemsList.add(extractionFilteredTag);
                    }
                    tag.put("ExtractionFilteredItems", itemsList);
                    // flags
                    tag.putBoolean("whitelist", filteredExtraction.whitelist);
                } else if (tube instanceof TeleportTubeTileEntity teleport) {
                    tag.putString("freq", teleport.frequency);
                } else if (tube instanceof PickupTubeTileEntity pickup) {
                    CompoundTag pickupTag = new CompoundTag();
                    pickup.saveAdditional(pickupTag);
                    tag.putBoolean("largeRadius", pickupTag.getBoolean("large"));
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

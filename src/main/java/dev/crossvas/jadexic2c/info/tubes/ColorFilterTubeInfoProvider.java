package dev.crossvas.jadexic2c.info.tubes;

import dev.crossvas.jadexic2c.JadePluginHandler;
import dev.crossvas.jadexic2c.base.interfaces.IHelper;
import dev.crossvas.jadexic2c.helpers.PluginHelper;
import dev.crossvas.jadexic2c.helpers.TextHelper;
import ic2.api.util.DirectionList;
import ic2.core.block.base.tiles.BaseTileEntity;
import ic2.core.block.transport.item.tubes.ColorFilterTubeTileEntity;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec2;
import net.minecraftforge.registries.ForgeRegistries;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ColorFilterTubeInfoProvider implements IHelper<BlockEntity> {

    public static final ColorFilterTubeInfoProvider INSTANCE = new ColorFilterTubeInfoProvider();

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (!shouldAddInfo(blockAccessor, "ColorTubeInfo")) {
            return;
        }

        CompoundTag tag = getData(blockAccessor, "ColorTubeInfo");
        if (blockAccessor.getBlockEntity() instanceof ColorFilterTubeTileEntity) {
            boolean invPrio = tag.getBoolean("invPrio");
            TextHelper.text(iTooltip, Component.translatable("ic2.probe.tube.inv_priority").withStyle(ChatFormatting.GOLD).append((invPrio ? ChatFormatting.GREEN : ChatFormatting.RED) + String.valueOf(invPrio)));
            // get data from server
            ListTag filteredItemsTagList = tag.getList("FilteredItems", Tag.TAG_COMPOUND);
            List<FilterEntry> filters = new ObjectArrayList<>();
            filteredItemsTagList.forEach(filterTag -> {
                CompoundTag filter = (CompoundTag) filterTag;
                filters.add(FilterEntry.read(filter.getCompound("filter")));
            });

            // sort it up
            if (!filters.isEmpty()) {
                PluginHelper.spacerY(iTooltip, 3);
                Object2ObjectOpenHashMap<Component, List<FilterEntry>> mappedFilters = new Object2ObjectOpenHashMap<>();
                for (FilterEntry entry : filters) {
                    Component side = getSides(entry);
                    if (mappedFilters.containsKey(side)) {
                        List<FilterEntry> existing = new ArrayList<>(mappedFilters.get(side));
                        existing.add(entry);
                        mappedFilters.put(side, existing);
                    } else {
                        if (side != null) {
                            mappedFilters.put(side, Collections.singletonList(entry));
                        }
                    }
                }
                // apply
                mappedFilters.keySet().forEach(side -> {
                    TextHelper.text(iTooltip, Component.translatable("ic2.tube.filter.info").withStyle(ChatFormatting.GOLD));
                    for (FilterEntry entry : mappedFilters.get(side)) {
                        iTooltip.append(iTooltip.getElementHelper().item(entry.getStack()).translate(new Vec2(0, -5)));
                    }
                    iTooltip.append(iTooltip.getElementHelper().spacer(3, 0));
                    TextHelper.appendText(iTooltip, Component.literal("→ ").withStyle(ChatFormatting.WHITE));
                    TextHelper.appendText(iTooltip, side);
                });
            }


        }
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, ServerPlayer serverPlayer, Level level, BlockEntity blockEntity, boolean b) {
        if (blockEntity instanceof BaseTileEntity base) {
            if (base instanceof ColorFilterTubeTileEntity color) {
                CompoundTag tag = new CompoundTag();
                ListTag itemsList = new ListTag();
                List<FilterEntry> filters = new ObjectArrayList<>();
                for (int i = 0; i < color.colorDirections.length; i++) {
                    // listing every dye item
                    ItemStack dyeStack = new ItemStack(DyeItem.byColor(DyeColor.byId(i)));
                    DirectionList directionList = DirectionList.ofNumber(color.colorDirections[i]);
                    if (!directionList.isEmpty()) { // add dye only if it has directions
                        filters.add(new FilterEntry(dyeStack, directionList));
                    }
                }
                for (FilterEntry entry : filters) {
                    CompoundTag filterTag = new CompoundTag();
                    filterTag.put("filter", entry.write());
                    itemsList.add(filterTag);
                }
                if (!itemsList.isEmpty()) {
                    tag.put("FilteredItems", itemsList);
                }
                tag.putBoolean("invPrio", color.invPriority);
                compoundTag.put("ColorTubeInfo", tag);
            }
        }
    }

    @Override
    public ResourceLocation getUid() {
        return JadePluginHandler.EU_READER_INFO;
    }

    public static Component getSides(FilterEntry entry) {
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

    public static class FilterEntry {
        ItemStack stack;
        DirectionList directions;

        public FilterEntry(ItemStack stack, DirectionList directions) {
            this.stack = stack;
            this.directions = directions;
        }

        public static FilterEntry read(CompoundTag data) {
            ItemStack stack = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(data.getString("item"))));
            return stack.isEmpty() ? null : new FilterEntry(stack, DirectionList.ofNumber(data.getInt("sides")));
        }

        public CompoundTag write() {
            CompoundTag nbt = new CompoundTag();
            nbt.putString("item", ForgeRegistries.ITEMS.getKey(this.stack.getItem()).toString());
            nbt.putByte("sides", (byte) this.directions.getCode());
            return nbt;
        }

        public ItemStack getStack() {
            return this.stack;
        }

        public DirectionList getSides() {
            return this.directions;
        }
    }
}

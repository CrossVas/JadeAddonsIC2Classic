package dev.crossvas.jadexic2c.info.tubes;

import dev.crossvas.jadexic2c.JadePluginHandler;
import dev.crossvas.jadexic2c.base.interfaces.IHelper;
import dev.crossvas.jadexic2c.helpers.PluginHelper;
import dev.crossvas.jadexic2c.helpers.TextHelper;
import ic2.core.block.transport.item.tubes.FilterTubeTileEntity;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec2;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FilterTubeInfoProvider implements IHelper<BlockEntity> {

    public static FilterTubeInfoProvider INSTANCE = new FilterTubeInfoProvider();

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (!shouldAddInfo(blockAccessor, "FilterTubeInfo")) {
            return;
        }

        CompoundTag tag = getData(blockAccessor, "FilterTubeInfo");
        if (blockAccessor.getBlockEntity() instanceof FilterTubeTileEntity) {
            boolean invPrio = tag.getBoolean("invPrio");
            TextHelper.text(iTooltip, Component.translatable("ic2.probe.tube.inv_priority").withStyle(ChatFormatting.GOLD).append((invPrio ? ChatFormatting.GREEN : ChatFormatting.RED) + String.valueOf(invPrio)));
            ListTag filteredItemsTagList = tag.getList("FilteredItems", Tag.TAG_COMPOUND);
            List<FilterTubeTileEntity.FilterEntry> filteredList = new ArrayList<>();
            filteredItemsTagList.forEach(filter -> {
                if (filter != null) {
                    CompoundTag filterTag = (CompoundTag) filter;
                    filteredList.add(FilterTubeTileEntity.FilterEntry.read(filterTag.getCompound("filter")));
                }
            });
            if (!filteredList.isEmpty()) {
                PluginHelper.spacerY(iTooltip, 3);
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
                    TextHelper.text(iTooltip, Component.translatable("ic2.tube.filter.info").withStyle(ChatFormatting.GOLD));
                    for (FilterTubeTileEntity.FilterEntry entry : mappedFilter.get(side)) {
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
        if (blockEntity instanceof FilterTubeTileEntity filterTube) {
            CompoundTag tag = new CompoundTag();
            ListTag itemsList = new ListTag();
            for (FilterTubeTileEntity.FilterEntry entry : filterTube.stacks) {
                CompoundTag filterTag = new CompoundTag();
                filterTag.put("filter", entry.write());
                itemsList.add(filterTag);
            }
            if (!itemsList.isEmpty()) {
                tag.put("FilteredItems", itemsList);
            }
            tag.putBoolean("invPrio", filterTube.invPriority);
            compoundTag.put("FilterTubeInfo", tag);
        }
    }

    @Override
    public ResourceLocation getUid() {
        return JadePluginHandler.EU_READER_INFO;
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
}

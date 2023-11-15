package dev.crossvas.jadexic2c.info.tubes;

import dev.crossvas.jadexic2c.IHelper;
import dev.crossvas.jadexic2c.JadeIC2CPluginHandler;
import dev.crossvas.jadexic2c.utils.Helpers;
import ic2.core.block.transport.item.tubes.FilteredExtractionTubeTileEntity;
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
import java.util.List;

public class FilteredExtractionTubeInfoProvider implements IHelper<BlockEntity> {

    public static FilteredExtractionTubeInfoProvider INSTANCE = new FilteredExtractionTubeInfoProvider();

    public FilteredExtractionTubeInfoProvider() {}

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (!shouldAddInfo(blockAccessor, "FilteredExtractionTubeInfo")) {
            return;
        }

        CompoundTag tag = getData(blockAccessor, "FilteredExtractionTubeInfo");
        if (blockAccessor.getBlockEntity() instanceof FilteredExtractionTubeTileEntity) {
            Helpers.space_y(iTooltip, 3);
            ListTag extractionFilteredItemsTagList = tag.getList("ExtractionFilteredItems", Tag.TAG_COMPOUND);
            List<FilteredExtractionTubeTileEntity.FilterEntry> extractionFilteredList = new ArrayList<>();
            extractionFilteredItemsTagList.forEach(filter -> {
                if (filter != null) {
                    CompoundTag entryTag = (CompoundTag) filter;
                    extractionFilteredList.add(FilteredExtractionTubeTileEntity.FilterEntry.read(entryTag.getCompound("extractionFilter")));
                }
            });

            List<FilteredExtractionTubeTileEntity.FilterEntry> generalFilter = new ArrayList<>();
            List<FilteredExtractionTubeTileEntity.FilterEntry> metaFilter = new ArrayList<>();

            if (!extractionFilteredList.isEmpty()) {
                for (FilteredExtractionTubeTileEntity.FilterEntry entry : extractionFilteredList) {
                    if (!hasFlags(entry)) {
                        generalFilter.add(entry);
                    } else {
                        metaFilter.add(entry);
                    }
                }
            }
            if (!generalFilter.isEmpty()) {
                grid(iTooltip, "ic2.tube.filter.info", ChatFormatting.GOLD, generalFilter);
            }

            if (!metaFilter.isEmpty()) {
                metaFilter.forEach(filter -> {
                    boolean checkNBT = (filter.getFlags() & 16) != 0;
                    boolean checkFluid = (filter.getFlags() & 128) != 0;
                    boolean checkDurability = (filter.getFlags() & 256) != 0;
                    int keepItem = filter.getKeepItems();
                    Helpers.text(iTooltip, Component.translatable("ic2.tube.meta_filter.info").withStyle(ChatFormatting.GOLD));
                    iTooltip.append(iTooltip.getElementHelper().item(filter.getStack()).translate(new Vec2(0, -5)));
                    iTooltip.append(iTooltip.getElementHelper().text(Component.literal(" ")
                            .append(checkNBT ? ChatFormatting.GREEN + "*nbt " : "")
                            .append(checkFluid ? ChatFormatting.GREEN + "*fluid " : "")
                            .append(checkDurability ? ChatFormatting.GREEN + "*meta " : "")
                            .append(keepItem > 0 ? ChatFormatting.WHITE + "Keep: " + keepItem : "")));
                });
            }

            boolean whitelist = tag.getBoolean("whitelist");
            Helpers.text(iTooltip, Component.translatable("ic2.tube.extraction_filter_whitelist.info").withStyle(ChatFormatting.GOLD).append(" ")
                    .append((whitelist ? ChatFormatting.GREEN : ChatFormatting.RED) + String.valueOf(whitelist)));
        }
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, ServerPlayer serverPlayer, Level level, BlockEntity blockEntity, boolean b) {
        if (blockEntity instanceof FilteredExtractionTubeTileEntity filteredExtractionTube) {
            CompoundTag tag = new CompoundTag();
            ListTag itemsList = new ListTag();
            for (FilteredExtractionTubeTileEntity.FilterEntry entry : filteredExtractionTube.filters) {
                CompoundTag extractionFilteredTag = new CompoundTag();
                extractionFilteredTag.put("extractionFilter", entry.save());
                itemsList.add(extractionFilteredTag);
            }
            if (!itemsList.isEmpty()) {
                tag.put("ExtractionFilteredItems", itemsList);
            }
            tag.putBoolean("whitelist", filteredExtractionTube.whitelist);
            compoundTag.put("FilteredExtractionTubeInfo", tag);
        }
    }

    @Override
    public ResourceLocation getUid() {
        return JadeIC2CPluginHandler.EU_READER_INFO;
    }

    public static boolean hasFlags(FilteredExtractionTubeTileEntity.FilterEntry entry) {
        boolean checkNBT = (entry.getFlags() & 16) != 0;
        boolean checkFluid = (entry.getFlags() & 128) != 0;
        boolean checkDurability = (entry.getFlags() & 256) != 0;
        int keepItem = entry.getKeepItems();
        return checkNBT || checkFluid || checkDurability || keepItem > 0;
    }

    public static void grid(ITooltip iTooltip, String text, ChatFormatting style, List<FilteredExtractionTubeTileEntity.FilterEntry> entryList) {
        int counter = 0;
        int size = 7;
        if (!entryList.isEmpty()) {
            Helpers.text(iTooltip, Component.translatable(text).withStyle(style));
            Helpers.space_y(iTooltip, 2);
            for (FilteredExtractionTubeTileEntity.FilterEntry entry : entryList) {
                if (counter <= size) {
                    iTooltip.append(iTooltip.getElementHelper().item(entry.getStack()));
                    counter++;
                    if (counter == size) {
                        counter = 0;
                        Helpers.text(iTooltip, "");
                    }
                }
            }
            Helpers.space_y(iTooltip, 2);
        }
    }
}

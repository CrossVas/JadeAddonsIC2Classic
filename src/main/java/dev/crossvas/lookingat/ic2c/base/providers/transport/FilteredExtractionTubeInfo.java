package dev.crossvas.lookingat.ic2c.base.providers.transport;

import dev.crossvas.lookingat.ic2c.base.interfaces.IInfoProvider;
import dev.crossvas.lookingat.ic2c.base.interfaces.IHelper;
import ic2.core.block.transport.item.tubes.FilteredExtractionTubeTileEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.ArrayList;
import java.util.List;

public class FilteredExtractionTubeInfo implements IInfoProvider {

    public static final FilteredExtractionTubeInfo THIS = new FilteredExtractionTubeInfo();

    @Override
    public void addInfo(IHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof FilteredExtractionTubeTileEntity filteredExtraction) {
            List<FilteredExtractionTubeTileEntity.FilterEntry> generalFilter = new ArrayList<>();
            List<FilteredExtractionTubeTileEntity.FilterEntry> metaFilter = new ArrayList<>();

            for (FilteredExtractionTubeTileEntity.FilterEntry entry : filteredExtraction.filters) {
                if (!hasFlags(entry)) {
                    generalFilter.add(entry);
                } else {
                    metaFilter.add(entry);
                }
            }

            if (!generalFilter.isEmpty()) {
                List<ItemStack> stacks = generalFilter.stream().map(FilteredExtractionTubeTileEntity.FilterEntry::getStack).toList();
                addGrid(helper, stacks, Component.translatable("ic2.tube.filter.info").withStyle(ChatFormatting.GOLD));
            }
            if (!metaFilter.isEmpty()) {
                metaFilter.forEach(filter -> {
                    boolean checkNBT = (filter.getFlags() & 16) != 0;
                    boolean checkFluid = (filter.getFlags() & 128) != 0;
                    boolean checkDurability = (filter.getFlags() & 256) != 0;
                    int keepItem = filter.getKeepItems();
                    text(helper, Component.translatable("ic2.tube.meta_filter.info").withStyle(ChatFormatting.GOLD));
                    appendItem(helper, filter.getStack());
                    appendText(helper, Component.literal(" ")
                            .append(checkNBT ? ChatFormatting.GREEN + "*nbt " : "")
                            .append(checkFluid ? ChatFormatting.GREEN + "*fluid " : "")
                            .append(checkDurability ? ChatFormatting.GREEN + "*meta " : "")
                            .append(keepItem > 0 ? ChatFormatting.WHITE + "Keep: " + keepItem : ""));
                });
            }

            boolean whitelist = filteredExtraction.whitelist;
            boolean redstoneControl = filteredExtraction.sensitive;
            text(helper, Component.translatable("ic2.tube.extraction_filter_whitelist.info", (whitelist ? ChatFormatting.GREEN : ChatFormatting.RED) + String.valueOf(whitelist)).withStyle(ChatFormatting.GOLD));
            text(helper, Component.translatable("ic2.tube.redstone.control", (redstoneControl ? ChatFormatting.GREEN : ChatFormatting.RED) + String.valueOf(redstoneControl)).withStyle(ChatFormatting.GOLD));
            if (redstoneControl) {
                boolean comparator = filteredExtraction.comparator;
                boolean pulse = filteredExtraction.pulse;
                text(helper, Component.translatable("ic2.tube.redstone.comparator", (comparator ? ChatFormatting.GREEN : ChatFormatting.RED) + String.valueOf(comparator)).withStyle(ChatFormatting.LIGHT_PURPLE));
                text(helper, Component.translatable("ic2.tube.redstone.pulse", (pulse ? ChatFormatting.GREEN : ChatFormatting.RED) + String.valueOf(pulse)).withStyle(ChatFormatting.LIGHT_PURPLE));
            }
        }
    }

    public static boolean hasFlags(FilteredExtractionTubeTileEntity.FilterEntry entry) {
        boolean checkNBT = (entry.getFlags() & 16) != 0;
        boolean checkFluid = (entry.getFlags() & 128) != 0;
        boolean checkDurability = (entry.getFlags() & 256) != 0;
        int keepItem = entry.getKeepItems();
        return checkNBT || checkFluid || checkDurability || keepItem > 0;
    }
}

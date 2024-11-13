package dev.crossvas.jadexic2c.providers.transport;

import dev.crossvas.jadexic2c.base.interfaces.IInfoProvider;
import dev.crossvas.jadexic2c.base.interfaces.IJadeHelper;
import dev.crossvas.jadexic2c.helpers.TextFormatter;
import ic2.core.block.transport.item.tubes.FilteredExtractionTubeTileEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.ArrayList;
import java.util.List;

public class FilteredExtractionTubeInfo implements IInfoProvider {

    public static final FilteredExtractionTubeInfo THIS = new FilteredExtractionTubeInfo();

    @Override
    public void addInfo(IJadeHelper helper, BlockEntity blockEntity, Player player) {
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
                addGrid(helper, stacks, TextFormatter.GOLD.translate("info.tube.filter"));
            }
            if (!metaFilter.isEmpty()) {
                metaFilter.forEach(filter -> {
                    boolean checkNBT = (filter.getFlags() & 16) != 0;
                    boolean checkFluid = (filter.getFlags() & 128) != 0;
                    boolean checkDurability = (filter.getFlags() & 256) != 0;
                    int keepItem = filter.getKeepItems();
                    TextFormatter meta = TextFormatter.GREEN;
                    text(helper, TextFormatter.GOLD.translate("info.tube.filter.meta"));
                    appendItem(helper, filter.getStack());
                    appendText(helper, Component.literal(" ")
                            .append(checkNBT ? meta.literal("*nbt ") : Component.empty())
                            .append(checkFluid ? meta.literal("*fluid ") : Component.empty())
                            .append(checkDurability ? meta.literal("*meta ") : Component.empty())
                            .append(keepItem > 0 ? TextFormatter.WHITE.translate("info.tube.keep", keepItem) : Component.empty()));
                });
            }

            boolean whitelist = filteredExtraction.whitelist;
            boolean redstoneControl = filteredExtraction.sensitive;
            text(helper, TextFormatter.GOLD.translate("info.tube.whitelist", status(whitelist)));
            text(helper, TextFormatter.GOLD.translate("info.tube.redstone", status(redstoneControl)));
            if (redstoneControl) {
                boolean comparator = filteredExtraction.comparator;
                boolean pulse = filteredExtraction.pulse;
                text(helper, TextFormatter.LIGHT_PURPLE.translate("info.tube.comparator", status(comparator)));
                text(helper, TextFormatter.LIGHT_PURPLE.translate("info.tube.pulse", status(pulse)));
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

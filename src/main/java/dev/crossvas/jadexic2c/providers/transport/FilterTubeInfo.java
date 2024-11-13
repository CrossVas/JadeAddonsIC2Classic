package dev.crossvas.jadexic2c.providers.transport;

import dev.crossvas.jadexic2c.base.interfaces.IInfoProvider;
import dev.crossvas.jadexic2c.base.interfaces.IJadeHelper;
import dev.crossvas.jadexic2c.helpers.PluginHelper;
import dev.crossvas.jadexic2c.helpers.TextFormatter;
import ic2.core.block.transport.item.tubes.FilterTubeTileEntity;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FilterTubeInfo implements IInfoProvider {

    public static final FilterTubeInfo THIS = new FilterTubeInfo();

    @Override
    public void addInfo(IJadeHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof FilterTubeTileEntity filterTube) {
            boolean invPriority = filterTube.invPriority;
            text(helper, TextFormatter.GOLD.translate("info.tube.inv.prio", status(invPriority)));

            List<FilterTubeTileEntity.FilterEntry> filterEntries = filterTube.stacks;
            Object2ObjectOpenHashMap<Component, List<FilterTubeTileEntity.FilterEntry>> mappedFilter = new Object2ObjectOpenHashMap<>();
            if (!filterEntries.isEmpty()) {
                paddingY(helper, 3);
                for (FilterTubeTileEntity.FilterEntry entry : filterEntries) {
                    Component side = PluginHelper.getSides(entry.getSides());
                    if (mappedFilter.containsKey(side)) {
                        List<FilterTubeTileEntity.FilterEntry> existing = new ArrayList<>(mappedFilter.get(side));
                        existing.add(entry);
                        mappedFilter.put(side, existing);
                    } else {
                        mappedFilter.put(side, Collections.singletonList(entry));
                    }
                }

                mappedFilter.keySet().forEach(side -> {
                    text(helper, TextFormatter.GOLD.translate("info.tube.filter"));
                    for (FilterTubeTileEntity.FilterEntry entry : mappedFilter.get(side)) {
                        appendPaddingX(helper, 2);
                        appendItem(helper, entry.getStack());
                    }
                    appendPaddingX(helper, 3);
                    appendText(helper, TextFormatter.WHITE.literal("→ "));
                    appendText(helper, side);
                });
            }

        }
    }
}

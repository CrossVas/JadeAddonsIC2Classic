package dev.crossvas.jadexic2c.providers.transport;

import dev.crossvas.jadexic2c.base.JadeHelper;
import dev.crossvas.jadexic2c.base.interfaces.IInfoProvider;
import dev.crossvas.jadexic2c.helpers.PluginHelper;
import dev.crossvas.jadexic2c.helpers.TextFormatter;
import ic2.api.util.DirectionList;
import ic2.core.block.transport.item.tubes.ColorFilterTubeTileEntity;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ColorFilterTubeInfo implements IInfoProvider {

    public static final ColorFilterTubeInfo THIS = new ColorFilterTubeInfo();

    @Override
    public void addInfo(JadeHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof ColorFilterTubeTileEntity colorFilterTube) {
            boolean invPrio = colorFilterTube.invPriority;
            helper.text(TextFormatter.GOLD.translate("info.tube.inv.prio", status(invPrio)));

            List<FilterEntry> filters = new ObjectArrayList<>();
            for (int i = 0; i < colorFilterTube.colorDirections.length; i++) {
                // listing every dye item
                ItemStack dyeStack = new ItemStack(DyeItem.byColor(DyeColor.byId(i)));
                DirectionList directionList = DirectionList.ofNumber(colorFilterTube.colorDirections[i]);
                if (!directionList.isEmpty()) { // add dye only if it has directions
                    filters.add(new FilterEntry(dyeStack, directionList));
                }
            }
            if (!filters.isEmpty()) {
                helper.paddingY(3);
                Object2ObjectOpenHashMap<Component, List<FilterEntry>> mappedFilters = new Object2ObjectOpenHashMap<>();
                for (FilterEntry entry : filters) {
                    Component side = PluginHelper.getSides(entry.directions);
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
                    helper.text(TextFormatter.GOLD.translate("info.tube.filter"));
                    for (FilterEntry entry : mappedFilters.get(side)) {
                        helper.appendItem(entry.stack);
                    }
                    helper.appendText(TextFormatter.WHITE.literal(" → "));
                    helper.appendText(side);
                });
            }
        }
    }

    public record FilterEntry(ItemStack stack, DirectionList directions) {}
}

package dev.crossvas.jadexic2c.providers.expansions;

import dev.crossvas.jadexic2c.base.interfaces.IInfoProvider;
import dev.crossvas.jadexic2c.base.interfaces.IJadeHelper;
import dev.crossvas.jadexic2c.helpers.TextFormatter;
import ic2.core.block.machines.tiles.nv.UUMatterExpansionTileEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.ArrayList;
import java.util.List;

public class UUMExpansionInfo implements IInfoProvider {

    public static final UUMExpansionInfo THIS = new UUMExpansionInfo();

    @Override
    public void addInfo(IJadeHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof UUMatterExpansionTileEntity uumExpansion) {
            int currentUUM = uumExpansion.uuMatter;
            int maxUUM = uumExpansion.maxUUMatter;
            if (currentUUM > 0) {
                bar(helper, currentUUM, maxUUM, TextFormatter.WHITE.translate("info.uum.storage", currentUUM / 1000, maxUUM / 1000), -5829955);
            } else {
                text(helper, TextFormatter.RED.translate("info.uum.missing"));
            }
            List<ItemStack> providing = new ArrayList<>();
            for (int i = 0; i < uumExpansion.filter.getSlotCount(); i++) {
                ItemStack filter = uumExpansion.filter.getStackInSlot(i);
                if (!filter.isEmpty()) {
                    providing.add(filter);
                }
            }
            if (!providing.isEmpty()) {
                addGrid(helper, providing, TextFormatter.YELLOW.translate("info.uum.providing"));
            }
        }
    }
}

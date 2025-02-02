package ic2.jadeplugin.providers.expansions;

import ic2.core.block.machines.tiles.nv.UUMatterExpansionTileEntity;
import ic2.jadeplugin.base.JadeHelper;
import ic2.jadeplugin.base.interfaces.IInfoProvider;
import ic2.jadeplugin.helpers.TextFormatter;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.ArrayList;
import java.util.List;

public class UUMExpansionInfo implements IInfoProvider {

    public static final UUMExpansionInfo THIS = new UUMExpansionInfo();

    @Override
    public void addInfo(JadeHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof UUMatterExpansionTileEntity uumExpansion) {
            int currentUUM = uumExpansion.uuMatter;
            int maxUUM = uumExpansion.maxUUMatter;
            if (currentUUM > 0) {
                helper.bar(currentUUM, maxUUM, TextFormatter.WHITE.translate("info.uum.storage", currentUUM / 1000, maxUUM / 1000), -5829955);
            } else {
                helper.text(TextFormatter.RED.translate("info.uum.missing"));
            }
            List<ItemStack> providing = new ArrayList<>();
            for (int i = 0; i < uumExpansion.filter.getSlotCount(); i++) {
                ItemStack filter = uumExpansion.filter.getStackInSlot(i);
                if (!filter.isEmpty()) {
                    providing.add(filter);
                }
            }
            if (!providing.isEmpty()) {
                helper.addGrid(providing, TextFormatter.YELLOW.translate("info.uum.providing"));
            }
        }
    }
}

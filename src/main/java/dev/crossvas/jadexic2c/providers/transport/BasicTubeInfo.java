package dev.crossvas.jadexic2c.providers.transport;

import dev.crossvas.jadexic2c.base.interfaces.IInfoProvider;
import dev.crossvas.jadexic2c.base.interfaces.IJadeHelper;
import dev.crossvas.jadexic2c.helpers.TextFormatter;
import ic2.api.tiles.tubes.TransportedItem;
import ic2.core.block.machines.recipes.ItemStackStrategy;
import ic2.core.block.transport.item.TubeTileEntity;
import ic2.core.utils.helpers.StackUtil;
import it.unimi.dsi.fastutil.objects.Object2IntLinkedOpenCustomHashMap;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

public class BasicTubeInfo implements IInfoProvider {

    public static final BasicTubeInfo THIS = new BasicTubeInfo();

    @Override
    public void addInfo(IJadeHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof TubeTileEntity tube) {
            int priority = tube.getPrioritySide();
            if (priority > 0) {
                text(helper, TextFormatter.GOLD.translate("info.tube.prio", TextFormatter.getColor(priority - 1).translate("misc.ic2.side." + Direction.from3DDataValue(priority - 1))));
            }
            if (!tube.synchronize) {
                text(helper, TextFormatter.RED.translate("ic2.probe.tube.invisible"));
            }

            NonNullList<ItemStack> list = NonNullList.create();
            if (!tube.items.isEmpty()) {
                Object2IntLinkedOpenCustomHashMap<ItemStack> mapped = new Object2IntLinkedOpenCustomHashMap<>(ItemStackStrategy.INSTANCE);
                int i = 0;

                for (int m = tube.items.size(); i < m; ++i) {
                    TransportedItem item = tube.items.get(i);
                    mapped.addTo(StackUtil.copyWithSize(item.getServerStack(), 1), item.getServerStack().getCount());
                }

                mapped.forEach((K, V) -> list.add(StackUtil.copyWithSize(K, V)));
                paddingY(helper, 3);
                addGrid(helper, list, TextFormatter.GOLD.translate("ic2.probe.tube.transported"));
            }
        }
    }
}

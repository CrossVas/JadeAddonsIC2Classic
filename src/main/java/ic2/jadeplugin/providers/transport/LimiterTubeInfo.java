package ic2.jadeplugin.providers.transport;

import ic2.core.block.transport.item.tubes.LimiterTubeTileEntity;
import ic2.jadeplugin.base.JadeHelper;
import ic2.jadeplugin.base.interfaces.IInfoProvider;
import ic2.jadeplugin.helpers.TextFormatter;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.List;

public class LimiterTubeInfo implements IInfoProvider {

    public static final LimiterTubeInfo THIS = new LimiterTubeInfo();

    @Override
    public void addInfo(JadeHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof LimiterTubeTileEntity limiter) {
            List<ItemStack> dyeStacks = new ObjectArrayList<>();
            for (DyeColor color : limiter.usedColors) {
                ItemStack stack = new ItemStack(DyeItem.byColor(color));
                dyeStacks.add(stack);
            }
            if (!dyeStacks.isEmpty()) {
                helper.addGrid(dyeStacks, TextFormatter.GOLD.translate("info.tube.colors"));
            }
        }
    }
}

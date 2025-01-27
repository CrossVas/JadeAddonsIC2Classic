package dev.crossvas.jadexic2c.providers.transport;

import dev.crossvas.jadexic2c.base.JadeHelper;
import dev.crossvas.jadexic2c.base.interfaces.IInfoProvider;
import dev.crossvas.jadexic2c.helpers.TextFormatter;
import ic2.core.block.transport.item.tubes.StackingTubeTileEntity;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.List;

public class StackingTubeInfo implements IInfoProvider {

    public static final StackingTubeInfo THIS = new StackingTubeInfo();

    @Override
    public void addInfo(JadeHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof StackingTubeTileEntity stackingTube) {
            if (!stackingTube.cached.isEmpty()) {
                List<ItemStack> cached = new ObjectArrayList<>();
                for (StackingTubeTileEntity.StackingStack item : stackingTube.cached) {
                    cached.add(item.getStack());
                }
                helper.addGrid( cached, TextFormatter.GOLD.translate("ic2.probe.tube.cached"));
            }
            boolean ignoreColored = stackingTube.ignoreColors;
            helper.text(TextFormatter.GOLD.translate("info.tube.ignore", status(ignoreColored)));
            helper.text(TextFormatter.GOLD.translate("ic2.probe.tube.stacking.limit", TextFormatter.AQUA.literal(stackingTube.stacking + "")));
        }
    }
}

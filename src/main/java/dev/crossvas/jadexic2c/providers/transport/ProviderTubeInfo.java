package dev.crossvas.jadexic2c.providers.transport;

import dev.crossvas.jadexic2c.base.interfaces.IInfoProvider;
import dev.crossvas.jadexic2c.base.interfaces.IJadeHelper;
import dev.crossvas.jadexic2c.helpers.TextFormatter;
import ic2.core.block.transport.item.tubes.ProviderTubeTileEntity;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.List;

public class ProviderTubeInfo implements IInfoProvider {

    public static final ProviderTubeInfo THIS = new ProviderTubeInfo();

    @Override
    public void addInfo(IJadeHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof ProviderTubeTileEntity provider) {
            List<ItemStack> providing = new ObjectArrayList<>();
            for (ProviderTubeTileEntity.ProvideEntry entry : provider.filters) {
                if (entry.isValid()) {
                    providing.add(entry.getStack());
                }
             }
            int globalKeep = provider.globalKeepItems;
            if (globalKeep > 0) {
                text(helper, TextFormatter.GOLD.translate("info.tube.keep.global", TextFormatter.AQUA.literal(globalKeep + "")));
            }
            boolean whiteList = provider.whiteList;
            text(helper, TextFormatter.GOLD.translate("info.tube.whitelist", status(whiteList)));
            boolean compareTag = provider.compareNBT;
            text(helper, TextFormatter.GOLD.translate("info.tube.nbt", status(compareTag)));
            boolean keepMode = provider.keepMode;
            text(helper, TextFormatter.GOLD.translate("info.tube.keep", status(keepMode)));
            if (!providing.isEmpty()) {
                addGrid(helper, providing, TextFormatter.GOLD.translate("info.tube.providing"));
            }
        }
    }
}

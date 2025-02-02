package ic2.jadeplugin.providers.transport;

import ic2.core.block.transport.item.tubes.ProviderTubeTileEntity;
import ic2.jadeplugin.base.JadeHelper;
import ic2.jadeplugin.base.interfaces.IInfoProvider;
import ic2.jadeplugin.helpers.TextFormatter;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.List;

public class ProviderTubeInfo implements IInfoProvider {

    public static final ProviderTubeInfo THIS = new ProviderTubeInfo();

    @Override
    public void addInfo(JadeHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof ProviderTubeTileEntity provider) {
            List<ItemStack> providing = new ObjectArrayList<>();
            for (ProviderTubeTileEntity.ProvideEntry entry : provider.filters) {
                if (entry.isValid()) {
                    providing.add(entry.getStack());
                }
             }
            int globalKeep = provider.globalKeepItems;
            if (globalKeep > 0) {
                helper.text(TextFormatter.GOLD.translate("info.tube.keep.global", TextFormatter.AQUA.literal(globalKeep + "")));
            }
            boolean whiteList = provider.whiteList;
            helper.text(TextFormatter.GOLD.translate("info.tube.whitelist", status(whiteList)));
            boolean compareTag = provider.compareNBT;
            helper.text(TextFormatter.GOLD.translate("info.tube.nbt", status(compareTag)));
            boolean keepMode = provider.keepMode;
            helper.text(TextFormatter.GOLD.translate("info.tube.keep", status(keepMode)));
            if (!providing.isEmpty()) {
                helper.addGrid(providing, TextFormatter.GOLD.translate("info.tube.providing"));
            }
        }
    }
}

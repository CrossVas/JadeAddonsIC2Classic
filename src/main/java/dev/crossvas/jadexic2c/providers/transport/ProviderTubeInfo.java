package dev.crossvas.jadexic2c.providers.transport;

import dev.crossvas.jadexic2c.base.interfaces.IInfoProvider;
import dev.crossvas.jadexic2c.base.interfaces.IJadeHelper;
import ic2.core.block.transport.item.tubes.ProviderTubeTileEntity;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
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
                text(helper, Component.literal("Global Keep: " + ChatFormatting.AQUA + globalKeep).withStyle(ChatFormatting.GOLD));
            }
            boolean whiteList = provider.whiteList;
            text(helper, Component.translatable("ic2.tube.extraction_filter_whitelist.info", (whiteList ? ChatFormatting.GREEN : ChatFormatting.RED) + String.valueOf(whiteList)).withStyle(ChatFormatting.GOLD));
            boolean compareTag = provider.compareNBT;
            text(helper, Component.literal("NBT: " + (compareTag ? ChatFormatting.GREEN : ChatFormatting.RED) + compareTag).withStyle(ChatFormatting.GOLD));
            boolean keepMode = provider.keepMode;
            text(helper, Component.literal("Keep: " + (keepMode ? ChatFormatting.GREEN : ChatFormatting.RED) + keepMode).withStyle(ChatFormatting.GOLD));
            if (!providing.isEmpty()) {
                addGrid(helper, providing, Component.translatable("ic2.probe.uum.providing.name").withStyle(ChatFormatting.GOLD));
            }
        }
    }
}

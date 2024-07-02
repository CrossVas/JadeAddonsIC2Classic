package dev.crossvas.jadexic2c.providers.transport;

import dev.crossvas.jadexic2c.base.interfaces.IInfoProvider;
import dev.crossvas.jadexic2c.base.interfaces.IJadeHelper;
import ic2.core.block.transport.item.tubes.ExtractionTubeTileEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class ExtractionTubeInfo implements IInfoProvider {

    public static final ExtractionTubeInfo THIS = new ExtractionTubeInfo();

    @Override
    public void addInfo(IJadeHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof ExtractionTubeTileEntity extractionTube) {
            boolean redstoneControl = extractionTube.sensitive;
            boolean comparator = extractionTube.comparator;
            boolean pulse = extractionTube.pulse;

            text(helper, Component.translatable("ic2.tube.redstone.control", (redstoneControl ? ChatFormatting.GREEN : ChatFormatting.RED) + String.valueOf(redstoneControl)).withStyle(ChatFormatting.GOLD));
            if (redstoneControl) {
                text(helper, Component.translatable("ic2.tube.redstone.comparator", (comparator ? ChatFormatting.GREEN : ChatFormatting.RED) + String.valueOf(comparator)).withStyle(ChatFormatting.LIGHT_PURPLE));
                text(helper, Component.translatable("ic2.tube.redstone.pulse", (pulse ? ChatFormatting.GREEN : ChatFormatting.RED) + String.valueOf(pulse)).withStyle(ChatFormatting.LIGHT_PURPLE));
            }
        }
    }
}

package dev.crossvas.jadexic2c.providers;

import dev.crossvas.jadexic2c.base.IJadeHelper;
import dev.crossvas.jadexic2c.helpers.EnergyContainer;
import ic2.core.block.base.tiles.impls.BaseTransformerTileEntity;
import ic2.core.utils.helpers.Formatters;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class TransformerInfo extends EUReaderInfoProvider {

    public static final TransformerInfo THIS = new TransformerInfo();

    @Override
    public void addInfo(IJadeHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof BaseTransformerTileEntity transformer) {

            text(helper, ChatFormatting.GOLD, Component.translatable("ic2.probe.transformer.inverted").
                    append((transformer.isActive() ? ChatFormatting.GREEN : ChatFormatting.RED) + String.valueOf(transformer.isActive())));

            text(helper, "ic2.probe.eu.max_in.name", transformer.isActive() ? transformer.lowOutput : transformer.highOutput);
            text(helper, "ic2.probe.eu.output.max.name", transformer.isActive() ? transformer.highOutput : transformer.lowOutput);
            text(helper, "ic2.probe.transformer.packets.name", transformer.isActive() ? 1 : 4);
            EnergyContainer result = EnergyContainer.getContainer(transformer);
            int out = result.getAverageOut();
            int pOut = result.getPacketsOut();
            if (out > 0) {
                helper.addPaddingElement(0, 3);
                text(helper, ChatFormatting.AQUA, "tooltip.item.ic2.eu_reader.cable_flow", Formatters.EU_FORMAT.format(out));
                text(helper, ChatFormatting.AQUA, "tooltip.item.ic2.eu_reader.packet_flow", Formatters.EU_FORMAT.format(pOut));
            }
        }
    }
}

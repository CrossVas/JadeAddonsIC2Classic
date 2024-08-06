package dev.crossvas.lookingat.ic2c.base.providers;

import dev.crossvas.lookingat.ic2c.base.interfaces.IInfoProvider;
import dev.crossvas.lookingat.ic2c.base.interfaces.IHelper;
import dev.crossvas.lookingat.ic2c.helpers.EnergyContainer;
import ic2.core.block.base.tiles.impls.BaseTransformerTileEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class TransformerInfo implements IInfoProvider {

    public static final TransformerInfo THIS = new TransformerInfo();

    @Override
    public void addInfo(IHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof BaseTransformerTileEntity transformer) {
            text(helper, Component.translatable("ic2.probe.transformer.inverted", (transformer.isActive() ? ChatFormatting.GREEN : ChatFormatting.RED) + String.valueOf(transformer.isActive())).withStyle(ChatFormatting.GOLD));

            defaultText(helper, "ic2.probe.eu.max_in.name", transformer.isActive() ? transformer.lowOutput : transformer.highOutput);
            defaultText(helper, "ic2.probe.eu.output.max.name", transformer.isActive() ? transformer.highOutput : transformer.lowOutput);
            defaultText(helper, "ic2.probe.transformer.packets.name", transformer.isActive() ? 1 : 4);
            EnergyContainer container = EnergyContainer.getContainer(transformer);
            addCableAverages(helper, container.getAverageOut(), container.getPacketsOut());
        }
    }
}

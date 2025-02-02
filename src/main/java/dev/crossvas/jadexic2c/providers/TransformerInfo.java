package dev.crossvas.jadexic2c.providers;

import dev.crossvas.jadexic2c.base.JadeHelper;
import dev.crossvas.jadexic2c.base.interfaces.IInfoProvider;
import dev.crossvas.jadexic2c.helpers.EnergyContainer;
import ic2.core.block.base.tiles.impls.BaseTransformerTileEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class TransformerInfo implements IInfoProvider {

    public static final TransformerInfo THIS = new TransformerInfo();

    @Override
    public void addInfo(JadeHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof BaseTransformerTileEntity transformer) {
            helper.text(translate("ic2.probe.transformer.inverted", (transformer.isActive() ? ChatFormatting.GREEN : ChatFormatting.RED) + String.valueOf(transformer.isActive())).withStyle(ChatFormatting.GOLD));
            helper.maxIn(transformer.isActive() ? transformer.lowOutput : transformer.highOutput);
            helper.maxOut(transformer.isActive() ? transformer.highOutput : transformer.lowOutput);
            helper.defaultText("ic2.probe.transformer.packets.name", transformer.isActive() ? 1 : 4);
            EnergyContainer container = EnergyContainer.getContainer(transformer);
            helper.addStats(player, () -> helper.addCableAverages(container.getAverageOut(), container.getPacketsOut()));
        }
    }
}

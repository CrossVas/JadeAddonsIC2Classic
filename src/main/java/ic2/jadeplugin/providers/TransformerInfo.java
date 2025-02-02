package ic2.jadeplugin.providers;

import ic2.core.block.base.tiles.impls.BaseTransformerTileEntity;
import ic2.jadeplugin.base.JadeHelper;
import ic2.jadeplugin.base.interfaces.IInfoProvider;
import ic2.jadeplugin.helpers.EnergyContainer;
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

package dev.crossvas.waila.ic2.providers;

import dev.crossvas.waila.ic2.base.interfaces.IInfoProvider;
import dev.crossvas.waila.ic2.base.interfaces.IWailaHelper;
import dev.crossvas.waila.ic2.utils.EnergyContainer;
import ic2.core.block.wiring.TileEntityTransformer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

public class TransformerInfo implements IInfoProvider {

    public static final TransformerInfo THIS = new TransformerInfo();

    @Override
    public void addInfo(IWailaHelper helper, TileEntity blockEntity, EntityPlayer player) {
        if (blockEntity instanceof TileEntityTransformer) {
            TileEntityTransformer transformer = (TileEntityTransformer) blockEntity;
            text(helper, translate("probe.transformer.inverted", status(transformer.getActive())).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GOLD)));
            text(helper, maxIn(transformer.getActive() ? transformer.lowOutput : transformer.highOutput));
            text(helper, translate("probe.energy.output.max", transformer.getActive() ? transformer.highOutput : transformer.lowOutput));
            text(helper, translate("probe.packet.tick", transformer.getActive() ? 1 : 4));
            EnergyContainer container = EnergyContainer.getContainer(transformer);
            addStats(helper, player, () -> addCableOut(helper, container));
        }
    }
}

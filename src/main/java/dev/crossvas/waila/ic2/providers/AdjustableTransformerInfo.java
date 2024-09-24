package dev.crossvas.waila.ic2.providers;

import dev.crossvas.waila.ic2.base.interfaces.IInfoProvider;
import dev.crossvas.waila.ic2.base.interfaces.IWailaHelper;
import dev.crossvas.waila.ic2.utils.EnergyContainer;
import ic2.core.block.wiring.TileEntityAdjustableTransformer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

public class AdjustableTransformerInfo implements IInfoProvider {

    public static final AdjustableTransformerInfo THIS = new AdjustableTransformerInfo();

    @Override
    public void addInfo(IWailaHelper helper, TileEntity blockEntity, EntityPlayer player) {
        if (blockEntity instanceof TileEntityAdjustableTransformer) {
            TileEntityAdjustableTransformer transformer = (TileEntityAdjustableTransformer) blockEntity;
            int energyPacket = transformer.energyPacket;
            int packets = transformer.packetCount;
            text(helper, tier(transformer.sinkTier));
            text(helper, translatable("probe.energy.output.max", energyPacket));
            text(helper, translatable("probe.packet.tick", packets));
            EnergyContainer container = EnergyContainer.getContainer(transformer);
            if (player.isSneaking()) {
                text(helper, translatable("probe.energy.stats.info").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GREEN)), true);
                addCableOut(helper, container);
            } else {
                text(helper, translatable("probe.sneak.info").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.AQUA)), true);
            }
        }
    }
}

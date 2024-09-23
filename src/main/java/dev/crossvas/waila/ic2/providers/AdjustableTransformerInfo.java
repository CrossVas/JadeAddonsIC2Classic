package dev.crossvas.waila.ic2.providers;

import dev.crossvas.jadexic2c.base.interfaces.IInfoProvider;
import dev.crossvas.jadexic2c.base.interfaces.IJadeHelper;
import dev.crossvas.jadexic2c.utils.EnergyContainer;
import ic2.api.energy.EnergyNet;
import ic2.core.block.wiring.tile.TileEntityAdjustableTransformer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;

public class AdjustableTransformerInfo implements IInfoProvider {

    public static final AdjustableTransformerInfo THIS = new AdjustableTransformerInfo();

    @Override
    public void addInfo(IJadeHelper helper, TileEntity blockEntity, EntityPlayer player) {
        if (blockEntity instanceof TileEntityAdjustableTransformer) {
            TileEntityAdjustableTransformer transformer = (TileEntityAdjustableTransformer) blockEntity;
            int energyPacket = transformer.energyPacket;
            int packets = transformer.packetCount;
            text(helper, translatable("probe.energy.tier", (int) EnergyNet.instance.getPowerFromTier(transformer.sinkTier)));
            text(helper, translatable("probe.energy.output.max", energyPacket));
            text(helper, translatable("probe.packet.tick", packets));
            EnergyContainer container = EnergyContainer.getContainer(transformer);
            if (player.isSneaking()) {
                text(helper, translatable("probe.energy.stats.info").setStyle(new Style().setColor(TextFormatting.GREEN)), true);
                addCableOut(helper, container);
            } else {
                text(helper, translatable("probe.sneak.info").setStyle(new Style().setColor(TextFormatting.AQUA)), true);
            }
        }
    }
}

package dev.crossvas.jadexic2c.providers;

import dev.crossvas.jadexic2c.base.JadeHelper;
import dev.crossvas.jadexic2c.base.interfaces.IInfoProvider;
import dev.crossvas.jadexic2c.helpers.EnergyContainer;
import ic2.api.energy.EnergyNet;
import ic2.core.block.storage.tiles.transformer.AdjustableTransformerTileEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class AdjustableTransformerInfo implements IInfoProvider {

    public static final AdjustableTransformerInfo THIS = new AdjustableTransformerInfo();

    @Override
    public void addInfo(JadeHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof AdjustableTransformerTileEntity transformer) {
            int energyPacket = transformer.energyPacket;
            int packetCount = transformer.getPacketCount();

            helper.defaultText("ic2.probe.eu.max_in.name", EnergyNet.INSTANCE.getPowerFromTier(transformer.getSinkTier()));
            helper.defaultText("ic2.probe.eu.output.max.name", energyPacket);
            helper.defaultText("ic2.probe.transformer.packets.name", packetCount);

            EnergyContainer container = EnergyContainer.getContainer(transformer);
            helper.addStats(player, () -> helper.addCableAverages(container.getAverageOut(), container.getPacketsOut()));
        }
    }
}

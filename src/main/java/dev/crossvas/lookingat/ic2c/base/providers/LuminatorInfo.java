package dev.crossvas.lookingat.ic2c.base.providers;

import dev.crossvas.lookingat.ic2c.base.interfaces.IInfoProvider;
import dev.crossvas.lookingat.ic2c.base.interfaces.IHelper;
import ic2.api.energy.EnergyNet;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.tiles.readers.IEUStorage;
import ic2.core.block.cables.luminator.ConstructionLightTileEntity;
import ic2.core.block.cables.luminator.LuminatorTileEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class LuminatorInfo implements IInfoProvider {

    public static final LuminatorInfo THIS = new LuminatorInfo();

    @Override
    public void addInfo(IHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof LuminatorTileEntity || blockEntity instanceof ConstructionLightTileEntity) {
            defaultText(helper, "ic2.probe.eu.tier.name", EnergyNet.INSTANCE.getDisplayTier(((IEUStorage) blockEntity).getTier()));
            defaultText(helper, "ic2.probe.eu.max_in.name", EnergyNet.INSTANCE.getPowerFromTier(((IEnergySink) blockEntity).getSinkTier()));
            defaultText(helper, "ic2.probe.eu.usage.name", 0.1);
        }

        if (blockEntity instanceof LuminatorTileEntity luminator) {
            defaultText(helper, "ic2.probe.luminator.light.name", luminator.getLightLevel());
        }
        if (blockEntity instanceof ConstructionLightTileEntity constructionLight) {
            defaultText(helper, "ic2.probe.luminator.light.name", constructionLight.isActive() ? 15 : 0);
        }
    }
}

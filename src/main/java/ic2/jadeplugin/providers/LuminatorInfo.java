package ic2.jadeplugin.providers;

import ic2.api.energy.tile.IEnergySink;
import ic2.core.block.cables.luminator.ConstructionLightTileEntity;
import ic2.core.block.cables.luminator.LuminatorTileEntity;
import ic2.jadeplugin.base.JadeHelper;
import ic2.jadeplugin.base.interfaces.IInfoProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class LuminatorInfo implements IInfoProvider {

    public static final LuminatorInfo THIS = new LuminatorInfo();

    @Override
    public void addInfo(JadeHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof LuminatorTileEntity || blockEntity instanceof ConstructionLightTileEntity) {
            helper.maxInFromTier(((IEnergySink) blockEntity).getSinkTier());
            helper.usage(0.1);
        }

        if (blockEntity instanceof LuminatorTileEntity luminator) {
            helper.defaultText("ic2.probe.luminator.light.name", luminator.getLightLevel());
        }
        if (blockEntity instanceof ConstructionLightTileEntity constructionLight) {
            helper.defaultText("ic2.probe.luminator.light.name", constructionLight.isActive() ? 15 : 0);
        }
    }
}

package ic2.jadeplugin.providers.transport;

import ic2.core.block.transport.item.tubes.DirectionalTubeTileEntity;
import ic2.core.utils.helpers.SanityHelper;
import ic2.jadeplugin.base.JadeHelper;
import ic2.jadeplugin.base.interfaces.IInfoProvider;
import ic2.jadeplugin.helpers.TextFormatter;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class DirectionalTubeInfo implements IInfoProvider {

    public static final DirectionalTubeInfo THIS = new DirectionalTubeInfo();

    @Override
    public void addInfo(JadeHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof DirectionalTubeTileEntity directional) {
            helper.paddingY(3);
            int direction3DDataValue = directional.getFacing().get3DDataValue();
            String facingName = SanityHelper.toPascalCase(directional.getFacing().toString());
            helper.text(TextFormatter.GOLD.translate("info.tube.direction", TextFormatter.getColor(direction3DDataValue).literal(facingName)));
            helper.text(TextFormatter.DARK_AQUA.translate("info.tube.blocked", status(directional.isBlocked())));
            helper.text(TextFormatter.GRAY.translate("info.tube.direction.set"));
        }
    }
}

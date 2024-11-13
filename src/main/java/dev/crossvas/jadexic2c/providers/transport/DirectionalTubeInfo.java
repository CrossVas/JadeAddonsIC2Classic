package dev.crossvas.jadexic2c.providers.transport;

import dev.crossvas.jadexic2c.base.interfaces.IInfoProvider;
import dev.crossvas.jadexic2c.base.interfaces.IJadeHelper;
import dev.crossvas.jadexic2c.helpers.TextFormatter;
import ic2.core.block.transport.item.tubes.DirectionalTubeTileEntity;
import ic2.core.utils.helpers.SanityHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class DirectionalTubeInfo implements IInfoProvider {

    public static final DirectionalTubeInfo THIS = new DirectionalTubeInfo();

    @Override
    public void addInfo(IJadeHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof DirectionalTubeTileEntity directional) {
            paddingY(helper, 3);
            int direction3DDataValue = directional.getFacing().get3DDataValue();
            String facingName = SanityHelper.toPascalCase(directional.getFacing().toString());
            text(helper, TextFormatter.GOLD.translate("info.tube.direction", TextFormatter.getColor(direction3DDataValue).literal(facingName)));
            text(helper, TextFormatter.DARK_AQUA.translate("info.tube.blocked", status(directional.isBlocked())));
        }
    }
}

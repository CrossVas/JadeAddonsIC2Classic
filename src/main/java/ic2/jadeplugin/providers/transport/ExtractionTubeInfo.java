package ic2.jadeplugin.providers.transport;

import ic2.core.block.transport.item.tubes.ExtractionTubeTileEntity;
import ic2.jadeplugin.base.JadeHelper;
import ic2.jadeplugin.base.interfaces.IInfoProvider;
import ic2.jadeplugin.helpers.TextFormatter;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class ExtractionTubeInfo implements IInfoProvider {

    public static final ExtractionTubeInfo THIS = new ExtractionTubeInfo();

    @Override
    public void addInfo(JadeHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof ExtractionTubeTileEntity extractionTube) {
            boolean redstoneControl = extractionTube.sensitive;
            boolean comparator = extractionTube.comparator;
            boolean pulse = extractionTube.pulse;
            helper.text(TextFormatter.GOLD.translate("info.tube.redstone", status(redstoneControl)));
            if (redstoneControl) {
                helper.text(TextFormatter.LIGHT_PURPLE.translate("info.tube.comparator", status(comparator)));
                helper.text(TextFormatter.LIGHT_PURPLE.translate("info.tube.pulse", status(pulse)));
            }
        }
    }
}

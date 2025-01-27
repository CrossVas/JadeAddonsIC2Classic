package dev.crossvas.jadexic2c.providers.transport;

import dev.crossvas.jadexic2c.base.JadeHelper;
import dev.crossvas.jadexic2c.base.interfaces.IInfoProvider;
import dev.crossvas.jadexic2c.helpers.TextFormatter;
import ic2.core.block.transport.item.tubes.ExtractionTubeTileEntity;
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

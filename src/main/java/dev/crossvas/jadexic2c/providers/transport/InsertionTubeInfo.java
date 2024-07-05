package dev.crossvas.jadexic2c.providers.transport;

import dev.crossvas.jadexic2c.base.interfaces.IInfoProvider;
import dev.crossvas.jadexic2c.base.interfaces.IJadeHelper;
import ic2.core.block.transport.item.tubes.InsertionTubeTileEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class InsertionTubeInfo implements IInfoProvider {

    public static final InsertionTubeInfo THIS = new InsertionTubeInfo();

    @Override
    public void addInfo(IJadeHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof InsertionTubeTileEntity insertionTube) {
            boolean insertExisting = insertionTube.onlyExistingOne;
            text(helper, Component.translatable("ic2.tube.inserter.existing", (insertExisting ? ChatFormatting.GREEN : ChatFormatting.RED) + String.valueOf(insertExisting)).withStyle(ChatFormatting.GOLD));
        }
    }
}

package ic2.jadeplugin.providers;

import ic2.core.block.machines.tiles.hv.VillagerOMatTileEntity;
import ic2.jadeplugin.base.JadeHelper;
import ic2.jadeplugin.base.interfaces.IInfoProvider;
import ic2.jadeplugin.helpers.TextFormatter;
import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class VillagerOMatInfo implements IInfoProvider {

    public static final VillagerOMatInfo THIS = new VillagerOMatInfo();

    @Override
    public void addInfo(JadeHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof VillagerOMatTileEntity oMat) {
            helper.maxIn(oMat.getMaxInput());
            helper.defaultText("ic2.probe.villager_o_mat.usage", TextFormatter.GREEN.literal(oMat.trades.getActiveTrades() * 6000 + " "));
            helper.text(translate("ic2.probe.personal.owner", oMat.getOwner().getDisplayName().copy().withStyle(ChatFormatting.GREEN)).withStyle(ChatFormatting.AQUA));
            helper.bar((int) (1200 - oMat.clockTime(1200)), 1200, translate("ic2.probe.villager_o_mat.next", oMat.clockTime(1200)), -16733185);
        }
    }
}

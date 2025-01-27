package dev.crossvas.jadexic2c.providers;

import dev.crossvas.jadexic2c.base.JadeHelper;
import dev.crossvas.jadexic2c.base.interfaces.IInfoProvider;
import ic2.api.energy.EnergyNet;
import ic2.core.block.machines.tiles.hv.VillagerOMatTileEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class VillagerOMatInfo implements IInfoProvider {

    public static final VillagerOMatInfo THIS = new VillagerOMatInfo();

    @Override
    public void addInfo(JadeHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof VillagerOMatTileEntity oMat) {
            helper.defaultText("ic2.probe.eu.tier.name", EnergyNet.INSTANCE.getDisplayTier(oMat.getTier()));
            helper.defaultText("ic2.probe.eu.max_in.name", oMat.getMaxInput());
            helper.defaultText("ic2.probe.villager_o_mat.usage", oMat.trades.getActiveTrades() * 6000);
            helper.text(translate("ic2.probe.personal.owner", oMat.getOwner().getDisplayName().copy().withStyle(ChatFormatting.GREEN)).withStyle(ChatFormatting.AQUA));
            helper.bar((int) (1200 - oMat.clockTime(1200)), 1200, translate("ic2.probe.villager_o_mat.next", oMat.clockTime(1200)), -16733185);
        }
    }
}

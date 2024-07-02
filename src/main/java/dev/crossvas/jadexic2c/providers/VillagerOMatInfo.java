package dev.crossvas.jadexic2c.providers;

import dev.crossvas.jadexic2c.base.interfaces.IInfoProvider;
import dev.crossvas.jadexic2c.base.interfaces.IJadeHelper;
import ic2.api.energy.EnergyNet;
import ic2.core.block.machines.tiles.hv.VillagerOMatTileEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class VillagerOMatInfo implements IInfoProvider {

    public static final VillagerOMatInfo THIS = new VillagerOMatInfo();

    @Override
    public void addInfo(IJadeHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof VillagerOMatTileEntity oMat) {
            defaultText(helper, "ic2.probe.eu.tier.name", EnergyNet.INSTANCE.getDisplayTier(oMat.getTier()));
            defaultText(helper, "ic2.probe.eu.max_in.name", oMat.getMaxInput());
            defaultText(helper, "ic2.probe.villager_o_mat.usage", oMat.trades.getActiveTrades() * 6000);
            bar(helper, (int) (1200 - oMat.clockTime(1200)), 1200, Component.translatable("ic2.probe.villager_o_mat.next", oMat.clockTime(1200)), -16733185);
        }
    }
}

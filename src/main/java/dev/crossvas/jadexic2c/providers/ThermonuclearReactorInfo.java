package dev.crossvas.jadexic2c.providers;

import dev.crossvas.jadexic2c.JadeCommonHandler;
import dev.crossvas.jadexic2c.base.interfaces.IInfoProvider;
import dev.crossvas.jadexic2c.base.interfaces.IJadeHelper;
import ic2.core.block.base.tiles.BaseLinkingTileEntity;
import ic2.core.block.machines.tiles.luv.FusionReactorTileEntity;
import ic2.core.utils.math.ColorUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class ThermonuclearReactorInfo implements IInfoProvider {

    public static final ThermonuclearReactorInfo THIS = new ThermonuclearReactorInfo();

    @Override
    public void addInfo(IJadeHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof FusionReactorTileEntity reactor) {
            JadeCommonHandler.addTankInfo(helper, reactor);
            if (!reactor.isValid || reactor.isDynamic()) {
                long time = reactor.clockTime(512);
                bar(helper, (int) time, 512, Component.translatable("ic2.multiblock.reform.next", 512 - time), ColorUtils.GRAY);
            }
        }
        if (blockEntity instanceof BaseLinkingTileEntity linking) {
            BlockEntity master = linking.getMaster();
            if (master instanceof FusionReactorTileEntity reactor) {
                JadeCommonHandler.addTankInfo(helper, reactor);
            }
        }
    }
}

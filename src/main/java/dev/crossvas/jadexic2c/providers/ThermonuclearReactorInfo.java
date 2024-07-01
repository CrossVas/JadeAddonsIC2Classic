package dev.crossvas.jadexic2c.providers;

import dev.crossvas.jadexic2c.JadeCommonHandler;
import dev.crossvas.jadexic2c.base.IInfoProvider;
import dev.crossvas.jadexic2c.base.IJadeHelper;
import ic2.core.block.base.tiles.BaseLinkingTileEntity;
import ic2.core.block.machines.tiles.luv.FusionReactorTileEntity;
import ic2.core.utils.math.ColorUtils;
import net.minecraft.ChatFormatting;
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
                helper.addBarElement((int) time, 512, Component.literal("Next Reform: ").append(String.valueOf(512 - time)).append(" Ticks").withStyle(ChatFormatting.WHITE), ColorUtils.GRAY);
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

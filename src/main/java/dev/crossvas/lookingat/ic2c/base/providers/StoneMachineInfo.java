package dev.crossvas.lookingat.ic2c.base.providers;

import dev.crossvas.lookingat.ic2c.base.LookingAtCommonHandler;
import dev.crossvas.lookingat.ic2c.base.interfaces.IInfoProvider;
import dev.crossvas.lookingat.ic2c.base.interfaces.IHelper;
import ic2.core.block.machines.tiles.nv.StoneBasicMachineTileEntity;
import ic2.core.block.machines.tiles.nv.StoneCannerTileEntity;
import ic2.core.block.machines.tiles.nv.StoneWoodGassifierTileEntity;
import ic2.core.utils.helpers.Formatters;
import ic2.core.utils.math.ColorUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class StoneMachineInfo implements IInfoProvider {

    public static final StoneMachineInfo THIS = new StoneMachineInfo();

    @Override
    public void addInfo(IHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof StoneBasicMachineTileEntity stoneMachine) {
            if (stoneMachine.getFuel() > 0) {
                bar(helper, stoneMachine.getFuel(), stoneMachine.getMaxFuel(), Component.translatable("ic2.probe.fuel.storage.name").append(String.valueOf(stoneMachine.getFuel())), ColorUtils.DARK_GRAY);
            }
            if (stoneMachine.getProgress() > 0) {
                bar(helper, (int) stoneMachine.getProgress(), (int) stoneMachine.getMaxProgress(), Component.translatable("ic2.probe.progress.full.name", (int) stoneMachine.getProgress(), (int) stoneMachine.getMaxProgress()).append("t").withStyle(ChatFormatting.WHITE), -16733185);
            }
        }

        if (blockEntity instanceof StoneWoodGassifierTileEntity woodGassifierTile) {
            defaultText(helper, "ic2.probe.pump.pressure", 25);
            defaultText(helper, "ic2.probe.pump.amount", Formatters.EU_FORMAT.format(900L));
            if (woodGassifierTile.getFuel() > 0) {
                bar(helper, woodGassifierTile.getFuel(), woodGassifierTile.getMaxFuel(), Component.translatable("ic2.probe.fuel.storage.name").append(String.valueOf(woodGassifierTile.getFuel())), ColorUtils.DARK_GRAY);
            }
            if (woodGassifierTile.getProgress() > 0) {
                bar(helper, (int) woodGassifierTile.getProgress(), (int) woodGassifierTile.getMaxProgress(), Component.translatable("ic2.probe.progress.full.name", (int) woodGassifierTile.getProgress(), (int) woodGassifierTile.getMaxProgress()).append("t").withStyle(ChatFormatting.WHITE), -16733185);
            }
            LookingAtCommonHandler.addTankInfo(helper, woodGassifierTile);
        }
        if (blockEntity instanceof StoneCannerTileEntity stoneCannerTile) {
            if (stoneCannerTile.getFuel() > 0) {
                bar(helper, stoneCannerTile.getFuel(), stoneCannerTile.getMaxFuel(), Component.translatable("ic2.probe.fuel.storage.name").append(String.valueOf(stoneCannerTile.getFuel())), ColorUtils.DARK_GRAY);
            }
            if (stoneCannerTile.getProgress() > 0) {
                bar(helper, (int) stoneCannerTile.getProgress(), (int) stoneCannerTile.getMaxProgress(), Component.translatable("ic2.probe.progress.full.name", (int) stoneCannerTile.getProgress(), (int) stoneCannerTile.getMaxProgress()).append("t").withStyle(ChatFormatting.WHITE), -16733185);
            }
        }
    }
}

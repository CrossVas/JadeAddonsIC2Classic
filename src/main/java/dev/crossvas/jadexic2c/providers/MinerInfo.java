package dev.crossvas.jadexic2c.providers;

import dev.crossvas.jadexic2c.base.JadeHelper;
import dev.crossvas.jadexic2c.base.interfaces.IInfoProvider;
import ic2.core.block.machines.tiles.hv.RocketMinerTileEntity;
import ic2.core.block.machines.tiles.lv.MinerTileEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class MinerInfo implements IInfoProvider {

    public static final MinerInfo THIS = new MinerInfo();

    @Override
    public void addInfo(JadeHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof MinerTileEntity miner) {
            helper.maxIn(miner.getMaxInput());
            helper.usage(miner.getEnergyUsage());

            float progress = miner.getProgress();
            boolean isStuck = miner.isStuck();
            boolean isOperating = miner.isOperating();

            if (miner instanceof RocketMinerTileEntity rocketMiner) {
                RocketMinerTileEntity.MinerState state = rocketMiner.state;
                helper.defaultText(state.getState());
                helper.addTankInfo(rocketMiner);
            } else {
                helper.defaultText(isStuck ? "ic2.probe.miner.stuck.name" : isOperating ? "ic2.probe.miner.mining.name" : "ic2.probe.miner.retracting.name");
            }

            helper.text(translate("ic2.probe.miner.progress.name", miner.getPipeTip().getY()).withStyle(ChatFormatting.GOLD));
            if (!isStuck && progress > 0) {
                int scaledOp = (int) Math.min(6.0E7F, progress);
                int scaledMaxOp = (int) Math.min(6.0E7F, miner.getMaxProgress());
                helper.bar(scaledOp, scaledMaxOp, translate("ic2.probe.progress.full.name", scaledOp, scaledMaxOp), -16733185);
            }
        }
    }
}

package dev.crossvas.waila.ic2.providers;

import dev.crossvas.waila.ic2.base.interfaces.IInfoProvider;
import dev.crossvas.waila.ic2.base.interfaces.IWailaHelper;
import dev.crossvas.waila.ic2.utils.ColorUtils;
import dev.crossvas.waila.ic2.utils.TextFormatter;
import ic2.api.item.IMiningDrill;
import ic2.core.block.machine.tileentity.TileEntityMiner;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class MinerInfo implements IInfoProvider {

    public static final MinerInfo THIS = new MinerInfo();

    @Override
    public void addInfo(IWailaHelper helper, TileEntity blockEntity, EntityPlayer player) {
        if (blockEntity instanceof TileEntityMiner) {
            TileEntityMiner miner = (TileEntityMiner) blockEntity;
            text(helper, tier(miner.getSinkTier()));
            text(helper, maxIn(miner.maxInput));
            text(helper, usage(getEnergyUsage(miner)));

            float progress = miner.miningTicker;
            boolean isStuck = miner.isStuck();
            boolean isOperating = miner.isOperating();
            text(helper, translate(isStuck ? "probe.miner.stuck" : isOperating ? "probe.miner.mining" : "probe.miner.retracting"));

            text(helper, translate(TextFormatter.GOLD, "probe.miner.progress", miner.getPipeTip()));
            if (!isStuck && progress > 0) {
                int scaledOp = (int) Math.min(6.0E7F, progress);
                int scaledMaxOp = (int) Math.min(6.0E7F, 200);
                bar(helper, scaledOp, scaledMaxOp, translate("probe.progress.full.name", scaledOp, scaledMaxOp), ColorUtils.PROGRESS);
            }
        }
    }

    public int getEnergyUsage(TileEntityMiner miner) {
        ItemStack tool = miner.inventory[3];
        return tool.getItem() instanceof IMiningDrill ? Math.max(1, 1 + ((IMiningDrill) tool.getItem()).getExtraEnergyCost(tool)) : 1;
    }
}

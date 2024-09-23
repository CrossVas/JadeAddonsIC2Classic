package dev.crossvas.jadexic2c.providers;

import dev.crossvas.jadexic2c.base.interfaces.IInfoProvider;
import dev.crossvas.jadexic2c.base.interfaces.IJadeHelper;
import ic2.api.classic.item.IMiningDrill;
import ic2.core.block.machine.low.TileEntityMiner;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;

public class MinerInfo implements IInfoProvider {

    public static final MinerInfo THIS = new MinerInfo();

    @Override
    public void addInfo(IJadeHelper helper, TileEntity blockEntity, EntityPlayer player) {
        if (blockEntity instanceof TileEntityMiner) {
            TileEntityMiner miner = (TileEntityMiner) blockEntity;
            text(helper, tier(miner.getTier()));
            text(helper, maxIn(miner.maxInput));
            text(helper, usage(getEnergyUsage(miner)));

            float progress = miner.getProgress();
            boolean isStuck = miner.isStuck();
            boolean isOperating = miner.isOperating();
            text(helper, translatable(isStuck ? "probe.miner.stuck" : isOperating ? "probe.miner.mining" : "probe.miner.retracting"));

            text(helper, translatable("probe.miner.progress", miner.getPipeTip().getY()).setStyle(new Style().setColor(TextFormatting.GOLD)));
            if (!isStuck && progress > 0) {
                int scaledOp = (int) Math.min(6.0E7F, progress);
                int scaledMaxOp = (int) Math.min(6.0E7F, miner.getMaxProgress());
                bar(helper, scaledOp, scaledMaxOp, translatable("probe.progress.full.name", scaledOp, scaledMaxOp), -16733185);
            }
        }
    }

    public int getEnergyUsage(TileEntityMiner miner) {
        ItemStack tool = miner.inventory.get(3);
        return tool.getItem() instanceof IMiningDrill ? Math.max(1, 1 + ((IMiningDrill) tool.getItem()).getExtraEnergyCost(tool)) : 1;
    }
}

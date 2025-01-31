package dev.crossvas.jadexic2c.providers;

import dev.crossvas.jadexic2c.base.JadeHelper;
import dev.crossvas.jadexic2c.base.interfaces.IInfoProvider;
import dev.crossvas.jadexic2c.helpers.EnergyContainer;
import ic2.api.energy.EnergyNet;
import ic2.api.items.electric.ElectricItem;
import ic2.core.block.base.tiles.impls.BaseChargingBenchTileEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.apache.commons.lang3.time.DurationFormatUtils;

public class ChargingBenchInfo implements IInfoProvider {

    public static final ChargingBenchInfo THIS = new ChargingBenchInfo();

    @Override
    public void addInfo(JadeHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof BaseChargingBenchTileEntity bench) {
            helper.defaultText("ic2.probe.eu.tier.name", EnergyNet.INSTANCE.getDisplayTier(bench.getSinkTier()));
            helper.defaultText("ic2.probe.eu.max_in.name", bench.getMaxInput());

            int missingEnergy = 0;
            for (int i = 0; i < 16; i++) {
                ItemStack toCharge = bench.getStackInSlot(i);
                if (!toCharge.isEmpty()) {
                    missingEnergy += (ElectricItem.MANAGER.getCapacity(toCharge) - ElectricItem.MANAGER.getCharge(toCharge));
                }
            }
            int benchAverageOut = bench.getMissingEnergy().getIntValue();
            if (missingEnergy > 0) {
                int i = Math.min(benchAverageOut, missingEnergy);
                helper.text(translate("ic2.probe.chargingBench.eta.name",
                        DurationFormatUtils.formatDuration(i <= 0 ? 0L : (missingEnergy / i * 50L), "HH:mm:ss")).withStyle(ChatFormatting.GOLD));
            }

            ItemStack battery = bench.getStackInSlot(16);
            int toDischargeEnergy = ElectricItem.MANAGER.getCharge(battery);
            int transferLimit = ElectricItem.MANAGER.getTransferLimit(battery);
            int maxCapacity = ElectricItem.MANAGER.getCapacity(battery);

            if (toDischargeEnergy > 0) {
                int dischargeEnergy = Math.min(transferLimit, toDischargeEnergy);
                helper.bar(toDischargeEnergy, maxCapacity, translate("ic2.probe.discharging.eta.name",
                        DurationFormatUtils.formatDuration(dischargeEnergy <= 0 ? 0L : (toDischargeEnergy / dischargeEnergy * 50L), "HH:mm:ss")).withStyle(ChatFormatting.WHITE), -16733185);
            }
            EnergyContainer container = EnergyContainer.getContainer(bench);
            helper.addStats(player, () -> helper.addAveragesIn(container));
        }
    }
}

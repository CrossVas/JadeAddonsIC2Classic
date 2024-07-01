package dev.crossvas.jadexic2c.providers;

import dev.crossvas.jadexic2c.base.IInfoProvider;
import dev.crossvas.jadexic2c.base.IJadeHelper;
import dev.crossvas.jadexic2c.helpers.EnergyContainer;
import ic2.api.energy.EnergyNet;
import ic2.api.items.electric.ElectricItem;
import ic2.core.block.base.tiles.impls.BaseChargingBenchTileEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.apache.commons.lang3.time.DurationFormatUtils;

public class ChargingBenchInfo implements IInfoProvider {

    public static final ChargingBenchInfo THIS = new ChargingBenchInfo();

    @Override
    public void addInfo(IJadeHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof BaseChargingBenchTileEntity bench) {
            text(helper, "ic2.probe.eu.tier.name", EnergyNet.INSTANCE.getDisplayTier(bench.getSinkTier()));
            text(helper, "ic2.probe.eu.max_in.name", bench.getMaxInput());

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
                text(helper, ChatFormatting.GOLD, Component.translatable("ic2.probe.chargingBench.eta.name",
                        DurationFormatUtils.formatDuration(i <= 0 ? 0L : (missingEnergy / i * 50L), "HH:mm:ss")));
            }

            ItemStack battery = bench.getStackInSlot(16);
            int toDischargeEnergy = ElectricItem.MANAGER.getCharge(battery);
            int transferLimit = ElectricItem.MANAGER.getTransferLimit(battery);

            if (toDischargeEnergy > 0) {
                int dischargeEnergy = Math.min(transferLimit, toDischargeEnergy);
                text(helper, ChatFormatting.AQUA, Component.translatable("ic2.probe.discharging.eta.name",
                        DurationFormatUtils.formatDuration(dischargeEnergy <= 0 ? 0L : (toDischargeEnergy / dischargeEnergy * 50L), "HH:mm:ss")));
            }
            EnergyContainer container = EnergyContainer.getContainer(bench);
            addAveragesIn(helper, container);
        }
    }
}

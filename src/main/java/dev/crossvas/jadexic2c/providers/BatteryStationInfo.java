package dev.crossvas.jadexic2c.providers;

import dev.crossvas.jadexic2c.base.IInfoProvider;
import dev.crossvas.jadexic2c.base.IJadeHelper;
import dev.crossvas.jadexic2c.helpers.EnergyContainer;
import ic2.api.energy.EnergyNet;
import ic2.api.items.electric.ElectricItem;
import ic2.core.block.base.tiles.impls.BaseBatteryStationTileEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.apache.commons.lang3.time.DurationFormatUtils;

public class BatteryStationInfo implements IInfoProvider {

    public static final BatteryStationInfo THIS = new BatteryStationInfo();

    @Override
    public void addInfo(IJadeHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof BaseBatteryStationTileEntity station) {
            text(helper, "ic2.probe.eu.tier.name", EnergyNet.INSTANCE.getDisplayTier(station.getSourceTier()));
            text(helper, "ic2.probe.eu.output.max.name", station.getMaxEnergyOutput());

            ItemStack toCharge = station.getStackInSlot(16);
            long missingEnergy = ElectricItem.MANAGER.getCapacity(toCharge) - ElectricItem.MANAGER.getCharge(toCharge);
            int maxTransfer = ElectricItem.MANAGER.getTransferLimit(toCharge);

            if (missingEnergy > 0) {
                int chargeEnergy = (int) Math.min(maxTransfer, missingEnergy);
                text(helper, ChatFormatting.GOLD, Component.translatable("ic2.probe.chargingBench.eta.name",
                        DurationFormatUtils.formatDuration(chargeEnergy <= 0 ? 0L : (missingEnergy / chargeEnergy * 50L), "HH:mm:ss")));
            }

            int capacity = station.getMissingEnergy().getIntKey();
            int averageIn = station.getMissingEnergy().getIntValue();

            if (capacity > 0) {
                int dischargeEnergy = Math.min(averageIn, capacity);
                text(helper, ChatFormatting.AQUA, Component.translatable("ic2.probe.discharging.eta.name",
                        DurationFormatUtils.formatDuration(dischargeEnergy <= 0 ? 0L : (capacity / dischargeEnergy * 50L), "HH:mm:ss")));
            }
            EnergyContainer container = EnergyContainer.getContainer(station);
            addAveragesOut(helper, container);
        }
    }
}

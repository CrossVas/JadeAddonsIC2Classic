package dev.crossvas.lookingat.ic2c.base.providers;

import dev.crossvas.lookingat.ic2c.base.interfaces.IInfoProvider;
import dev.crossvas.lookingat.ic2c.base.interfaces.IHelper;
import dev.crossvas.lookingat.ic2c.helpers.EnergyContainer;
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
    public void addInfo(IHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof BaseBatteryStationTileEntity station) {
            defaultText(helper, "ic2.probe.eu.tier.name", EnergyNet.INSTANCE.getDisplayTier(station.getSourceTier()));
            defaultText(helper, "ic2.probe.eu.output.max.name", station.getMaxEnergyOutput());

            int capacity = 0;
            int maxCapacity = 0;
            for (int i = 0; i < 16; i++) {
                ItemStack battery = station.getStackInSlot(i);
                if (!battery.isEmpty()) {
                    capacity += ElectricItem.MANAGER.getCharge(battery);
                    maxCapacity += ElectricItem.MANAGER.getCapacity(battery);
                }
            }

            ItemStack battery = station.getStackInSlot(16);
            long toCharge = ElectricItem.MANAGER.getCapacity(battery) - ElectricItem.MANAGER.getCharge(battery);
            int maxTransfer = ElectricItem.MANAGER.getTransferLimit(battery);

            if (toCharge > 0) {
                int chargeEnergy = (int) Math.min(maxTransfer, toCharge);
                text(helper, Component.translatable("ic2.probe.chargingBench.eta.name",
                        DurationFormatUtils.formatDuration(chargeEnergy <= 0 ? 0L : (toCharge / chargeEnergy * 50L), "HH:mm:ss")).withStyle(ChatFormatting.GOLD));
            }

            int missingEnergy = station.getMissingEnergy().getIntKey();
            int averageIn = station.getMissingEnergy().getIntValue();

            if (missingEnergy > 0) {
                int dischargeEnergy = Math.min(averageIn, missingEnergy);
                bar(helper, capacity, maxCapacity, Component.translatable("ic2.probe.discharging.eta.name",
                        DurationFormatUtils.formatDuration(dischargeEnergy <= 0 ? 0L : (missingEnergy / dischargeEnergy * 50L), "HH:mm:ss")), -16733185);
            }
            EnergyContainer container = EnergyContainer.getContainer(station);
            addAveragesOut(helper, container);
        }
    }
}

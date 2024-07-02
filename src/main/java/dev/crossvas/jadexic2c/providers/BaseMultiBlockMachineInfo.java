package dev.crossvas.jadexic2c.providers;

import dev.crossvas.jadexic2c.JadeCommonHandler;
import dev.crossvas.jadexic2c.base.interfaces.IInfoProvider;
import dev.crossvas.jadexic2c.base.interfaces.IJadeHelper;
import ic2.api.energy.EnergyNet;
import ic2.core.block.base.tiles.impls.machine.multi.BaseAdvMultiMachineTileEntity;
import ic2.core.block.base.tiles.impls.machine.multi.BaseColossalMachineTileEntity;
import ic2.core.block.base.tiles.impls.machine.multi.BaseMultiMachineTileEntity;
import ic2.core.block.base.tiles.impls.machine.multi.BasicMultiMachineTileEntity;
import ic2.core.utils.math.ColorUtils;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.text.DecimalFormat;

public class BaseMultiBlockMachineInfo implements IInfoProvider {

    public static final BaseMultiBlockMachineInfo THIS = new BaseMultiBlockMachineInfo();

    @Override
    public void addInfo(IJadeHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof BaseMultiMachineTileEntity multiMachine) {
            defaultText(helper, "ic2.probe.eu.tier.name", EnergyNet.INSTANCE.getDisplayTier(multiMachine.getTier()));
            defaultText(helper, "ic2.probe.eu.max_in.name", multiMachine.getMaxInput());
            defaultText(helper, "ic2.probe.eu.usage.name", multiMachine.getEnergyPerTick());

            if (multiMachine instanceof BaseAdvMultiMachineTileEntity adv) {
                int speed = adv.getSpeed();
                int maxSpeed = adv.getMaxSpeed();
                Component speedName = adv.getSpeedName();
                double scaledProgress = (double) speed / maxSpeed;
                if (speed > 0) {
                    bar(helper, speed, maxSpeed, speedName.plainCopy().append(": " + new DecimalFormat().format(scaledProgress * 100.0) + "%"), -295680);
                }
            }

            if (!multiMachine.isValid) {
                long time = multiMachine.clockTime(512);
                bar(helper, (int) time, 512, Component.translatable("ic2.multiblock.reform.next", 512 - time), ColorUtils.GRAY);
            }

            if (multiMachine instanceof BasicMultiMachineTileEntity machineTile) {
                if (multiMachine.isMachineWorking() || machineTile.getProgress() > 0) {
                    bar(helper, (int) machineTile.getProgress(), (int) machineTile.getMaxProgress(), Component.translatable("ic2.probe.progress.full.name", (int) machineTile.getProgress() / 1000, (int) machineTile.getMaxProgress() / 1000).append("t"), -16733185);
                }
            }

            if (multiMachine instanceof BaseColossalMachineTileEntity colossalMachine) {
                IntList maxProgressFilter = new IntArrayList();
                IntList progressFilter = new IntArrayList();
                IntIterator activeSlotsIterator = colossalMachine.getActiveSlots();
                label38:
                while (true) {
                    int progress;
                    int maxProgress;
                    int maxIndex;
                    int index;
                    do {
                        if (!activeSlotsIterator.hasNext()) {
                            for (int i = 0; i < progressFilter.size(); ++i) {
                                bar(helper, progressFilter.getInt(i), maxProgressFilter.getInt(i), Component.translatable("ic2.probe.progress.full.name", progressFilter.getInt(i) / 1000, maxProgressFilter.getInt(i) / 1000).append("t"), -16733185);
                            }
                            break label38;
                        }

                        int slot = activeSlotsIterator.nextInt();
                        progress = (int) colossalMachine.getProgress(slot);
                        maxProgress = (int) colossalMachine.getMaxProgress(slot);
                        maxIndex = maxProgressFilter.indexOf(maxProgress);
                        index = progressFilter.indexOf(progress);
                    } while (maxIndex == index && index != -1);

                    maxProgressFilter.add(maxProgress);
                    progressFilter.add(progress);
                }
            }
            JadeCommonHandler.addTankInfo(helper, multiMachine);
        }
    }
}

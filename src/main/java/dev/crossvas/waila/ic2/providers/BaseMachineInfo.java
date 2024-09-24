package dev.crossvas.waila.ic2.providers;

import dev.crossvas.waila.ic2.base.interfaces.IInfoProvider;
import dev.crossvas.waila.ic2.base.interfaces.IWailaHelper;
import dev.crossvas.waila.ic2.utils.ColorUtils;
import ic2.core.block.machine.tileentity.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IChatComponent;

import java.text.DecimalFormat;

public class BaseMachineInfo implements IInfoProvider {

    public static final BaseMachineInfo THIS = new BaseMachineInfo();

    @Override
    public void addInfo(IWailaHelper helper, TileEntity blockEntity, EntityPlayer player) {
        if (blockEntity instanceof TileEntityElecMachine) {
            TileEntityElecMachine machine = (TileEntityElecMachine) blockEntity;
            IChatComponent tier = null;
            IChatComponent maxIn = null;
            IChatComponent usage = null;
            float progress = 0;
            float maxProgress = 0;
            float progressPerTick = 0;

            if (machine instanceof TileEntityElectricMachine) {
                TileEntityElectricMachine electricMachine = (TileEntityElectricMachine) machine;
                tier = tier(electricMachine.getSinkTier());
                maxIn = maxIn(electricMachine.maxInput);
                usage = usage(electricMachine.energyConsume);
                progress = electricMachine.getProgress();
                maxProgress = 1;
                progressPerTick = electricMachine.progress;
            }

            float speed = 0;
            float maxSpeed = 0;
            String name = "";
            double scaledProgress = 0;

            if (machine instanceof TileEntityAdvancedMachine) {
                TileEntityAdvancedMachine advMAchine = (TileEntityAdvancedMachine) machine;
                tier = tier(advMAchine.getSinkTier());
                maxIn = maxIn(advMAchine.maxInput);
                usage = usage(advMAchine.energyConsume);
                speed = advMAchine.speed;
                maxSpeed = advMAchine.MaxSpeed;
                scaledProgress = speed / maxSpeed;
                if (advMAchine instanceof TileEntityInduction) {
                    name = "probe.speed.heat";
                } else if (advMAchine instanceof TileEntityRotary) {
                    name = "probe.speed.rotation";
                } else if (advMAchine instanceof TileEntitySingularity) {
                    name = "probe.speed.pressure";
                } else if (advMAchine instanceof TileEntityCentrifuge || advMAchine instanceof TileEntityCompacting) {
                    name = "probe.speed.speed";
                }
            }
            if (machine instanceof TileEntityVacuumCanner) {
                TileEntityVacuumCanner canner = (TileEntityVacuumCanner) machine;
                tier = tier(canner.getSinkTier());
                maxIn = maxIn(canner.maxInput);
                usage = usage(canner.energyConsume);
                name = "probe.speed.vacuum";
                speed = canner.speed;
                maxSpeed = canner.MaxSpeed;
                scaledProgress = (double) speed / maxSpeed;
            }
            // tier
            if (tier != null) text(helper, tier);
            // maxIn
            if (maxIn != null) text(helper, maxIn);
            // usage
            if (usage != null) text(helper, usage);
            if (speed > 0) {
                bar(helper, (int) speed, (int) maxSpeed, translate(name, new DecimalFormat().format(scaledProgress * 100.0)), ColorUtils.SPEED);
            }
            if (progress > 0) {
                int scaledOp = (int) Math.min(6.0E7F, progress / progressPerTick);
                int scaledMaxOp = (int) Math.min(6.0E7F, maxProgress / progressPerTick);
                bar(helper, scaledOp, scaledMaxOp, translate("probe.progress.full.name", scaledOp, scaledMaxOp), ColorUtils.PROGRESS);
            }
        }
    }
}

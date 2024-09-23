package dev.crossvas.jadexic2c.providers;

import dev.crossvas.jadexic2c.base.interfaces.IInfoProvider;
import dev.crossvas.jadexic2c.base.interfaces.IJadeHelper;
import dev.crossvas.jadexic2c.utils.ColorUtils;
import ic2.core.block.base.tile.TileEntityAdvancedMachine;
import ic2.core.block.base.tile.TileEntityBasicElectricMachine;
import ic2.core.block.base.tile.TileEntityElecMachine;
import ic2.core.block.machine.med.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;

import java.text.DecimalFormat;

public class BaseMachineInfo implements IInfoProvider {

    public static final BaseMachineInfo THIS = new BaseMachineInfo();

    @Override
    public void addInfo(IJadeHelper helper, TileEntity blockEntity, EntityPlayer player) {
        if (blockEntity instanceof TileEntityElecMachine) {
            TileEntityElecMachine machine = (TileEntityElecMachine) blockEntity;
            ITextComponent tier = null;
            ITextComponent maxIn = null;
            ITextComponent usage = null;
            float progress = 0;
            float maxProgress = 0;
            float progressPerTick = 0;

            if (machine instanceof TileEntityBasicElectricMachine) {
                TileEntityBasicElectricMachine electricMachine = (TileEntityBasicElectricMachine) machine;
                tier = tier(electricMachine.getTier());
                maxIn = maxIn(electricMachine.maxInput);
                usage = usage(electricMachine.energyConsume);
                progress = electricMachine.getProgress();
                maxProgress = electricMachine.getMaxProgress();
                progressPerTick = electricMachine.progressPerTick;
            }

            float speed = 0;
            float maxSpeed = 0;
            String name = "";
            double scaledProgress = 0;

            if (machine instanceof TileEntityAdvancedMachine) {
                TileEntityAdvancedMachine advMAchine = (TileEntityAdvancedMachine) machine;
                tier = tier(advMAchine.getTier());
                maxIn = maxIn(advMAchine.maxInput);
                usage = usage(advMAchine.energyConsume);
                speed = advMAchine.speed;
                maxSpeed = advMAchine.getMaxSpeed();
                scaledProgress = speed / maxSpeed;
                if (advMAchine instanceof TileEntityInductionFurnace) {
                    name = "probe.speed.heat";
                } else if (advMAchine instanceof TileEntityRotaryMacerator) {
                    name = "probe.speed.rotation";
                } else if (advMAchine instanceof TileEntitySingularityCompressor) {
                    name = "probe.speed.pressure";
                } else if (advMAchine instanceof TileEntityCentrifugalExtractor || advMAchine instanceof TileEntityCompactingRecycler) {
                    name = "probe.speed.speed";
                }
            }
            if (machine instanceof TileEntityVacuumCanner) {
                TileEntityVacuumCanner canner = (TileEntityVacuumCanner) machine;
                tier = tier(canner.getTier());
                maxIn = maxIn(canner.maxInput);
                usage = usage(canner.energyConsume);
                name = "probe.speed.vacuum";
                speed = canner.getSpeed();
                maxSpeed = canner.getMaxSpeed();
                scaledProgress = (double) speed / maxSpeed;
            }
            // tier
            if (tier != null) text(helper, tier);
            // maxIn
            if (maxIn != null) text(helper, maxIn);
            // usage
            if (usage != null) text(helper, usage);
            if (speed > 0) {
                bar(helper, (int) speed, (int) maxSpeed, translatable(name, new DecimalFormat().format(scaledProgress * 100.0)), ColorUtils.SPEED);
            }
            if (progress > 0) {
                int scaledOp = (int) Math.min(6.0E7F, progress / progressPerTick);
                int scaledMaxOp = (int) Math.min(6.0E7F, maxProgress / progressPerTick);
                bar(helper, scaledOp, scaledMaxOp, translatable("probe.progress.full.name", scaledOp, scaledMaxOp), ColorUtils.PROGRESS);
            }
        }
    }
}

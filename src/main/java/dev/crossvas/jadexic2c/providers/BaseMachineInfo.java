package dev.crossvas.jadexic2c.providers;

import dev.crossvas.jadexic2c.base.JadeHelper;
import dev.crossvas.jadexic2c.base.interfaces.IInfoProvider;
import ic2.api.energy.EnergyNet;
import ic2.core.block.base.tiles.impls.machine.single.BaseAdvMachineTileEntity;
import ic2.core.block.base.tiles.impls.machine.single.BaseMachineTileEntity;
import ic2.core.block.machines.tiles.lv.RareEarthExtractorTileEntity;
import ic2.core.block.machines.tiles.mv.RareEarthCentrifugeTileEntity;
import ic2.core.block.machines.tiles.mv.RefineryTileEntity;
import ic2.core.block.machines.tiles.mv.SlowGrinderTileEntity;
import ic2.core.block.machines.tiles.mv.VacuumCannerTileEntity;
import ic2.core.utils.helpers.Formatters;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.text.DecimalFormat;

public class BaseMachineInfo implements IInfoProvider {

    public static final BaseMachineInfo THIS = new BaseMachineInfo();

    @Override
    public void addInfo(JadeHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof BaseMachineTileEntity baseMachine) {
            helper.defaultText("ic2.probe.eu.tier.name", EnergyNet.INSTANCE.getDisplayTier(baseMachine.getTier()));
            helper.defaultText("ic2.probe.eu.max_in.name", baseMachine.getMaxInput());
            helper.defaultText("ic2.probe.eu.usage.name", baseMachine.getEnergyPerTick());
            if (baseMachine instanceof SlowGrinderTileEntity slowGrinder) {
                helper.defaultText("ic2.probe.scrap.chance.name", Formatters.XP_FORMAT.format(slowGrinder.getChance(0.25F) * 100.0F));
            }
            if (baseMachine instanceof RefineryTileEntity refinery) {
                helper.addTankInfo(refinery);
            }

            int speed;
            int maxSpeed;
            Component speedName;
            double scaledProgress;
            float progress = baseMachine.getProgress();
            float maxProgress = baseMachine.getMaxProgress();
            float progressPerTick = baseMachine.progressPerTick;

            if (baseMachine instanceof BaseAdvMachineTileEntity adv) {
                speed = adv.getSpeed();
                maxSpeed = adv.getMaxSpeed();
                speedName = adv.getSpeedName();
                scaledProgress = (double) speed / maxSpeed;
                if (speed > 0) {
                    helper.bar(speed, maxSpeed, speedName.plainCopy().append(": " + new DecimalFormat().format(scaledProgress * 100.0) + "%"), -295680);
                }
            }
            if (baseMachine instanceof VacuumCannerTileEntity canner) {
                speed = canner.getSpeed();
                maxSpeed = canner.getMaxSpeed();
                speedName = canner.getSpeedName();
                scaledProgress = (double) speed / maxSpeed;
                if (speed > 0) {
                    helper.bar(speed, maxSpeed, speedName.plainCopy().append(": " + new DecimalFormat().format(scaledProgress * 100.0) + "%"), -295680);
                }
            }
            int material;
            if (baseMachine instanceof RareEarthExtractorTileEntity rareExtractor) {
                material = (int) rareExtractor.materialProgress;
                if (material > 0) {
                    helper.bar(material, 1000, translate("ic2.probe.progress.material.name", Formatters.EU_READER_FORMAT.format(material)), -5829955);
                }
            } else if (baseMachine instanceof RareEarthCentrifugeTileEntity rareEarthCentrifuge) {
                material = (int) rareEarthCentrifuge.materialProgress;
                if (material > 0) {
                    helper.bar(material, 1000, translate("ic2.probe.progress.material.name", Formatters.EU_READER_FORMAT.format(material)), -5829955);
                }
                speed = rareEarthCentrifuge.getSpeed();
                maxSpeed = rareEarthCentrifuge.getMaxSpeed();
                speedName = rareEarthCentrifuge.getSpeedName();
                scaledProgress = (double) speed / maxSpeed;
                if (speed > 0) {
                    helper.bar(speed, maxSpeed, speedName.plainCopy().append(": " + new DecimalFormat().format(scaledProgress * 100.0) + "%"), -295680);
                }
            }

            if (progress > 0) {
                int scaledOp = (int) Math.min(6.0E7F, progress / progressPerTick);
                int scaledMaxOp = (int) Math.min(6.0E7F, maxProgress / progressPerTick);
                helper.bar(scaledOp, scaledMaxOp, translate("ic2.probe.progress.full.name", scaledOp, scaledMaxOp).append("t"), -16733185);
            }
        }
    }
}

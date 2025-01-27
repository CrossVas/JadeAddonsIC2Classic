package dev.crossvas.jadexic2c.providers;

import dev.crossvas.jadexic2c.base.JadeHelper;
import dev.crossvas.jadexic2c.base.interfaces.IInfoProvider;
import dev.crossvas.jadexic2c.helpers.Formatter;
import ic2.api.energy.EnergyNet;
import ic2.core.block.base.tiles.BaseElectricTileEntity;
import ic2.core.block.base.tiles.impls.BaseFluxGeneratorTileEntity;
import ic2.core.block.cables.mointor.MonitorTileEntity;
import ic2.core.block.machines.tiles.ev.CrafterTileEntity;
import ic2.core.block.machines.tiles.hv.ElectricEnchanterTileEntity;
import ic2.core.block.machines.tiles.hv.MassFabricatorTileEntity;
import ic2.core.block.machines.tiles.hv.TerraformerTileEntity;
import ic2.core.block.machines.tiles.luv.TeleporterHubTileEntity;
import ic2.core.block.machines.tiles.lv.CropMatronTileEntity;
import ic2.core.block.machines.tiles.lv.MachineBufferTileEntity;
import ic2.core.block.machines.tiles.lv.MachineTankTileEntity;
import ic2.core.block.machines.tiles.lv.MagnetizerTileEntity;
import ic2.core.block.machines.tiles.mv.ChunkloaderTileEntity;
import ic2.core.block.machines.tiles.mv.CropHarvesterTileEntity;
import ic2.core.block.machines.tiles.mv.ReactorPlannerTileEntity;
import ic2.core.block.machines.tiles.mv.TeslaCoilTileEntity;
import ic2.core.utils.math.ColorUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class ElectricBlockInfo implements IInfoProvider {

    public static final ElectricBlockInfo THIS = new ElectricBlockInfo();

    @Override
    public void addInfo(JadeHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof BaseElectricTileEntity baseTile) {
            if (baseTile instanceof ChunkloaderTileEntity chunkLoader) {
                helper.defaultText("ic2.probe.eu.tier.name", EnergyNet.INSTANCE.getDisplayTier(chunkLoader.getTier()));
                helper.defaultText("ic2.probe.eu.max_in.name", chunkLoader.getMaxInput());
                helper.defaultText("ic2.probe.eu.usage.name", ChunkloaderTileEntity.POWER_COST[chunkLoader.getRadius()] * (chunkLoader.doesChunkProcessing ? 2 : 1));
                helper.text(translate("ic2.probe.chunkloader.radius", string(String.valueOf(chunkLoader.getRadius())).withStyle(ChatFormatting.AQUA)).withStyle(ChatFormatting.GOLD));
                boolean chunkTicks = chunkLoader.doesChunkProcessing;
                helper.text(translate("ic2.probe.chunkloader.ticks", (chunkTicks ? ChatFormatting.GREEN : ChatFormatting.RED) + String.valueOf(chunkTicks)).withStyle(ChatFormatting.GOLD));
            }
            if (baseTile instanceof MonitorTileEntity monitor) {
                if (monitor.isMaster()) {
                    helper.defaultText("ic2.probe.eu.tier.name", EnergyNet.INSTANCE.getDisplayTier(monitor.getTier()));
                    helper.defaultText("ic2.probe.eu.max_in.name", monitor.getMaxInput());
                    helper.defaultText("ic2.probe.eu.usage.name", monitor.isActive() ? Math.max(monitor.width, monitor.height) : 0);
                }
            }
            if (baseTile instanceof CrafterTileEntity crafter) {
                helper.defaultText("ic2.probe.eu.tier.name", EnergyNet.INSTANCE.getDisplayTier(crafter.getTier()));
                helper.defaultText("ic2.probe.eu.max_in.name", crafter.getMaxInput());
                helper.defaultText("ic2.probe.crafter.usage", 25);
                helper.defaultText("ic2.probe.crafter.delay", crafter.getSpeed());
                helper.defaultText("ic2.probe.crafter.crafts", crafter.getCrafts());
            }
            if (baseTile instanceof TerraformerTileEntity || baseTile instanceof ReactorPlannerTileEntity || baseTile instanceof BaseFluxGeneratorTileEntity || baseTile instanceof CropHarvesterTileEntity ||
                    baseTile instanceof MachineBufferTileEntity || baseTile instanceof MagnetizerTileEntity || baseTile instanceof TeslaCoilTileEntity || baseTile instanceof TeleporterHubTileEntity) {
                helper.defaultText("ic2.probe.eu.tier.name", EnergyNet.INSTANCE.getDisplayTier(baseTile.getTier()));
                helper.defaultText("ic2.probe.eu.max_in.name", baseTile.getMaxInput());
            }
            if (baseTile instanceof MachineTankTileEntity tank) {
                helper.defaultText("ic2.probe.eu.tier.name", EnergyNet.INSTANCE.getDisplayTier(tank.getTier()));
                helper.defaultText("ic2.probe.eu.max_in.name", tank.getMaxInput());
                helper.addTankInfo(tank);
            }

            if (baseTile instanceof CropMatronTileEntity cropmatron) {
                helper.defaultText("ic2.probe.eu.tier.name", EnergyNet.INSTANCE.getDisplayTier(cropmatron.getTier()));
                helper.defaultText("ic2.probe.eu.max_in.name", cropmatron.getMaxInput());
                helper.addTankInfo(cropmatron);
            }
            if (baseTile instanceof ElectricEnchanterTileEntity enchanter) {
                helper.defaultText("ic2.probe.eu.tier.name", EnergyNet.INSTANCE.getDisplayTier(enchanter.getTier()));
                helper.defaultText("ic2.probe.eu.max_in.name", enchanter.getMaxInput());
                helper.defaultText("ic2.probe.eu.usage.name", 500);
                int storedXP = enchanter.storedExperience;
                int progress = (int) enchanter.getProgress();
                int maxProgress = (int) enchanter.getMaxProgress();
                if (storedXP <= 0) {
                    helper.text(translate("ic2.probe.enchanter.missing").withStyle(ChatFormatting.RED));
                } else {
                    helper.bar(storedXP, 1000, translate("ic2.probe.machine.xp", storedXP), ColorUtils.GREEN);
                }
                if (progress > 0) {
                    helper.bar(progress, maxProgress, translate("ic2.probe.progress.full.name", progress, maxProgress).append("t"), -16733185);
                }
            }
            if (baseTile instanceof MassFabricatorTileEntity massFab) {
                helper.defaultText("ic2.probe.eu.tier.name", EnergyNet.INSTANCE.getDisplayTier(massFab.getTier()));
                helper.defaultText("ic2.probe.eu.max_in.name", massFab.getMaxInput());
                int progress = (int) massFab.getProgress();
                int maxProgress = (int) massFab.getMaxProgress();
                if (progress > 0) {
                    double finalProgress = progress / massFab.getMaxProgress() * 100.0;
                    if (finalProgress > 100) finalProgress = 100;
                    helper.bar(progress, maxProgress, translate("ic2.probe.progress.moderate.name",
                            Formatter.THERMAL_GEN.format(finalProgress)), -4441721);
                }
                if (massFab.getScrap() > 0) {
                    helper.bar(massFab.getScrap(), massFab.getLastScrap() * 2, translate("ic2.probe.matter.amplifier.name",
                            massFab.getScrap()), -10996205);
                }
            }
        }
    }
}

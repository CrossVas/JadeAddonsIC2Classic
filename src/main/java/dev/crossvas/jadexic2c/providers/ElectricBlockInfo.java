package dev.crossvas.jadexic2c.providers;

import dev.crossvas.jadexic2c.base.JadeCommonHandler;
import dev.crossvas.jadexic2c.base.interfaces.IInfoProvider;
import dev.crossvas.jadexic2c.base.interfaces.IJadeHelper;
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
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class ElectricBlockInfo implements IInfoProvider {

    public static final ElectricBlockInfo THIS = new ElectricBlockInfo();

    @Override
    public void addInfo(IJadeHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof BaseElectricTileEntity baseTile) {
            if (baseTile instanceof ChunkloaderTileEntity chunkLoader) {
                defaultText(helper, "ic2.probe.eu.tier.name", EnergyNet.INSTANCE.getDisplayTier(chunkLoader.getTier()));
                defaultText(helper, "ic2.probe.eu.max_in.name", chunkLoader.getMaxInput());
                defaultText(helper, "ic2.probe.eu.usage.name", ChunkloaderTileEntity.POWER_COST[chunkLoader.getRadius()] * (chunkLoader.doesChunkProcessing ? 2 : 1));
                text(helper, Component.translatable("ic2.probe.chunkloader.radius", Component.literal(String.valueOf(chunkLoader.getRadius())).withStyle(ChatFormatting.AQUA)).withStyle(ChatFormatting.GOLD));
                boolean chunkTicks = chunkLoader.doesChunkProcessing;
                text(helper, Component.translatable("ic2.probe.chunkloader.ticks", (chunkTicks ? ChatFormatting.GREEN : ChatFormatting.RED) + String.valueOf(chunkTicks)).withStyle(ChatFormatting.GOLD));
            }
            if (baseTile instanceof MonitorTileEntity monitor) {
                if (monitor.isMaster()) {
                    defaultText(helper, "ic2.probe.eu.tier.name", EnergyNet.INSTANCE.getDisplayTier(monitor.getTier()));
                    defaultText(helper, "ic2.probe.eu.max_in.name", monitor.getMaxInput());
                    defaultText(helper, "ic2.probe.eu.usage.name", monitor.isActive() ? Math.max(monitor.width, monitor.height) : 0);
                }
            }
            if (baseTile instanceof CrafterTileEntity crafter) {
                defaultText(helper, "ic2.probe.eu.tier.name", EnergyNet.INSTANCE.getDisplayTier(crafter.getTier()));
                defaultText(helper, "ic2.probe.eu.max_in.name", crafter.getMaxInput());
                defaultText(helper, "ic2.probe.crafter.usage", 25);
                defaultText(helper, "ic2.probe.crafter.delay", crafter.getSpeed());
                defaultText(helper, "ic2.probe.crafter.crafts", crafter.getCrafts());
            }
            if (baseTile instanceof TerraformerTileEntity || baseTile instanceof ReactorPlannerTileEntity || baseTile instanceof BaseFluxGeneratorTileEntity || baseTile instanceof CropHarvesterTileEntity ||
                    baseTile instanceof MachineBufferTileEntity || baseTile instanceof MagnetizerTileEntity || baseTile instanceof TeslaCoilTileEntity || baseTile instanceof TeleporterHubTileEntity) {
                defaultText(helper, "ic2.probe.eu.tier.name", EnergyNet.INSTANCE.getDisplayTier(baseTile.getTier()));
                defaultText(helper, "ic2.probe.eu.max_in.name", baseTile.getMaxInput());
            }
            if (baseTile instanceof MachineTankTileEntity tank) {
                defaultText(helper, "ic2.probe.eu.tier.name", EnergyNet.INSTANCE.getDisplayTier(tank.getTier()));
                defaultText(helper, "ic2.probe.eu.max_in.name", tank.getMaxInput());
                JadeCommonHandler.addTankInfo(helper, tank);
            }

            if (baseTile instanceof CropMatronTileEntity cropmatron) {
                defaultText(helper, "ic2.probe.eu.tier.name", EnergyNet.INSTANCE.getDisplayTier(cropmatron.getTier()));
                defaultText(helper, "ic2.probe.eu.max_in.name", cropmatron.getMaxInput());
                JadeCommonHandler.addTankInfo(helper, cropmatron);
            }
            if (baseTile instanceof ElectricEnchanterTileEntity enchanter) {
                defaultText(helper, "ic2.probe.eu.tier.name", EnergyNet.INSTANCE.getDisplayTier(enchanter.getTier()));
                defaultText(helper, "ic2.probe.eu.max_in.name", enchanter.getMaxInput());
                defaultText(helper, "ic2.probe.eu.usage.name", 500);
                int storedXP = enchanter.storedExperience;
                int progress = (int) enchanter.getProgress();
                int maxProgress = (int) enchanter.getMaxProgress();
                if (storedXP <= 0) {
                    text(helper, Component.translatable("ic2.probe.enchanter.missing").withStyle(ChatFormatting.RED));
                } else {
                    bar(helper, storedXP, 1000, Component.translatable("ic2.probe.machine.xp", storedXP), ColorUtils.GREEN);
                }
                if (progress > 0) {
                    bar(helper, progress, maxProgress, Component.translatable("ic2.probe.progress.full.name", progress, maxProgress).append("t"), -16733185);
                }
            }
            if (baseTile instanceof MassFabricatorTileEntity massFab) {
                defaultText(helper, "ic2.probe.eu.tier.name", EnergyNet.INSTANCE.getDisplayTier(massFab.getTier()));
                defaultText(helper, "ic2.probe.eu.max_in.name", massFab.getMaxInput());
                int progress = (int) massFab.getProgress();
                int maxProgress = (int) massFab.getMaxProgress();
                if (progress > 0) {
                    double finalProgress = progress / massFab.getMaxProgress() * 100.0;
                    if (finalProgress > 100) finalProgress = 100;
                    bar(helper, progress, maxProgress, Component.translatable("ic2.probe.progress.moderate.name",
                            Formatter.THERMAL_GEN.format(finalProgress)), -4441721);
                }
                if (massFab.getScrap() > 0) {
                    bar(helper, massFab.getScrap(), massFab.getLastScrap() * 2, Component.translatable("ic2.probe.matter.amplifier.name",
                            massFab.getScrap()), -10996205);
                }
            }
        }
    }
}

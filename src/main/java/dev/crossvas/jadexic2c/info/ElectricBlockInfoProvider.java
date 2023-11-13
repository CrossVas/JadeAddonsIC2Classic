package dev.crossvas.jadexic2c.info;

import dev.crossvas.jadexic2c.IHelper;
import dev.crossvas.jadexic2c.JadeIC2CPluginHandler;
import dev.crossvas.jadexic2c.utils.ColorMix;
import dev.crossvas.jadexic2c.utils.Formatter;
import dev.crossvas.jadexic2c.utils.Helpers;
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
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum ElectricBlockInfoProvider implements IHelper<BlockEntity> {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (!shouldAddInfo(blockAccessor, "BaseElectricMachine")) {
            return;
        }

        CompoundTag tag = getData(blockAccessor, "BaseElectricMachine");
        if (blockAccessor.getBlockEntity() instanceof BaseElectricTileEntity tile) {
            if (tile instanceof ChunkloaderTileEntity chunk) {
                Helpers.text(iTooltip, "ic2.probe.eu.tier.name", EnergyNet.INSTANCE.getDisplayTier(chunk.getTier()));
                Helpers.text(iTooltip, "ic2.probe.eu.max_in.name", chunk.getMaxInput());
                Helpers.text(iTooltip, "ic2.probe.eu.usage.name", ChunkloaderTileEntity.POWER_COST[tag.getInt("radius")] * (tag.getBoolean("doesChunkProcessing") ? 2 : 1));
            }
            if (tile instanceof MonitorTileEntity monitor) {
                if (monitor.isMaster()) {
                    Helpers.text(iTooltip, "ic2.probe.eu.tier.name", EnergyNet.INSTANCE.getDisplayTier(monitor.getTier()));
                    Helpers.text(iTooltip, "ic2.probe.eu.max_in.name", monitor.getMaxInput());
                    Helpers.text(iTooltip, "ic2.probe.eu.usage.name", tag.getBoolean("isActive") ? Math.max(tag.getInt("width"), tag.getInt("height")) : 0);
                }
            }
            if (tile instanceof CrafterTileEntity crafter) {
                Helpers.text(iTooltip, "ic2.probe.eu.tier.name", EnergyNet.INSTANCE.getDisplayTier(crafter.getTier()));
                Helpers.text(iTooltip, "ic2.probe.eu.max_in.name", crafter.getMaxInput());
                Helpers.text(iTooltip, "ic2.probe.crafter.usage", 25);
                Helpers.text(iTooltip, "ic2.probe.crafter.delay", tag.getInt("speed"));
                Helpers.text(iTooltip, "ic2.probe.crafter.crafts", tag.getInt("crafts"));
            }
            if (tile instanceof TerraformerTileEntity || tile instanceof ReactorPlannerTileEntity || tile instanceof BaseFluxGeneratorTileEntity || tile instanceof CropHarvesterTileEntity ||
                    tile instanceof MachineBufferTileEntity || tile instanceof MagnetizerTileEntity || tile instanceof TeslaCoilTileEntity || tile instanceof TeleporterHubTileEntity) {
                Helpers.text(iTooltip, "ic2.probe.eu.tier.name", EnergyNet.INSTANCE.getDisplayTier(tile.getTier()));
                Helpers.text(iTooltip, "ic2.probe.eu.max_in.name", tile.getMaxInput());
            }
            if (tile instanceof MachineTankTileEntity tank) {
                Helpers.text(iTooltip, "ic2.probe.eu.tier.name", EnergyNet.INSTANCE.getDisplayTier(tank.getTier()));
                Helpers.text(iTooltip, "ic2.probe.eu.max_in.name", tank.getMaxInput());
                Helpers.addClientTankFromTag(iTooltip, blockAccessor);
            }

            if (tile instanceof CropMatronTileEntity cropmatron) {
                Helpers.text(iTooltip, "ic2.probe.eu.tier.name", EnergyNet.INSTANCE.getDisplayTier(cropmatron.getTier()));
                Helpers.text(iTooltip, "ic2.probe.eu.max_in.name", cropmatron.getMaxInput());
                Helpers.addClientTankFromTag(iTooltip, blockAccessor);
            }

            if (tile instanceof ElectricEnchanterTileEntity enchanter) {
                Helpers.text(iTooltip, "ic2.probe.eu.tier.name", EnergyNet.INSTANCE.getDisplayTier(tile.getTier()));
                Helpers.text(iTooltip, "ic2.probe.eu.max_in.name", tile.getMaxInput());
                Helpers.text(iTooltip, "ic2.probe.eu.usage.name", 500);
                int storedXP = tag.getInt("storedXP");
                float progress = tag.getFloat("progress");
                Helpers.monoBarLiteral(iTooltip, storedXP, 1000, Component.translatable("ic2.probe.xp.prefix.name").append(String.valueOf(storedXP)).append(Component.translatable("ic2.probe.xp.suffix.name")), ColorMix.MONO_GREEN);
                if (progress > 0.0F) {
                    Helpers.barLiteral(iTooltip, (int) progress, (int) enchanter.getMaxProgress(), Component.translatable("ic2.probe.progress.full.name", (int) progress, (int) enchanter.getMaxProgress()).append(" t").withStyle(ChatFormatting.WHITE), ColorMix.BLUE);
                }
            }

            if (tile instanceof MassFabricatorTileEntity massFab) {
                Helpers.text(iTooltip, "ic2.probe.eu.tier.name", EnergyNet.INSTANCE.getDisplayTier(massFab.getTier()));
                Helpers.text(iTooltip, "ic2.probe.eu.max_in.name", massFab.getMaxInput());
                float progress = tag.getFloat("uuProgress");
                if (progress > 0.0F) {
                    Helpers.barLiteral(iTooltip, (int) progress, (int) massFab.getMaxProgress(), Component.translatable("ic2.probe.progress.moderate.name",
                            Formatter.THERMAL_GEN.format((double) (progress / massFab.getMaxProgress()) * 100.0)), ColorMix.PURPLE);
                }
                if (tag.getInt("scrap") > 0) {
                    Helpers.barLiteral(iTooltip, tag.getInt("scrap"), tag.getInt("scrapValue") * 2, Component.translatable("ic2.probe.matter.amplifier.name",
                            tag.getInt("scrap")), ColorMix.BROWN);
                }
            }

        }
    }

    @Override
    public ResourceLocation getUid() {
        return JadeIC2CPluginHandler.EU_READER_INFO;
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, ServerPlayer serverPlayer, Level level, BlockEntity blockEntity, boolean b) {
        if (blockEntity instanceof BaseElectricTileEntity tile) {
            CompoundTag tag = new CompoundTag();
            if (tile instanceof ChunkloaderTileEntity chunk) {
                tag.putInt("radius", chunk.radius);
                tag.putBoolean("doesChunkProcessing", chunk.doesChunkProcessing);
            } else if (tile instanceof MonitorTileEntity monitor) {
                tag.putBoolean("isMaster", monitor.isMaster());
                tag.putBoolean("isActive", monitor.isActive());
                tag.putInt("width", monitor.width);
                tag.putInt("height", monitor.height);
            } else if (tile instanceof CrafterTileEntity crafter) {
                tag.putInt("speed", crafter.getSpeed());
                tag.putInt("crafts", crafter.getCrafts());
            } else if (tile instanceof ElectricEnchanterTileEntity enchanter) {
                tag.putInt("storedXP", enchanter.storedExperience);
                tag.putFloat("progress", enchanter.getProgress());
            } else if (tile instanceof MassFabricatorTileEntity massFab) {
                tag.putFloat("uuProgress", massFab.getProgress());
                tag.putInt("scrap", massFab.getScrap());
                tag.putInt("scrapValue", massFab.getLastScrap());
            } else if (tile instanceof MachineTankTileEntity tank) {
                Helpers.loadTankData(compoundTag, tank);
                Helpers.loadTankData(tank.tank, compoundTag);
            } else if (tile instanceof CropMatronTileEntity cropmatron) {
                Helpers.loadTankData(compoundTag, cropmatron);
            }
            compoundTag.put("BaseElectricMachine", tag);
        }
    }
}

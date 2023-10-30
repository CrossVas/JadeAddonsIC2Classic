package dev.crossvas.jadexic2c.info;

import dev.crossvas.jadexic2c.IHelper;
import dev.crossvas.jadexic2c.JadeIC2CPluginHandler;
import dev.crossvas.jadexic2c.utils.Helpers;
import ic2.api.energy.EnergyNet;
import ic2.core.block.base.tiles.BaseTileEntity;
import ic2.core.block.generators.tiles.SteamTurbineTileEntity;
import ic2.probeplugin.base.ProbePluginHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum SteamTurbineInfoProvider implements IHelper {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (!shouldAddInfo(blockAccessor, "SteamTurbineInfo")) {
            return;
        }

        CompoundTag tag = getData(blockAccessor, "SteamTurbineInfo");
        if (blockAccessor.getBlockEntity() instanceof BaseTileEntity tile) {
            if (tile instanceof SteamTurbineTileEntity turbine) {
                Helpers.text(iTooltip, "ic2.probe.eu.tier.name", EnergyNet.INSTANCE.getDisplayTier(turbine.getSourceTier()));
                Helpers.text(iTooltip,"ic2.probe.eu.output.current.name", ProbePluginHelper.formatNumber((double) tag.getFloat("production"), 5));
                Helpers.text(iTooltip,"ic2.probe.eu.output.max.name", turbine.getMaxEnergyOutput());
                Helpers.addClientTankFromTag(iTooltip, blockAccessor);
            }
        }
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, ServerPlayer serverPlayer, Level level, BlockEntity blockEntity, boolean b) {
        CompoundTag tag  = new CompoundTag();
        if (blockEntity instanceof BaseTileEntity tile) {
            if (tile instanceof SteamTurbineTileEntity turbine) {
                tag.putFloat("production", turbine.getEUProduction());
                Helpers.loadTankData(compoundTag, turbine);
            }
        }
        compoundTag.put("SteamTurbineInfo", tag);
    }

    @Override
    public ResourceLocation getUid() {
        return JadeIC2CPluginHandler.EU_READER_INFO;
    }
}

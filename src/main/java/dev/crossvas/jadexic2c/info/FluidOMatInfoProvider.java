package dev.crossvas.jadexic2c.info;

import dev.crossvas.jadexic2c.JadeIC2CPluginHandler;
import dev.crossvas.jadexic2c.helpers.IHelper;
import dev.crossvas.jadexic2c.helpers.TankHelper;
import ic2.core.block.personal.tile.FluidOMatTileEntity;
import ic2.core.inventory.filter.SpecialFilters;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum FluidOMatInfoProvider implements IHelper<BlockEntity> {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (!canHandle(blockAccessor, SpecialFilters.EU_READER)) {
            return;
        }

        if (blockAccessor.getBlockEntity() instanceof FluidOMatTileEntity) {
            TankHelper.addClientTankFromTag(iTooltip, blockAccessor);
        }
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, ServerPlayer serverPlayer, Level level, BlockEntity blockEntity, boolean b) {
        if (blockEntity instanceof FluidOMatTileEntity mat) {
            TankHelper.loadTankData(compoundTag, mat);
        }
    }

    @Override
    public ResourceLocation getUid() {
        return JadeIC2CPluginHandler.TOOLTIP_RENDERER;
    }
}

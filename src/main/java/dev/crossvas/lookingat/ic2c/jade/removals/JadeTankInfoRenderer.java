package dev.crossvas.lookingat.ic2c.jade.removals;

import dev.crossvas.lookingat.ic2c.LookingAtTags;
import dev.crossvas.lookingat.ic2c.base.LookingAtCommonHandler;
import ic2.core.inventory.filter.SpecialFilters;
import ic2.core.utils.helpers.StackUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import snownee.jade.api.*;
import snownee.jade.api.config.IPluginConfig;

public class JadeTankInfoRenderer implements IBlockComponentProvider, IServerDataProvider<BlockEntity> {

    public static final JadeTankInfoRenderer INSTANCE = new JadeTankInfoRenderer();

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (StackUtil.hasHotbarItems(blockAccessor.getPlayer(), SpecialFilters.EU_READER)) {
            if (blockAccessor.getServerData().contains("TankRemovals")) {
                iTooltip.remove(Identifiers.UNIVERSAL_FLUID_STORAGE);
                iTooltip.remove(Identifiers.UNIVERSAL_FLUID_STORAGE_DETAILED);
            }
        }
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, ServerPlayer serverPlayer, Level level, BlockEntity blockEntity, boolean b) {
        if (LookingAtCommonHandler.TANK_REMOVAL.contains(blockEntity)) {
            CompoundTag tag = new CompoundTag();
            compoundTag.put("TankRemovals", tag);
        }
    }

    @Override
    public int getDefaultPriority() {
        return TooltipPosition.TAIL;
    }

    @Override
    public ResourceLocation getUid() {
        return LookingAtTags.TANK_RENDER;
    }
}

package dev.crossvas.jadexic2c.base.removals;

import dev.crossvas.jadexic2c.JadeCommonHandler;
import dev.crossvas.jadexic2c.JadeTags;
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
        if (blockAccessor.getServerData().contains("TankRemovals")) {
            iTooltip.remove(Identifiers.UNIVERSAL_FLUID_STORAGE);
            iTooltip.remove(Identifiers.UNIVERSAL_FLUID_STORAGE_DETAILED);
        }
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, ServerPlayer serverPlayer, Level level, BlockEntity blockEntity, boolean b) {
        if (JadeCommonHandler.TANK_REMOVAL.contains(blockEntity)) {
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
        return JadeTags.TANK_RENDER;
    }
}

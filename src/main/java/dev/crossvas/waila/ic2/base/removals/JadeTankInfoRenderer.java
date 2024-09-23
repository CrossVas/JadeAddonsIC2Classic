package dev.crossvas.waila.ic2.base.removals;

import dev.crossvas.waila.ic2.base.JadeCommonHandler;
import dev.crossvas.waila.ic2.WailaTags;
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
        if (StackUtil.hasHotbarItems(blockAccessor.getPlayer(), SpecialFilters.EU_READER) || blockAccessor.getPlayer().isCreative()) {
            if (blockAccessor.getServerData().contains(WailaTags.TAG_TANKS)) {
                iTooltip.remove(Identifiers.UNIVERSAL_FLUID_STORAGE);
                iTooltip.remove(Identifiers.UNIVERSAL_FLUID_STORAGE_DETAILED);
            }
        }
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, ServerPlayer serverPlayer, Level level, BlockEntity blockEntity, boolean b) {
        if (JadeCommonHandler.TANK_REMOVAL.contains(blockEntity)) {
            CompoundTag tag = new CompoundTag();
            compoundTag.put(WailaTags.TAG_TANKS, tag);
        }
    }

    @Override
    public int getDefaultPriority() {
        return TooltipPosition.TAIL;
    }

    @Override
    public ResourceLocation getUid() {
        return WailaTags.TANK_RENDER;
    }
}

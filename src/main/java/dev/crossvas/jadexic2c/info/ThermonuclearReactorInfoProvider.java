package dev.crossvas.jadexic2c.info;

import dev.crossvas.jadexic2c.JadeIC2CPluginHandler;
import dev.crossvas.jadexic2c.helpers.BarHelper;
import dev.crossvas.jadexic2c.helpers.IHelper;
import dev.crossvas.jadexic2c.helpers.TankHelper;
import ic2.core.block.base.tiles.BaseLinkingTileEntity;
import ic2.core.block.base.tiles.BaseMultiBlockTileEntity;
import ic2.core.block.base.tiles.BaseTileEntity;
import ic2.core.block.machines.tiles.luv.FusionReactorTileEntity;
import ic2.core.inventory.filter.SpecialFilters;
import ic2.core.utils.math.ColorUtils;
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

public enum ThermonuclearReactorInfoProvider implements IHelper<BlockEntity> {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (!shouldAddInfo(blockAccessor, "FusionReactorInfo", SpecialFilters.EU_READER)) {
            return;
        }

        if (blockAccessor.getBlockEntity() instanceof BaseTileEntity tile) {
            if (tile instanceof BaseMultiBlockTileEntity multiBlock) {
                if (multiBlock instanceof FusionReactorTileEntity reactor) {
                    TankHelper.addClientTankFromTag(iTooltip, blockAccessor);
                    if (!reactor.isValid || reactor.isDynamic()) {
                        long time = tile.clockTime(512);
                        BarHelper.bar(iTooltip, (int) time, 512, Component.literal("Next Reform: ").append(String.valueOf(512 - time)).append(" Ticks").withStyle(ChatFormatting.WHITE), ColorUtils.GRAY);
                    }
                }
            }
            if (tile instanceof BaseLinkingTileEntity linking) {
                BlockEntity master = linking.getMaster();
                if (master instanceof FusionReactorTileEntity) {
                    TankHelper.addClientTankFromTag(iTooltip, blockAccessor);
                }
            }
        }
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, ServerPlayer serverPlayer, Level level, BlockEntity blockEntity, boolean b) {
        if (blockEntity instanceof BaseTileEntity tile) {
            CompoundTag tag = new CompoundTag();
            if (tile instanceof BaseMultiBlockTileEntity multi) {
                if (multi instanceof FusionReactorTileEntity tunnel) {
                    TankHelper.loadTankData(compoundTag, tunnel);
                }
            } else if (tile instanceof BaseLinkingTileEntity linking) {
                TankHelper.loadTankData(compoundTag, linking);
            }
            compoundTag.put("FusionReactorInfo", tag);
        }

    }

    @Override
    public ResourceLocation getUid() {
        return JadeIC2CPluginHandler.TOOLTIP_RENDERER;
    }
}

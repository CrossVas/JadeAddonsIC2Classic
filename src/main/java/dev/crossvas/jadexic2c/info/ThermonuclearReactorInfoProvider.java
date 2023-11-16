package dev.crossvas.jadexic2c.info;

import dev.crossvas.jadexic2c.IHelper;
import dev.crossvas.jadexic2c.JadeIC2CPluginHandler;
import dev.crossvas.jadexic2c.utils.Helpers;
import ic2.core.block.base.tiles.BaseMultiBlockTileEntity;
import ic2.core.block.base.tiles.BaseTileEntity;
import ic2.core.block.machines.tiles.luv.FusionReactorTileEntity;
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
        if (!shouldAddInfo(blockAccessor, "FusionReactorInfo")) {
            return;
        }

        if (blockAccessor.getBlockEntity() instanceof BaseTileEntity tile) {
            if (tile instanceof BaseMultiBlockTileEntity multiBlock) {
                if (multiBlock instanceof FusionReactorTileEntity reactor) {
                    Helpers.addClientTankFromTag(iTooltip, blockAccessor);
                    if (!reactor.isValid || reactor.isDynamic()) {
                        long time = tile.clockTime(512);
                        Helpers.barLiteral(iTooltip, (int) time, 512, Component.literal("Next Reform: ").append(String.valueOf(time)).append(" Ticks").withStyle(ChatFormatting.WHITE), ColorUtils.GRAY);
                    }
                }
            }
        }
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, ServerPlayer serverPlayer, Level level, BlockEntity blockEntity, boolean b) {
        CompoundTag tag = new CompoundTag();
        if (blockEntity instanceof BaseTileEntity tile) {
            if (tile instanceof BaseMultiBlockTileEntity multi) {
                if (multi instanceof FusionReactorTileEntity tunnel) {
                    Helpers.loadTankData(compoundTag, tunnel);
                }
            }
        }
        compoundTag.put("FusionReactorInfo", tag);
    }

    @Override
    public ResourceLocation getUid() {
        return JadeIC2CPluginHandler.EU_READER_INFO;
    }
}

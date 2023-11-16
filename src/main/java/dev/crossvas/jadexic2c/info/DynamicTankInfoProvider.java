package dev.crossvas.jadexic2c.info;

import dev.crossvas.jadexic2c.IHelper;
import dev.crossvas.jadexic2c.JadeIC2CPluginHandler;
import dev.crossvas.jadexic2c.utils.Helpers;
import ic2.core.block.base.tiles.BaseInventoryTileEntity;
import ic2.core.block.base.tiles.BaseLinkingTileEntity;
import ic2.core.block.base.tiles.BaseMultiBlockTileEntity;
import ic2.core.block.storage.tiles.tank.BaseValveTileEntity;
import ic2.core.block.storage.tiles.tank.TankTileEntity;
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

public enum DynamicTankInfoProvider implements IHelper<BlockEntity> {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (!shouldAddInfo(blockAccessor, "DynamicTankInfo")) {
            return;
        }

        if (blockAccessor.getBlockEntity() instanceof BaseInventoryTileEntity tile) {
            if (tile instanceof BaseMultiBlockTileEntity multiBlock) {
                if (multiBlock instanceof TankTileEntity) {
                    Helpers.addClientTankFromTag(iTooltip, blockAccessor);
                }

                if (!multiBlock.isValid || multiBlock.isDynamic()) {
                    long time = tile.clockTime(512);
                    Helpers.barLiteral(iTooltip, (int) time, 512, Component.literal("Next Reform: ").append(String.valueOf(time)).append(" Ticks").withStyle(ChatFormatting.WHITE), ColorUtils.GRAY);
                }
            }
        }

        if (blockAccessor.getBlockEntity() instanceof BaseLinkingTileEntity linking) {
            if (linking instanceof BaseValveTileEntity) {
                BlockEntity master = linking.getMaster();
                if (master instanceof TankTileEntity) {
                    Helpers.addClientTankFromTag(iTooltip, blockAccessor);
                }
            }
        }
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, ServerPlayer serverPlayer, Level level, BlockEntity blockEntity, boolean b) {
        CompoundTag tag = new CompoundTag();
        if (blockEntity instanceof BaseInventoryTileEntity tile) {
            if (tile instanceof BaseMultiBlockTileEntity multi) {
                if (multi instanceof TankTileEntity tank) {
                    Helpers.loadTankData(compoundTag, tank);
                }
            }
        } else if (blockEntity instanceof BaseLinkingTileEntity linking) {
            BlockEntity master = linking.getMaster();
            if (master instanceof TankTileEntity) {
                Helpers.loadTankData(compoundTag, linking);
            }
        }
        compoundTag.put("DynamicTankInfo", tag);
    }

    @Override
    public ResourceLocation getUid() {
        return JadeIC2CPluginHandler.EU_READER_INFO;
    }
}

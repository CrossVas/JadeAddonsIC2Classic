package dev.crossvas.jadexic2c.info.tubes;

import dev.crossvas.jadexic2c.JadeIC2CPluginHandler;
import dev.crossvas.jadexic2c.helpers.IHelper;
import dev.crossvas.jadexic2c.helpers.TextHelper;
import ic2.core.block.base.tiles.BaseTileEntity;
import ic2.core.block.transport.item.tubes.InsertionTubeTileEntity;
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

public class InsertionTubInfoProvider implements IHelper<BlockEntity> {

    public static final InsertionTubInfoProvider INSTANCE = new InsertionTubInfoProvider();

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (!shouldAddInfo(blockAccessor, "InsertionTubeInfo")) {
            return;
        }

        CompoundTag tag = getData(blockAccessor, "InsertionTubeInfo");
        if (blockAccessor.getBlockEntity() instanceof BaseTileEntity baseTileEntity) {
            if (baseTileEntity instanceof InsertionTubeTileEntity) {
                boolean insertExisting = tag.getBoolean("onlyExisting");
                TextHelper.text(iTooltip, Component.translatable("ic2.probe.tube.inserter.existing").withStyle(ChatFormatting.GOLD).append((insertExisting ? ChatFormatting.GREEN : ChatFormatting.RED) + String.valueOf(insertExisting)));
            }
        }
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, ServerPlayer serverPlayer, Level level, BlockEntity blockEntity, boolean b) {
        if (blockEntity instanceof BaseTileEntity baseTile) {
            if (baseTile instanceof InsertionTubeTileEntity insertion) {
                CompoundTag tag = new CompoundTag();
                tag.putBoolean("onlyExisting", insertion.onlyExistingOne);
                compoundTag.put("InsertionTubeInfo", tag);
            }
        }
    }

    @Override
    public ResourceLocation getUid() {
        return JadeIC2CPluginHandler.EU_READER_INFO;
    }
}

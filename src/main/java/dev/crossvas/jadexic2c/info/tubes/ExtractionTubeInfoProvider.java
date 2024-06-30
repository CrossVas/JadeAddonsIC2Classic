package dev.crossvas.jadexic2c.info.tubes;

import dev.crossvas.jadexic2c.JadeIC2CPluginHandler;
import dev.crossvas.jadexic2c.helpers.IHelper;
import dev.crossvas.jadexic2c.helpers.TextHelper;
import ic2.core.block.base.tiles.BaseTileEntity;
import ic2.core.block.transport.item.tubes.ExtractionTubeTileEntity;
import ic2.core.inventory.filter.SpecialFilters;
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

public class ExtractionTubeInfoProvider implements IHelper<BlockEntity> {

    public static final ExtractionTubeInfoProvider INSTANCE = new ExtractionTubeInfoProvider();

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (!shouldAddInfo(blockAccessor, "ExtractionTubeInfo", SpecialFilters.EU_READER)) {
            return;
        }

        CompoundTag tag = getData(blockAccessor, "ExtractionTubeInfo");
        if (blockAccessor.getBlockEntity() instanceof BaseTileEntity tile) {
            if (tile instanceof ExtractionTubeTileEntity) {
                boolean redstoneControl = tag.getBoolean("sensitive");
                boolean comparator = tag.getBoolean("comparator");
                boolean pulse = tag.getBoolean("pulse");
                TextHelper.text(iTooltip, Component.translatable("ic2.probe.tube.redstone.control").withStyle(ChatFormatting.GOLD).append((redstoneControl ? ChatFormatting.GREEN : ChatFormatting.RED) + String.valueOf(redstoneControl)));
                if (redstoneControl) {
                    TextHelper.text(iTooltip, Component.translatable("ic2.probe.tube.redstone.comparator").withStyle(ChatFormatting.LIGHT_PURPLE).append((comparator ? ChatFormatting.GREEN : ChatFormatting.RED) + String.valueOf(comparator)));
                    TextHelper.text(iTooltip, Component.translatable("ic2.probe.tube.redstone.pulse").withStyle(ChatFormatting.LIGHT_PURPLE).append((pulse ? ChatFormatting.GREEN : ChatFormatting.RED) + String.valueOf(pulse)));
                }
            }
        }
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, ServerPlayer serverPlayer, Level level, BlockEntity blockEntity, boolean b) {
        if (blockEntity instanceof BaseTileEntity base) {
            if (base instanceof ExtractionTubeTileEntity tube) {
                CompoundTag tag = new CompoundTag();
                tag.putBoolean("sensitive", tube.sensitive);
                tag.putBoolean("comparator", tube.comparator);
                tag.putBoolean("pulse", tube.pulse);
                compoundTag.put("ExtractionTubeInfo", tag);
            }
        }
    }

    @Override
    public ResourceLocation getUid() {
        return JadeIC2CPluginHandler.TOOLTIP_RENDERER;
    }
}

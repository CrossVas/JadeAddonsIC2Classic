package dev.crossvas.jadexic2c.info.tubes;

import dev.crossvas.jadexic2c.JadeIC2CPluginHandler;
import dev.crossvas.jadexic2c.helpers.IHelper;
import dev.crossvas.jadexic2c.helpers.TextHelper;
import ic2.core.block.transport.item.tubes.TeleportTubeTileEntity;
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

public class TeleportTubeInfoProvider implements IHelper<BlockEntity> {

    public static TeleportTubeInfoProvider INSTANCE = new TeleportTubeInfoProvider();

    public TeleportTubeInfoProvider() {
    }

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (!shouldAddInfo(blockAccessor, "TeleportTubeInfo", SpecialFilters.EU_READER)) {
            return;
        }

        CompoundTag tag = getData(blockAccessor, "TeleportTubeInfo");
        if (blockAccessor.getBlockEntity() instanceof TeleportTubeTileEntity) {
            String freq = tag.getString("freq");
            int state = tag.getInt("state");
            boolean isPrivate = (state & 4) != 0;
            boolean canSend = (state & 1) != 0;
            boolean canReceive = (state & 2) != 0;
            TextHelper.text(iTooltip, Component.translatable("ic2.probe.tube.teleport.private").withStyle(ChatFormatting.GOLD).append((isPrivate ? ChatFormatting.GREEN : ChatFormatting.RED) + String.valueOf(isPrivate)));
            TextHelper.text(iTooltip, Component.translatable("ic2.probe.tube.teleport.send").withStyle(ChatFormatting.GOLD).append((canSend ? ChatFormatting.GREEN : ChatFormatting.RED) + String.valueOf(canSend)));
            TextHelper.text(iTooltip, Component.translatable("ic2.probe.tube.teleport.receive").withStyle(ChatFormatting.GOLD).append((canReceive ? ChatFormatting.GREEN : ChatFormatting.RED) + String.valueOf(canReceive)));
            TextHelper.text(iTooltip, Component.translatable("ic2.tube.teleport.info").withStyle(ChatFormatting.LIGHT_PURPLE).append(ChatFormatting.YELLOW + freq));
        }
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, ServerPlayer serverPlayer, Level level, BlockEntity blockEntity, boolean b) {
        if (blockEntity instanceof TeleportTubeTileEntity teleportTube) {
            CompoundTag tag = new CompoundTag();
            tag.putString("freq", teleportTube.frequency);
            tag.putInt("state", teleportTube.state);
            compoundTag.put("TeleportTubeInfo", tag);
        }
    }

    @Override
    public ResourceLocation getUid() {
        return JadeIC2CPluginHandler.TOOLTIP_RENDERER;
    }
}

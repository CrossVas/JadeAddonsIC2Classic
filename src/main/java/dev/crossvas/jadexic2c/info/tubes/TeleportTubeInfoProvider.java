package dev.crossvas.jadexic2c.info.tubes;

import dev.crossvas.jadexic2c.JadeIC2CPluginHandler;
import dev.crossvas.jadexic2c.helpers.IHelper;
import dev.crossvas.jadexic2c.helpers.PluginHelper;
import dev.crossvas.jadexic2c.helpers.TextHelper;
import ic2.core.block.transport.item.tubes.TeleportTubeTileEntity;
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

    public TeleportTubeInfoProvider() {}

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (!shouldAddInfo(blockAccessor, "TeleportTubeInfo")) {
            return;
        }

        CompoundTag tag = getData(blockAccessor, "TeleportTubeInfo");
        if (blockAccessor.getBlockEntity() instanceof TeleportTubeTileEntity) {
            String freq = tag.getString("freq");
            PluginHelper.spacerY(iTooltip, 3);
            TextHelper.text(iTooltip, Component.translatable( "ic2.tube.teleport.info").withStyle(ChatFormatting.GOLD).append(ChatFormatting.YELLOW + freq));
        }
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, ServerPlayer serverPlayer, Level level, BlockEntity blockEntity, boolean b) {
        if (blockEntity instanceof TeleportTubeTileEntity teleportTube) {
            CompoundTag tag = new CompoundTag();
            tag.putString("freq", teleportTube.frequency);
            compoundTag.put("TeleportTubeInfo", tag);
        }
    }

    @Override
    public ResourceLocation getUid() {
        return JadeIC2CPluginHandler.EU_READER_INFO;
    }
}

package dev.crossvas.jadexic2c.info.tubes;

import dev.crossvas.jadexic2c.JadeIC2CPluginHandler;
import dev.crossvas.jadexic2c.helpers.IHelper;
import dev.crossvas.jadexic2c.helpers.PluginHelper;
import dev.crossvas.jadexic2c.helpers.TextHelper;
import ic2.core.block.transport.item.tubes.PickupTubeTileEntity;
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

public class PickupTubeInfoProvider implements IHelper<BlockEntity> {

    public static PickupTubeInfoProvider INSTANCE = new PickupTubeInfoProvider();

    public PickupTubeInfoProvider() {
    }

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (!shouldAddInfo(blockAccessor, "PickupTubeInfo")) {
            return;
        }

        CompoundTag tag = getData(blockAccessor, "PickupTubeInfo");
        if (blockAccessor.getBlockEntity() instanceof PickupTubeTileEntity) {
            boolean largeRadius = tag.getBoolean("largeRadius");
            PluginHelper.spacerY(iTooltip, 3);
            TextHelper.text(iTooltip, Component.translatable("ic2.tube.pickup.info").withStyle(ChatFormatting.GOLD).append((largeRadius ? ChatFormatting.GREEN : ChatFormatting.RED) + String.valueOf(largeRadius)));
        }
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, ServerPlayer serverPlayer, Level level, BlockEntity blockEntity, boolean b) {
        if (blockEntity instanceof PickupTubeTileEntity pickup) {
            CompoundTag tag = new CompoundTag();
            tag.putBoolean("largeRadius", pickup.largeRadius);
            compoundTag.put("PickupTubeInfo", tag);
        }
    }

    @Override
    public ResourceLocation getUid() {
        return JadeIC2CPluginHandler.EU_READER_INFO;
    }
}

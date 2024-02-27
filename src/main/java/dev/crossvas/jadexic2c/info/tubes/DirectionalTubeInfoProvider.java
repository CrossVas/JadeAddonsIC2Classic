package dev.crossvas.jadexic2c.info.tubes;

import dev.crossvas.jadexic2c.JadeIC2CPluginHandler;
import dev.crossvas.jadexic2c.helpers.IHelper;
import dev.crossvas.jadexic2c.helpers.PluginHelper;
import dev.crossvas.jadexic2c.helpers.TextHelper;
import ic2.core.block.transport.item.tubes.DirectionalTubeTileEntity;
import ic2.core.utils.helpers.SanityHelper;
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

public class DirectionalTubeInfoProvider implements IHelper<BlockEntity> {

    public static DirectionalTubeInfoProvider INSTANCE = new DirectionalTubeInfoProvider();

    public DirectionalTubeInfoProvider() {
    }

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (!shouldAddInfo(blockAccessor, "DirectionalTubeInfo")) {
            return;
        }

        CompoundTag tag = getData(blockAccessor, "DirectionalTubeInfo");
        if (blockAccessor.getBlockEntity() instanceof DirectionalTubeTileEntity) {
            PluginHelper.spacerY(iTooltip, 3);
            String directionName = tag.getString("direction");
            int direction3DDataValue = tag.getInt("3DDataValue");
            String facingName = SanityHelper.toPascalCase(directionName);
            TextHelper.text(iTooltip, Component.translatable("ic2.tube.directional.info").withStyle(ChatFormatting.GOLD).append(PluginHelper.getColor(direction3DDataValue) + facingName));
            boolean outputBlocked = tag.getBoolean("blocked");
            TextHelper.text(iTooltip, Component.translatable("ic2.probe.tube.blocked").withStyle(ChatFormatting.DARK_AQUA).append((outputBlocked ? ChatFormatting.GREEN : ChatFormatting.RED) + String.valueOf(outputBlocked)));
        }
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, ServerPlayer serverPlayer, Level level, BlockEntity blockEntity, boolean b) {
        if (blockEntity instanceof DirectionalTubeTileEntity directional) {
            CompoundTag tag = new CompoundTag();
            tag.putString("direction", directional.getFacing().toString());
            tag.putInt("3DDataValue", directional.getFacing().get3DDataValue());
            tag.putBoolean("blocked", directional.isBlocked());
            compoundTag.put("DirectionalTubeInfo", tag);
        }
    }

    @Override
    public ResourceLocation getUid() {
        return JadeIC2CPluginHandler.EU_READER_INFO;
    }
}

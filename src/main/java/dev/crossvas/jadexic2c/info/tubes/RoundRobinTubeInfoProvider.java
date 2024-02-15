package dev.crossvas.jadexic2c.info.tubes;

import dev.crossvas.jadexic2c.JadeIC2CPluginHandler;
import dev.crossvas.jadexic2c.helpers.IHelper;
import dev.crossvas.jadexic2c.helpers.PluginHelper;
import dev.crossvas.jadexic2c.helpers.TextHelper;
import ic2.core.block.transport.item.tubes.RoundRobinTubeTileEntity;
import ic2.core.utils.helpers.SanityHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public class RoundRobinTubeInfoProvider implements IHelper<BlockEntity> {

    public static RoundRobinTubeInfoProvider INSTANCE = new RoundRobinTubeInfoProvider();

    public RoundRobinTubeInfoProvider() {}

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (!shouldAddInfo(blockAccessor, "RoundRobinTubeInfo")) {
            return;
        }

        CompoundTag tag = getData(blockAccessor, "RoundRobinTubeInfo");
        if (blockAccessor.getBlockEntity() instanceof RoundRobinTubeTileEntity) {
            PluginHelper.spacerY(iTooltip, 3);
            TextHelper.text(iTooltip, Component.translatable("ic2.tube.round_robin.info").withStyle(ChatFormatting.GOLD));
            int[] size = tag.getIntArray("size");
            for (int i = 0; i < size.length; i++) {
                int count = size[i];
                if (count > 0) {
                    Direction side = Direction.from3DDataValue(i);
                    TextHelper.text(iTooltip, Component.literal( SanityHelper.toPascalCase(side.getName()) + ": " + count).withStyle(PluginHelper.getColor(i)));
                }
            }
        }
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, ServerPlayer serverPlayer, Level level, BlockEntity blockEntity, boolean b) {
        if (blockEntity instanceof RoundRobinTubeTileEntity roundRobin) {
            CompoundTag tag = new CompoundTag();
            tag.putIntArray("size", roundRobin.cap);
            compoundTag.put("RoundRobinTubeInfo", tag);
        }
    }

    @Override
    public ResourceLocation getUid() {
        return JadeIC2CPluginHandler.EU_READER_INFO;
    }
}

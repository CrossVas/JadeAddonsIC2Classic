package dev.crossvas.jadexic2c.info;

import dev.crossvas.jadexic2c.IHelper;
import dev.crossvas.jadexic2c.JadeIC2CPluginHandler;
import dev.crossvas.jadexic2c.utils.Helpers;
import ic2.api.energy.EnergyNet;
import ic2.core.block.machines.tiles.hv.VillagerOMatTileEntity;
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

public enum VillagerOMatInfoProvider implements IHelper {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (!shouldAddInfo(blockAccessor, "VillagerOMatInfo")) {
            return;
        }

        CompoundTag tag = getData(blockAccessor, "VillagerOMatInfo");
        if (blockAccessor.getBlockEntity() instanceof VillagerOMatTileEntity oMat) {
            Helpers.text(iTooltip,"ic2.probe.eu.tier.name", EnergyNet.INSTANCE.getDisplayTier(oMat.getTier()));
            Helpers.text(iTooltip,"ic2.probe.eu.max_in.name", oMat.getMaxInput());
            Helpers.text(iTooltip,"ic2.probe.villager_o_mat.usage", tag.getInt("activeTrades") * 6000);

            Helpers.barLiteral(iTooltip, (int) (1200 - oMat.clockTime(1200)), 1200, Component.translatable("ic2.probe.villager_o_mat.next", oMat.clockTime(1200)).withStyle(ChatFormatting.WHITE), ColorUtils.BLUE);
        }


    }

    @Override
    public void appendServerData(CompoundTag compoundTag, ServerPlayer serverPlayer, Level level, BlockEntity blockEntity, boolean b) {
        if (blockEntity instanceof VillagerOMatTileEntity oMat) {
            CompoundTag tag = new CompoundTag();
            tag.putInt("activeTrades", oMat.trades.getActiveTrades());
            compoundTag.put("VillagerOMatInfo", tag);
        }

    }

    @Override
    public ResourceLocation getUid() {
        return JadeIC2CPluginHandler.EU_READER_INFO;
    }
}

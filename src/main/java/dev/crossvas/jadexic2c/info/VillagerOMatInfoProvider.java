package dev.crossvas.jadexic2c.info;

import dev.crossvas.jadexic2c.JadeIC2CPluginHandler;
import dev.crossvas.jadexic2c.helpers.BarHelper;
import dev.crossvas.jadexic2c.helpers.IHelper;
import dev.crossvas.jadexic2c.helpers.TextHelper;
import ic2.api.energy.EnergyNet;
import ic2.core.block.machines.tiles.hv.VillagerOMatTileEntity;
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

public enum VillagerOMatInfoProvider implements IHelper<BlockEntity> {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (!shouldAddInfo(blockAccessor, "VillagerOMatInfo", SpecialFilters.EU_READER)) {
            return;
        }

        CompoundTag tag = getData(blockAccessor, "VillagerOMatInfo");
        if (blockAccessor.getBlockEntity() instanceof VillagerOMatTileEntity oMat) {
            TextHelper.text(iTooltip, "ic2.probe.eu.tier.name", EnergyNet.INSTANCE.getDisplayTier(oMat.getTier()));
            TextHelper.text(iTooltip, "ic2.probe.eu.max_in.name", oMat.getMaxInput());
            TextHelper.text(iTooltip, "ic2.probe.villager_o_mat.usage", tag.getInt("activeTrades") * 6000);

            BarHelper.bar(iTooltip, (int) (1200 - oMat.clockTime(1200)), 1200, Component.translatable("ic2.probe.villager_o_mat.next", oMat.clockTime(1200)).withStyle(ChatFormatting.WHITE), -16733185);
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
        return JadeIC2CPluginHandler.TOOLTIP_RENDERER;
    }
}

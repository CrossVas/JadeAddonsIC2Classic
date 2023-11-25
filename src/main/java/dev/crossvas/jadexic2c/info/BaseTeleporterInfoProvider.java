package dev.crossvas.jadexic2c.info;

import dev.crossvas.jadexic2c.IHelper;
import dev.crossvas.jadexic2c.JadeIC2CPluginHandler;
import dev.crossvas.jadexic2c.utils.Helpers;
import ic2.api.energy.EnergyNet;
import ic2.core.block.base.tiles.BaseTileEntity;
import ic2.core.block.machines.tiles.mv.BaseTeleporterTileEntity;
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

import java.util.Set;

public enum BaseTeleporterInfoProvider implements IHelper<BlockEntity> {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (!shouldAddInfo(blockAccessor, "BaseTeleporterInfo")) {
            return;
        }

        CompoundTag tag = getData(blockAccessor, "BaseTeleporterInfo");
        if (blockAccessor.getBlockEntity() instanceof BaseTileEntity tile) {
            if (tile instanceof BaseTeleporterTileEntity tp) {
                Helpers.text(iTooltip, "ic2.probe.eu.tier.name", EnergyNet.INSTANCE.getDisplayTier(tp.getTier()));
                Helpers.text(iTooltip, "ic2.probe.eu.max_in.name", EnergyNet.INSTANCE.getPowerFromTier(tp.getSinkTier()));
                Set<BaseTeleporterTileEntity.LocalTarget> targets = tp.getTargets();
                String name = tag.getString("name");
                String networkID = tag.getString("networkID");
                if (!targets.isEmpty()) {
                    Helpers.text(iTooltip, Component.translatable("gui.ic2.base_teleporter.name").append(": ").append(name).withStyle(ChatFormatting.WHITE));
                    Helpers.text(iTooltip, Component.translatable("gui.ic2.base_teleporter.network").append(": ").append(networkID).withStyle(ChatFormatting.WHITE));
                    Helpers.space_y(iTooltip, 3);
                    Helpers.text(iTooltip, Component.translatable("ic2.probe.base_teleporter.connections").withStyle(ChatFormatting.GOLD));
                    for (BaseTeleporterTileEntity.LocalTarget target : targets) {
                        if (!target.getPos().equals(tp.getPosition())) {
                            Helpers.text(iTooltip, Component.literal(" - " + target.getName()).withStyle(ChatFormatting.WHITE));
                        }
                    }
                }
            }
        }
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, ServerPlayer serverPlayer, Level level, BlockEntity blockEntity, boolean b) {
        CompoundTag tag = new CompoundTag();
        if (blockEntity instanceof BaseTileEntity tile) {
            if (tile instanceof BaseTeleporterTileEntity tp) {
                tag.putString("name", tp.name);
                tag.putString("networkID", tp.networkID);
            }
        }
        compoundTag.put("BaseTeleporterInfo", tag);
    }

    @Override
    public ResourceLocation getUid() {
        return JadeIC2CPluginHandler.EU_READER_INFO;
    }
}

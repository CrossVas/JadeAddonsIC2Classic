package dev.crossvas.jadexic2c.info;

import dev.crossvas.jadexic2c.IHelper;
import dev.crossvas.jadexic2c.JadeIC2CPluginHandler;
import dev.crossvas.jadexic2c.utils.Helpers;
import ic2.api.tiles.teleporter.TeleporterTarget;
import ic2.core.IC2;
import ic2.core.block.base.tiles.BaseTileEntity;
import ic2.core.block.machines.tiles.hv.TeleporterTileEntity;
import ic2.core.utils.helpers.Formatters;
import ic2.core.utils.helpers.SanityHelper;
import ic2.core.utils.helpers.TeleportUtil;
import ic2.probeplugin.base.ProbePluginHelper;
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

public enum TeleporterInfoProvider implements IHelper {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (!shouldAddInfo(blockAccessor, "TeleporterInfo")) {
            return;
        }

        CompoundTag tag = getData(blockAccessor, "TeleporterInfo");

        if (blockAccessor.getBlockEntity() instanceof BaseTileEntity tile) {
            if (tile instanceof TeleporterTileEntity teleport) {
                Helpers.text(iTooltip, Component.translatable("ic2.probe.teleporter.type", Component.translatable("ic2.probe.teleporter.type." + teleport.getProbeSendType().name().toLowerCase())).withStyle(ChatFormatting.WHITE));
                TeleporterTarget target = TeleporterTarget.read(tag.getCompound("TeleportTarget"));
                int baseCost = tag.getInt("cost");
                long availableEnergy = tag.getLong("availableEnergy");
                if (target == null) {
                    Helpers.text(iTooltip,"ic2.probe.teleporter.no_target");
                } else if (!tag.getBoolean("isMathching")) {
                    Helpers.text(iTooltip,"ic2.probe.teleporter.invalid_target");
                } else {
                    Helpers.text(iTooltip,"ic2.probe.teleporter.target", SanityHelper.toPascalCase(target.getDimension().location().getPath()), target.getTargetPosition().getX(), target.getTargetPosition().getY(), target.getTargetPosition().getZ());
                }

                if (baseCost > 0) {
                    switch (teleport.getProbeSendType()) {
                        case ENTITY:
                            Helpers.text(iTooltip,"ic2.probe.teleporter.cost", Formatters.EU_FORMAT.format((long)(TeleportUtil.getWeightOfEntity(blockAccessor.getPlayer(), IC2.CONFIG.teleporterKeepItems.get()) * baseCost * 5)));
                            break;
                        case ENERGY:
                            Helpers.text(iTooltip,"ic2.probe.teleporter.cost", Formatters.EU_FORMAT.format((long)baseCost));
                            break;
                        case FLUID:
                            Helpers.text(iTooltip,"ic2.probe.teleporter.cost", Formatters.EU_FORMAT.format((long)baseCost));
                            Helpers.text(iTooltip,"ic2.probe.teleporter.capacity", Component.translatable("ic2.probe.teleporter.capacity.fluid", ProbePluginHelper.formatNumber((double)(availableEnergy / (long)baseCost * 10L), 6)));
                            break;
                        case ITEM:
                            Helpers.text(iTooltip,"ic2.probe.teleporter.cost", Formatters.EU_FORMAT.format((long)baseCost));
                            Helpers.text(iTooltip,"ic2.probe.teleporter.capacity", Component.translatable("ic2.probe.teleporter.capacity.item", ProbePluginHelper.formatNumber((double)(availableEnergy / (long)baseCost / 100L * 64L), 6)));
                            break;
                        case SPAWNER:
                            Helpers.text(iTooltip,"ic2.probe.teleporter.cost", Formatters.EU_FORMAT.format((long)(baseCost * 25000)));
                    }
                }
            }
        }
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, ServerPlayer serverPlayer, Level level, BlockEntity blockEntity, boolean b) {
        if (blockEntity instanceof BaseTileEntity tile) {
            if (tile instanceof TeleporterTileEntity teleport) {
                CompoundTag tag = new CompoundTag();
                tag.put("TeleportTarget", teleport.target.write(new CompoundTag()));
                tag.putBoolean("isMathching", teleport.hasMatchingType());
                tag.putInt("cost", teleport.getBaseCost());
                tag.putLong("availableEnergy", teleport.getAvailableEnergy());
                compoundTag.put("TeleporterInfo", tag);
            }
        }
    }

    @Override
    public ResourceLocation getUid() {
        return JadeIC2CPluginHandler.EU_READER_INFO;
    }
}

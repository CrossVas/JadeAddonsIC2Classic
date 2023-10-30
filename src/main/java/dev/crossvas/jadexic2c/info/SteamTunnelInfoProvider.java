package dev.crossvas.jadexic2c.info;

import dev.crossvas.jadexic2c.IHelper;
import dev.crossvas.jadexic2c.JadeIC2CPluginHandler;
import dev.crossvas.jadexic2c.utils.Formatter;
import dev.crossvas.jadexic2c.utils.Helpers;
import ic2.api.energy.EnergyNet;
import ic2.core.block.base.tiles.BaseLinkingTileEntity;
import ic2.core.block.base.tiles.BaseMultiBlockTileEntity;
import ic2.core.block.base.tiles.BaseTileEntity;
import ic2.core.block.generators.tiles.SteamTunnelTileEntity;
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

public enum SteamTunnelInfoProvider implements IHelper {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (!shouldAddInfo(blockAccessor, "SteamTunnelInfo")) {
            return;
        }

        CompoundTag tag = getData(blockAccessor, "SteamTunnelInfo");
        if (blockAccessor.getBlockEntity() instanceof BaseTileEntity tile) {
            if (tile instanceof BaseMultiBlockTileEntity multiBlock) {
                if (multiBlock instanceof SteamTunnelTileEntity tunnel) {
                    addInfo(tunnel, iTooltip, tag);
                    Helpers.addClientTankFromTag(iTooltip, blockAccessor);
                }

                if (!multiBlock.isValid || multiBlock.isDynamic()) {
                    long time = tile.clockTime(512);
                    Helpers.barLiteral(iTooltip, (int) time, 512, Component.literal("Next Reform: ").append(String.valueOf(time)).append(" Ticks").withStyle(ChatFormatting.WHITE), ColorUtils.GRAY);
                }
            }

            if (tile instanceof BaseLinkingTileEntity linking) {
                CompoundTag linkingLag = tag.getCompound("LinkingBlockInfo");
                BlockEntity master = linking.getMaster();
                if (master instanceof SteamTunnelTileEntity tunnel) {
                    addInfo(tunnel, iTooltip, linkingLag);
                    Helpers.addClientTankFromTag(iTooltip, blockAccessor);
                }
            }
        }
    }

    public void addInfo(SteamTunnelTileEntity tunnel, ITooltip iTooltip, CompoundTag tag) {
        float energyProduction = tag.getFloat("energyProduction");
        Helpers.text(iTooltip, "ic2.probe.eu.tier.name", EnergyNet.INSTANCE.getDisplayTier(tunnel.getSourceTier()));
        Helpers.text(iTooltip, "ic2.probe.eu.output.current.name", Formatter.formatNumber((double)energyProduction, 5));
        Helpers.text(iTooltip, "ic2.probe.eu.output.max.name", tunnel.getMaxEnergyOutput());
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, ServerPlayer serverPlayer, Level level, BlockEntity blockEntity, boolean b) {
        CompoundTag tag = new CompoundTag();
        if (blockEntity instanceof BaseTileEntity tile) {
            if (tile instanceof BaseMultiBlockTileEntity multi) {
                if (multi instanceof SteamTunnelTileEntity tunnel) {
                    tag.putFloat("energyProduction", tunnel.getEUProduction());
                    Helpers.loadTankData(compoundTag, tunnel);
                }

            } else if (tile instanceof BaseLinkingTileEntity linking) {
                BlockEntity master = linking.getMaster();
                CompoundTag linkingTag = new CompoundTag();
                if (master instanceof SteamTunnelTileEntity tunnel) {
                    linkingTag.putFloat("energyProduction", tunnel.getEUProduction());
                }
                Helpers.loadTankData(compoundTag, linking);
                tag.put("LinkingBlockInfo", linkingTag);
            }
        }
        compoundTag.put("SteamTunnelInfo", tag);
    }

    @Override
    public ResourceLocation getUid() {
        return JadeIC2CPluginHandler.EU_READER_INFO;
    }
}

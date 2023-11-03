package dev.crossvas.jadexic2c.info;

import dev.crossvas.jadexic2c.JadeIC2CPluginHandler;
import dev.crossvas.jadexic2c.utils.ColorMix;
import dev.crossvas.jadexic2c.utils.Formatter;
import dev.crossvas.jadexic2c.utils.Helpers;
import ic2.api.reactor.IReactor;
import ic2.api.reactor.IReactorChamber;
import ic2.api.reactor.ISteamReactor;
import ic2.core.block.generators.tiles.ElectricNuclearReactorTileEntity;
import ic2.core.inventory.filter.SpecialFilters;
import ic2.core.platform.player.PlayerHandler;
import ic2.core.utils.helpers.Formatters;
import ic2.core.utils.helpers.StackUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum NuclearReactorInfoProvider implements IBlockComponentProvider, IServerDataProvider<BlockEntity> {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (!blockAccessor.getServerData().contains("NuclearReactorInfo")) {
            return;
        }

        if (blockAccessor.getBlockEntity() instanceof IReactor tile) {
            addToTooltip(iTooltip, (BlockEntity) tile, blockAccessor);
        }
        if (blockAccessor.getBlockEntity() instanceof IReactorChamber tile) {
            IReactor tileReactor = tile.getReactor();
            BlockEntity reactor = (BlockEntity) tileReactor;
            addToTooltip(iTooltip, reactor, blockAccessor);
        }
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, ServerPlayer serverPlayer, Level level, BlockEntity blockEntity, boolean b) {
        CompoundTag tag = new CompoundTag();
        if (blockEntity instanceof IReactor tile) {
            if (tile instanceof ISteamReactor steamReactor) {
                Helpers.loadTankData(compoundTag, (BlockEntity) steamReactor);
            }
        } else if (blockEntity instanceof IReactorChamber chamber) {
            BlockEntity tile = (BlockEntity) chamber.getReactor();
            if (tile instanceof ISteamReactor steamReactor) {
                Helpers.loadTankData(compoundTag, (BlockEntity) steamReactor);
            }
        }
        compoundTag.put("NuclearReactorInfo", tag);
    }

    @Override
    public ResourceLocation getUid() {
        return JadeIC2CPluginHandler.EU_READER_INFO;
    }

    public static void addToTooltip(ITooltip tooltip, BlockEntity tile, BlockAccessor blockAccessor) {
        if (StackUtil.hasHotbarItems(blockAccessor.getPlayer(), SpecialFilters.EU_READER)) {
            if (tile instanceof IReactor reactorTile) {
                if (tile instanceof ElectricNuclearReactorTileEntity reactor) {
                    Helpers.text(tooltip, "ic2.probe.eu.output.current.name", Formatter.formatNumber((double) reactor.getProvidedEnergy(), 5));
                    Helpers.text(tooltip, "ic2.probe.reactor.breeding.name", reactor.getHeat() / 3000 + 1);
                } else if (tile instanceof ISteamReactor steamReactor) {
                    Helpers.text(tooltip, "ic2.probe.steam.output.name", Formatter.THERMAL_GEN.format(steamReactor.getEnergyOutput() * 3.200000047683716));
                    Helpers.text(tooltip, "ic2.probe.water.consumption.name", Formatter.THERMAL_GEN.format(steamReactor.getEnergyOutput() / 50.0));
                    Helpers.text(tooltip, "ic2.probe.pump.pressure", 100);
                    Helpers.text(tooltip, "ic2.probe.pump.amount", Formatters.EU_FORMAT.format(20000L));
                    Helpers.addClientTankFromTag(tooltip, blockAccessor);
                }

                if (StackUtil.hasHotbarItems(blockAccessor.getPlayer(), SpecialFilters.THERMOMETER)) {
                    Helpers.barLiteral(tooltip, reactorTile.getHeat(), reactorTile.getMaxHeat(), Component.translatable("ic2.probe.reactor.heat.name",
                            reactorTile.getHeat(), Formatters.EU_READER_FORMAT.format((double) reactorTile.getMaxHeat() / 1000.0)).append("k").withStyle(ChatFormatting.WHITE), getReactorColor(reactorTile.getHeat(), reactorTile.getMaxHeat()));
                }
            }
        }
    }

    public static ColorMix getReactorColor(int current, int max) {
        float progress = (float) current / max;
        if ((double)progress < 0.25) {
            return ColorMix.GREEN;
        } else if ((double)progress < 0.5) {
            return ColorMix.YELLOW;
        } else {
            return (double) progress < 0.75 ? ColorMix.ORANGE : ColorMix.RED;
        }
    }
}

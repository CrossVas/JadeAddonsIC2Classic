package dev.crossvas.jadexic2c.info;

import dev.crossvas.jadexic2c.JadeIC2CPluginHandler;
import dev.crossvas.jadexic2c.helpers.BarHelper;
import dev.crossvas.jadexic2c.helpers.IHelper;
import dev.crossvas.jadexic2c.helpers.TankHelper;
import ic2.core.block.base.tiles.BaseTileEntity;
import ic2.core.block.generators.tiles.FuelBoilerTileEntity;
import ic2.core.platform.player.PlayerHandler;
import ic2.core.utils.helpers.Formatters;
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

public enum FuelBoilerInfoProvider implements IHelper<BlockEntity> {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (!shouldAddInfo(blockAccessor, "FuelBoilerInfo")) {
            return;
        }

        CompoundTag tag = getData(blockAccessor, "FuelBoilerInfo");
        if (blockAccessor.getBlockEntity() instanceof BaseTileEntity tile) {
            if (tile instanceof FuelBoilerTileEntity boiler) {
                addInfo(boiler, blockAccessor, iTooltip, tag);
            }
        }
    }

    public void addInfo(FuelBoilerTileEntity boiler, BlockAccessor accessor, ITooltip iTooltip, CompoundTag tag) {
        int fuel = tag.getInt("fuel");
        int maxFuel = tag.getInt("maxFuel");
        int heat = tag.getInt("heat");
        BarHelper.bar(iTooltip, fuel, maxFuel, Component.translatable("ic2.probe.fuel.storage.name").append(String.valueOf(fuel)), ColorUtils.DARK_GRAY);

        if (PlayerHandler.getHandler(accessor.getPlayer()).hasThermometer()) {
            BarHelper.bar(iTooltip, heat, boiler.getMaxHeat(), Component.translatable("ic2.probe.reactor.heat.name",
                    heat / 30, Formatters.EU_READER_FORMAT.format((double) boiler.getMaxHeat() / 30)).withStyle(ChatFormatting.WHITE), ColorUtils.GREEN);
        }
        TankHelper.addClientTankFromTag(iTooltip, accessor);
        if (!boiler.isValid) {
            long time = boiler.clockTime(512);
            BarHelper.bar(iTooltip, (int) time, 512, Component.literal("Next Reform: ").append(String.valueOf(512 - time)).append(" Ticks").withStyle(ChatFormatting.WHITE), ColorUtils.GRAY);
        }
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, ServerPlayer serverPlayer, Level level, BlockEntity blockEntity, boolean b) {
        if (blockEntity instanceof FuelBoilerTileEntity boiler) {
            CompoundTag tag = new CompoundTag();
            tag.putInt("fuel", boiler.getFuel());
            tag.putInt("maxFuel", boiler.getMaxFuel());
            tag.putInt("heat", boiler.getHeat());
            TankHelper.loadTankData(compoundTag, boiler);
            compoundTag.put("FuelBoilerInfo", tag);
        }
    }

    @Override
    public ResourceLocation getUid() {
        return JadeIC2CPluginHandler.EU_READER_INFO;
    }
}

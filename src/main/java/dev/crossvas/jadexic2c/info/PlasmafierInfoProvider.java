package dev.crossvas.jadexic2c.info;

import dev.crossvas.jadexic2c.JadePluginHandler;
import dev.crossvas.jadexic2c.helpers.BarHelper;
import dev.crossvas.jadexic2c.base.interfaces.IHelper;
import dev.crossvas.jadexic2c.helpers.TextHelper;
import ic2.api.energy.EnergyNet;
import ic2.core.block.base.tiles.BaseElectricTileEntity;
import ic2.core.block.machines.tiles.ev.PlasmafierTileEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum PlasmafierInfoProvider implements IHelper<BlockEntity> {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (!shouldAddInfo(blockAccessor, "PlasmaInfo")) {
            return;
        }

        CompoundTag tag = getData(blockAccessor, "PlasmaInfo");
        if (blockAccessor.getBlockEntity() instanceof PlasmafierTileEntity tile) {
            TextHelper.text(iTooltip, "ic2.probe.eu.tier.name", EnergyNet.INSTANCE.getDisplayTier(tile.getTier()));
            TextHelper.text(iTooltip, "ic2.probe.eu.max_in.name", tile.getMaxInput());
            TextHelper.text(iTooltip, "ic2.probe.eu.usage.name", 10240);

            int plasma = tag.getInt("plasma");
            int maxPlasma = tag.getInt("maxPlasma");
            int uuMatter = tag.getInt("uuMatter");

            if (plasma > 0) {
                BarHelper.bar(iTooltip, plasma, maxPlasma, Component.translatable("ic2.probe.plasma.name", plasma, maxPlasma), -5829955);
            }

            if (uuMatter > 0) {
                BarHelper.bar(iTooltip, uuMatter, 150, Component.translatable("ic2.probe.matter.name", uuMatter, 150), -5829955);
            }

        }
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, ServerPlayer serverPlayer, Level level, BlockEntity blockEntity, boolean b) {
        if (blockEntity instanceof BaseElectricTileEntity tile) {
            if (tile instanceof PlasmafierTileEntity plasma) {
                CompoundTag tag = new CompoundTag();
                tag.putInt("plasma", plasma.getPumpProgress());
                tag.putInt("maxPlasma", plasma.getPumpMaxProgress());
                tag.putInt("uuMatter", plasma.uuMatter);
                compoundTag.put("PlasmaInfo", tag);
            }
        }
    }

    @Override
    public ResourceLocation getUid() {
        return JadePluginHandler.EU_READER_INFO;
    }
}

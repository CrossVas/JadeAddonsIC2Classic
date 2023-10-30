package dev.crossvas.jadexic2c.info;

import dev.crossvas.jadexic2c.IHelper;
import dev.crossvas.jadexic2c.JadeIC2CPluginHandler;
import dev.crossvas.jadexic2c.utils.ColorMix;
import dev.crossvas.jadexic2c.utils.Helpers;
import ic2.api.energy.EnergyNet;
import ic2.core.block.base.tiles.BaseElectricTileEntity;
import ic2.core.block.machines.tiles.ev.PlasmafierTileEntity;
import ic2.core.inventory.filter.IFilter;
import ic2.core.inventory.filter.SpecialFilters;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.IElementHelper;

public enum PlasmafierInfoProvider implements IHelper {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (!canHandle(blockAccessor.getPlayer())) {
            return;
        }
        if (!blockAccessor.getServerData().contains("PlasmaInfo")) {
            return;
        }
        CompoundTag tag = blockAccessor.getServerData().getCompound("PlasmaInfo");
        if (blockAccessor.getBlockEntity() instanceof PlasmafierTileEntity tile) {
            Helpers.text(iTooltip, "ic2.probe.eu.tier.name", EnergyNet.INSTANCE.getDisplayTier(tile.getTier()));
            Helpers.text(iTooltip, "ic2.probe.eu.max_in.name", tile.getMaxInput());
            Helpers.text(iTooltip, "ic2.probe.eu.usage.name", 10240);

            int plasma = tag.getInt("plasma");
            int maxPlasma = tag.getInt("maxPlasma");
            int uuMatter = tag.getInt("uuMatter");

            if (plasma > 0) {
                Helpers.bar(iTooltip, plasma, maxPlasma, "ic2.probe.plasma.name", ColorMix.PURPLE);
            }

            if (uuMatter > 0) {
                Helpers.bar(iTooltip, uuMatter, 150, "ic2.probe.matter.name", ColorMix.PURPLE);
            }

        }
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, ServerPlayer serverPlayer, Level level, BlockEntity blockEntity, boolean b) {
        CompoundTag tag = new CompoundTag();
        if (blockEntity instanceof BaseElectricTileEntity tile) {
            if (tile instanceof PlasmafierTileEntity plasma) {
                tag.putInt("plasma", plasma.getPumpProgress());
                tag.putInt("maxPlasma", plasma.getPumpMaxProgress());
                tag.putInt("uuMatter", plasma.uuMatter);

            }
        }
        compoundTag.put("PlasmaInfo", tag);
    }

    @Override
    public ResourceLocation getUid() {
        return JadeIC2CPluginHandler.EU_READER_INFO;
    }
}

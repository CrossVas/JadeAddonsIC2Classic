package dev.crossvas.jadexic2c.info;

import dev.crossvas.jadexic2c.IHelper;
import dev.crossvas.jadexic2c.JadeIC2CPluginHandler;
import dev.crossvas.jadexic2c.utils.ColorMix;
import dev.crossvas.jadexic2c.utils.Helpers;
import ic2.api.energy.EnergyNet;
import ic2.core.block.base.tiles.BaseElectricTileEntity;
import ic2.core.block.machines.recipes.misc.EnrichRecipe;
import ic2.core.block.machines.tiles.hv.UraniumEnchricherTileEntity;
import ic2.core.inventory.filter.IFilter;
import ic2.core.inventory.filter.SpecialFilters;
import ic2.core.utils.helpers.SanityHelper;
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

public enum UraniumEnricherInfoProvider implements IHelper {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (!canHandle(blockAccessor.getPlayer())) {
            return;
        }
        if (!blockAccessor.getServerData().contains("EnricherInfo")) {
            return;
        }
        CompoundTag tag = blockAccessor.getServerData().getCompound("EnricherInfo");

        if (blockAccessor.getBlockEntity() instanceof UraniumEnchricherTileEntity tile) {
            int mainProgress = tag.getInt("mainProgress");
            int secondaryProgress = tag.getInt("secondaryProgress");
            int storedPoints = tag.getInt("storedPoints");
            ResourceLocation storedType = new ResourceLocation(tag.getString("storedType"));

            Helpers.text(iTooltip, "ic2.probe.eu.tier.name", EnergyNet.INSTANCE.getDisplayTier(tile.getTier()));
            Helpers.text(iTooltip, "ic2.probe.eu.max_in.name", tile.getMaxInput());
            EnrichRecipe currentRecipe = null;
            if (storedType != null && storedPoints > 0) {
                currentRecipe = tile.getRecipeList().getRecipe(storedType);
            }
            Helpers.text(iTooltip, "ic2.probe.eu.usage.name", currentRecipe != null ? currentRecipe.getEnergyCost() + 100 : 100);

            if (mainProgress > 0) {
                Helpers.barLiteral(iTooltip, mainProgress / 20, 1000 / 20,
                        Component.translatable("ic2.probe.progress.full.name", mainProgress / 20, 1000 / 20).append(" s").withStyle(ChatFormatting.WHITE), ColorMix.BLUE);
            }
            if (secondaryProgress > 0) {
                Helpers.barLiteral(iTooltip, secondaryProgress, 100,
                        Component.translatable("ic2.probe.progress.secondary.full.name", secondaryProgress, 100).append(" t").withStyle(ChatFormatting.WHITE), ColorMix.BLUE);
            }
            if (storedPoints > 0) {
                Helpers.barLiteral(iTooltip, storedPoints, 1000, Component.translatable("ic2.probe.uranium.type.name",
                        SanityHelper.toPascalCase(currentRecipe.getId().getPath()), storedPoints, 1000).withStyle(ChatFormatting.WHITE), currentRecipe.getColor());
            }
        }
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, ServerPlayer serverPlayer, Level level, BlockEntity blockEntity, boolean b) {
        CompoundTag tag = new CompoundTag();
        if (blockEntity instanceof BaseElectricTileEntity tile) {
            if (tile instanceof UraniumEnchricherTileEntity enricher) {
                tag.putInt("mainProgress", enricher.mainProgress);
                tag.putInt("secondaryProgress", enricher.secondaryProgress);
                tag.putInt("storedPoints", enricher.storedPoints);
                if (enricher.storedType != null) {
                    tag.putString("storedType", enricher.storedType.toString());
                }
            }
        }
        compoundTag.put("EnricherInfo", tag);
    }

    @Override
    public ResourceLocation getUid() {
        return JadeIC2CPluginHandler.EU_READER_INFO;
    }
}

package dev.crossvas.jadexic2c.providers;

import dev.crossvas.jadexic2c.base.IInfoProvider;
import dev.crossvas.jadexic2c.base.IJadeHelper;
import ic2.api.energy.EnergyNet;
import ic2.core.block.machines.recipes.misc.EnrichRecipe;
import ic2.core.block.machines.tiles.hv.UraniumEnchricherTileEntity;
import ic2.core.utils.helpers.SanityHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class UraniumEnricherInfo implements IInfoProvider {

    public static final UraniumEnricherInfo THIS = new UraniumEnricherInfo();

    @Override
    public void addInfo(IJadeHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof UraniumEnchricherTileEntity enricher) {
            text(helper, "ic2.probe.eu.tier.name", EnergyNet.INSTANCE.getDisplayTier(enricher.getTier()));
            text(helper, "ic2.probe.eu.max_in.name", enricher.getMaxInput());
            EnrichRecipe recipe = enricher.getRecipeList().getRecipe(enricher.storedType);
            text(helper, "ic2.probe.eu.usage.name", recipe != null ? recipe.getEnergyCost() + 100 : 100);
            if (enricher.mainProgress > 0) {
                helper.addBarElement(enricher.mainProgress / 20, 1000 / 20, Component.translatable("ic2.probe.progress.full.name", enricher.mainProgress / 20, 1000 / 20).append("s").withStyle(ChatFormatting.WHITE), -16733185);
            }
            if (enricher.secondaryProgress > 0) {
                helper.addBarElement(enricher.secondaryProgress, 100, Component.translatable("ic2.probe.progress.secondary.full.name", enricher.secondaryProgress, 100).append("t").withStyle(ChatFormatting.WHITE), -16733185);
            }
            if (enricher.storedPoints > 0) {
                helper.addBarElement(enricher.storedPoints, 1000, Component.translatable("ic2.probe.uranium.type.name",
                        SanityHelper.toPascalCase(recipe.getId().getPath()), enricher.storedPoints, 1000).withStyle(ChatFormatting.WHITE), recipe.getColor());
            }
        }
    }
}

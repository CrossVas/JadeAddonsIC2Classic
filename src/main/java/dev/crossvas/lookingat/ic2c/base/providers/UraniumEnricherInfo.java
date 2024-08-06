package dev.crossvas.lookingat.ic2c.base.providers;

import dev.crossvas.lookingat.ic2c.base.interfaces.IInfoProvider;
import dev.crossvas.lookingat.ic2c.base.interfaces.IHelper;
import ic2.api.energy.EnergyNet;
import ic2.core.block.machines.recipes.misc.EnrichRecipe;
import ic2.core.block.machines.tiles.hv.UraniumEnchricherTileEntity;
import ic2.core.utils.helpers.SanityHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class UraniumEnricherInfo implements IInfoProvider {

    public static final UraniumEnricherInfo THIS = new UraniumEnricherInfo();

    @Override
    public void addInfo(IHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof UraniumEnchricherTileEntity enricher) {
            defaultText(helper, "ic2.probe.eu.tier.name", EnergyNet.INSTANCE.getDisplayTier(enricher.getTier()));
            defaultText(helper, "ic2.probe.eu.max_in.name", enricher.getMaxInput());
            EnrichRecipe recipe = enricher.getRecipeList().getRecipe(enricher.storedType);
            defaultText(helper, "ic2.probe.eu.usage.name", recipe != null ? recipe.getEnergyCost() + 100 : 100);
            if (enricher.mainProgress > 0) {
                bar(helper, enricher.mainProgress / 20, 1000 / 20, Component.translatable("ic2.probe.progress.full.name", enricher.mainProgress / 20, 1000 / 20).append("s"), -16733185);
            }
            if (enricher.getSubProgress() > 0) {
                bar(helper, (int) enricher.getSubProgress(), 100, Component.translatable("ic2.probe.progress.secondary.full.name", enricher.secondaryProgress, 100).append("t"), recipe != null ? recipe.getColor() : -16733185);
            }
            if (enricher.storedPoints > 0) {
                bar(helper, enricher.storedPoints, 1000, Component.translatable("ic2.probe.uranium.type.name",
                        SanityHelper.toPascalCase(recipe.getId().getPath()), enricher.storedPoints, 1000), recipe.getColor());
            }
        }
    }
}

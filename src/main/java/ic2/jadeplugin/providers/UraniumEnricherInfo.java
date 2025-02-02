package ic2.jadeplugin.providers;

import ic2.core.block.machines.recipes.misc.EnrichRecipe;
import ic2.core.block.machines.tiles.hv.UraniumEnchricherTileEntity;
import ic2.core.utils.helpers.SanityHelper;
import ic2.jadeplugin.base.JadeHelper;
import ic2.jadeplugin.base.interfaces.IInfoProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class UraniumEnricherInfo implements IInfoProvider {

    public static final UraniumEnricherInfo THIS = new UraniumEnricherInfo();

    @Override
    public void addInfo(JadeHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof UraniumEnchricherTileEntity enricher) {
            helper.maxIn(enricher.getMaxInput());
            EnrichRecipe recipe = enricher.getRecipeList().getRecipe(enricher.storedType);
            helper.usage(recipe != null ? recipe.getEnergyCost() + 100 : 100);
            if (enricher.mainProgress > 0) {
                helper.bar(enricher.mainProgress / 20, 1000 / 20, translate("ic2.probe.progress.full.name", enricher.mainProgress / 20, 1000 / 20).append("s"), -16733185);
            }
            if (enricher.getSubProgress() > 0) {
                helper.bar((int) enricher.getSubProgress(), 100, translate("ic2.probe.progress.secondary.full.name", enricher.secondaryProgress, 100).append("t"), recipe != null ? recipe.getColor() : -16733185);
            }
            if (enricher.storedPoints > 0) {
                helper.bar(enricher.storedPoints, 1000, translate("ic2.probe.uranium.type.name",
                        SanityHelper.toPascalCase(recipe.getId().getPath()), enricher.storedPoints, 1000), recipe.getColor());
            }
        }
    }
}

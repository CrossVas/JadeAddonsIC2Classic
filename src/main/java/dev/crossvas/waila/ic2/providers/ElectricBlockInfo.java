package dev.crossvas.waila.ic2.providers;

import dev.crossvas.waila.ic2.base.WailaCommonHandler;
import dev.crossvas.waila.ic2.base.interfaces.IInfoProvider;
import dev.crossvas.waila.ic2.base.interfaces.IWailaHelper;
import dev.crossvas.waila.ic2.utils.ColorUtils;
import dev.crossvas.waila.ic2.utils.Formatter;
import ic2.api.recipe.RecipeOutput;
import ic2.api.recipe.Recipes;
import ic2.core.block.machine.tileentity.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

public class ElectricBlockInfo implements IInfoProvider {

    public static final ElectricBlockInfo THIS = new ElectricBlockInfo();

    @Override
    public void addInfo(IWailaHelper helper, TileEntity blockEntity, EntityPlayer player) {
        if (blockEntity instanceof TileEntityElecMachine) {
            TileEntityElecMachine baseTile = (TileEntityElecMachine) blockEntity;
            if (baseTile instanceof TileEntityTerra || baseTile instanceof TileEntityReactorPlanner || baseTile instanceof TileEntityCropHarvester) {
                text(helper, tier(baseTile.tier));
                text(helper, maxIn(baseTile.maxInput));
            }

            if (baseTile instanceof TileEntityElectricEnchanter) {
                TileEntityElectricEnchanter enchanter = (TileEntityElectricEnchanter) baseTile;
                text(helper, tier(enchanter.tier));
                text(helper, maxIn(enchanter.maxInput));
                text(helper, usage(enchanter.getEnergyUsage()));
                int storedXP = enchanter.exp;
                float progress = enchanter.getProgress();
                float maxProgress = 1f;
                if (storedXP <= 0) {
                    text(helper, translatable("probe.enchanter.missing").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
                } else {
                    bar(helper, storedXP, 1000, translatable("probe.machine.xp", storedXP), ColorUtils.GREEN);
                }
                if (progress > 0) {
                    bar(helper, (int) progress, (int) maxProgress, translatable("probe.progress.full.name", progress, maxProgress).appendText("t"), -16733185);

                }
            }
            if (baseTile instanceof TileEntityMatter) {
                TileEntityMatter massFab = (TileEntityMatter) baseTile;
                text(helper, tier(massFab.tier));
                text(helper, maxIn(massFab.maxInput));
                int progress = massFab.energy;
                int maxProgress = 7000000;
                if (progress > 0) {
                    double finalProgress = (double) progress / 7000000 * 100.0;
                    if (finalProgress > 100) finalProgress = 100;
                    bar(helper, progress, maxProgress, translatable("probe.speed.massFab",
                            Formatter.THERMAL_GEN.format(finalProgress)), -4441721);
                }
                RecipeOutput output = Recipes.matterAmplifier.getOutputFor(massFab.inventory[0], false);
                int lastScrap = output.metadata.getInteger("amplification");
                if (massFab.scrap > 0) {
                    bar(helper, massFab.scrap, lastScrap * 2, translatable("probe.matter.amplifier.name",
                            massFab.scrap), -10996205);
                }
            }
        }
        if (blockEntity instanceof TileEntityCropmatron) {
            text(helper, tier(1));
            text(helper, maxIn(32));
            WailaCommonHandler.addTankInfo(helper, blockEntity);
        }
        if (blockEntity instanceof TileEntityMagnetizer) {
            text(helper, tier(1));
            text(helper, maxIn(32));
        }
        if (blockEntity instanceof TileEntityTesla) {
            text(helper, tier(2));
            text(helper, maxIn(128));
        }
    }
}

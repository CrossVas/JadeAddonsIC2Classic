package dev.crossvas.lookingat.ic2c.base.providers;

import dev.crossvas.lookingat.ic2c.base.interfaces.IInfoProvider;
import dev.crossvas.lookingat.ic2c.base.interfaces.IHelper;
import ic2.api.crops.ICrop;
import ic2.api.crops.ICropTile;
import ic2.api.crops.ISeedCrop;
import ic2.core.inventory.filter.IFilter;
import ic2.core.utils.math.ColorUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class CropInfo implements IInfoProvider {

    public static final CropInfo THIS = new CropInfo();

    @Override
    public IFilter getFilter() {
        return CROP_ANALYZER;
    }

    @Override
    public void addInfo(IHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof ICropTile tile) {
            ICrop crop = tile.getCrop();

            // storage stats
            int fertilizer = tile.getFertilizerStorage();
            int water = tile.getWaterStorage();
            int weedex = tile.getWeedExStorage();

            // env stats
            int nutrients = tile.getNutrients();
            int humidity = tile.getHumidity();
            int env = tile.getEnvironmentQuality();
            int light = tile.getLightLevel();

            if (crop != null) {
                // growth info
                int maxStage = crop.getGrowthSteps();
                int currentStage = tile.getGrowthStage();
                int scanLevel = tile.getScanLevel();
                int growthSpeed = tile.calculateGrowthSpeed();
                int points = tile.getGrowthPoints();
                int maxPoints = crop.getGrowthDuration(tile);

                // stats info
                int growth = tile.getGrowthStat();
                int gain = tile.getGainStat();
                int resistance = tile.getResistanceStat();
                boolean canGrow = crop.canGrow(tile);
                boolean waterLogCompat = crop.getCropType().isCompatible(tile.isWaterLogged());

                if (scanLevel < 4 && currentStage < maxStage) {
                    bar(helper, scanLevel, 4, Component.translatable("ic2.probe.crop.info.scan", scanLevel, 4), ColorUtils.GREEN);
                } else {
                    centered(helper, Component.translatable("ic2.probe.crop.growth").withStyle(ChatFormatting.YELLOW));
                    if (currentStage < maxStage) {
                        bar(helper, currentStage, maxStage, Component.translatable("ic2.probe.crop.info.stage", currentStage, maxStage), ColorUtils.GREEN);
                        bar(helper, points, maxPoints, Component.translatable("ic2.probe.crop.info.points", points, maxPoints), ColorUtils.GREEN);
                        if (canGrow && waterLogCompat) {
                            centered(helper, Component.translatable("ic2.probe.crop.grow.rate", growthSpeed).withStyle(ChatFormatting.GOLD));
                        } else {
                            centered(helper, Component.translatable("ic2.probe.crop.grow.not").withStyle(ChatFormatting.RED));
                        }
                    } else {
                        bar(helper, currentStage, maxStage, Component.translatable("ic2.probe.crop.info.stage_done"), ColorUtils.GREEN);
                    }

                    if (scanLevel >= 4) {
                        // title
                        centered(helper, Component.translatable("ic2.probe.crop.stats").withStyle(ChatFormatting.YELLOW));
                        bar(helper, growth, 31, Component.translatable("ic2.probe.crop.info.growth", growth, 31), ColorUtils.CYAN);
                        bar(helper, gain, 31, Component.translatable("ic2.probe.crop.info.gain", gain, 31), -5829955);
                        bar(helper, resistance, 31, Component.translatable("ic2.probe.crop.info.resistance", resistance, 31), ColorUtils.rgb(255, 170, 0));

                        int stress = (crop.getProperties().getTier() - 1) * 4 + growth + gain + resistance;
                        int maxStress = crop.getStatInfluence(tile, humidity, nutrients, env) * 5;
                        bar(helper, stress, maxStress, Component.translatable("ic2.probe.crop.info.needs", stress, maxStress), ColorUtils.CYAN);
                    }
                }

                if (crop instanceof ISeedCrop seedCrop) {
                    boolean isDroppingSeed = seedCrop.isDroppingSeeds(tile);
                    text(helper, Component.translatable("ic2.probe.crop.seed_drop", (isDroppingSeed ? ChatFormatting.GREEN : ChatFormatting.RED) + String.valueOf(isDroppingSeed)).withStyle(ChatFormatting.GOLD));
                }
            }

            // title
            centered(helper, Component.translatable("ic2.probe.crop.storage").withStyle(ChatFormatting.YELLOW));
            bar(helper, fertilizer, 300, Component.translatable("ic2.probe.crop.info.fertilizer", fertilizer, 300), ColorUtils.rgb(86, 54, 36));
            bar(helper, water, 200, Component.translatable("ic2.probe.crop.info.water", water, 200), ColorUtils.rgb(93, 105, 255));
            bar(helper, weedex, 150, Component.translatable("ic2.probe.crop.info.weedex", weedex, 150), ColorUtils.rgb(255, 85, 255));

            // title
            centered(helper, Component.translatable("ic2.probe.crop.env").withStyle(ChatFormatting.YELLOW));
            bar(helper, nutrients, 20, Component.translatable("ic2.probe.crop.info.nutrients", nutrients, 20), ColorUtils.rgb(0, 255, 5));
            bar(helper, humidity, 20, Component.translatable("ic2.probe.crop.info.humidity", humidity, 20), ColorUtils.rgb(93, 105, 255));
            bar(helper, env, 10, Component.translatable("ic2.probe.crop.info.env", env, 10), ColorUtils.CYAN);
            bar(helper, light, 15, Component.translatable("ic2.probe.crop.info.light", light, 15), ColorUtils.rgb(255, 255, 85));
        }
    }
}

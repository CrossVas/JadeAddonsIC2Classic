package dev.crossvas.jadexic2c.info;

import dev.crossvas.jadexic2c.IHelper;
import dev.crossvas.jadexic2c.JadeIC2CPluginHandler;
import dev.crossvas.jadexic2c.utils.ColorMix;
import dev.crossvas.jadexic2c.utils.Helpers;
import ic2.api.crops.ICrop;
import ic2.api.crops.ICropTile;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec2;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum CropInfoProvider implements IHelper<BlockEntity> {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (!shouldAddInfo(blockAccessor, "CropInfo")) {
            return;
        }

        CompoundTag tag = getData(blockAccessor, "CropInfo");
        if (blockAccessor.getBlockEntity() instanceof ICropTile tile) {
            // growth info
            int maxStage = tag.getInt("growthSteps");
            int currentStage = tag.getInt("growthStage");
            int scanLevel = tag.getInt("scanLevel");
            int growthSpeed = tag.getInt("growthSpeed");
            int points = tag.getInt("growthPoints");
            int maxPoints = tag.getInt("growthDuration");

            // stats info
            int growth = tag.getInt("growth");
            int gain = tag.getInt("gain");
            int resistance = tag.getInt("resistance");
            // storage info
            int fertilizer = tag.getInt("fertilizer");
            int water = tag.getInt("water");
            int weedex = tag.getInt("weedex");
            // environment info
            int nutrients = tag.getInt("nutrients");
            int humidity = tag.getInt("humidity");
            int env = tag.getInt("env");
            int light = tag.getInt("light");

            ICrop crop = tile.getCrop();
            if (crop != null) {
                if (scanLevel < 4 && currentStage < maxStage) {
                    iTooltip.add(Component.literal("Crop: ").append("Unknown"));
                    Helpers.bar(iTooltip, scanLevel, 4, "ic2.probe.crop.info.scan", ColorMix.GREEN);
                } else {
                    iTooltip.add(Component.literal("Crop: ").append(crop.getName()).withStyle(ChatFormatting.WHITE));
                    iTooltip.append(iTooltip.getElementHelper().spacer(5, 0));
                    iTooltip.append(iTooltip.getElementHelper().item(crop.getDisplayItem()).translate(new Vec2(0, -5)));
                    Helpers.text(iTooltip, Component.translatable("ic2.probe.crop.growth").withStyle(ChatFormatting.YELLOW));
                    if (currentStage < maxStage) {
                        Helpers.bar(iTooltip, currentStage, maxStage, "ic2.probe.crop.info.stage", ColorMix.GREEN);
                        Helpers.bar(iTooltip, points, maxPoints, "ic2.probe.crop.info.points", ColorMix.GREEN);
                        if (tag.getBoolean("canGrow") && tag.getBoolean("isWaterlogCompatible")) {
                            Helpers.text(iTooltip, "ic2.probe.crop.grow.rate", growthSpeed);
                        } else {
                            Helpers.text(iTooltip, Component.translatable("ic2.probe.crop.grow.not").withStyle(ChatFormatting.RED));
                        }
                    } else {
                        Helpers.bar(iTooltip, currentStage, maxStage, "ic2.probe.crop.info.stage_done", ColorMix.GREEN);
                    }

                    if (scanLevel >= 4) {
                        Helpers.text(iTooltip, Component.translatable("ic2.probe.crop.stats").withStyle(ChatFormatting.YELLOW)); // title
                        Helpers.bar(iTooltip, growth, 31, "ic2.probe.crop.info.growth", ColorMix.AQUA);
                        Helpers.bar(iTooltip, gain, 31, "ic2.probe.crop.info.gain", ColorMix.PURPLE);
                        Helpers.bar(iTooltip, resistance, 31, "ic2.probe.crop.info.resistance", ColorMix.GOLD);

                        int need = (crop.getProperties().getTier() - 1) * 4 + growth + gain + resistance;
                        int have = crop.getStatInfluence(tile, humidity, nutrients, env) * 5;
                        Helpers.bar(iTooltip, need, have, "ic2.probe.crop.info.needs", ColorMix.AQUA);
                    }
                }
            }

            Helpers.text(iTooltip, Component.translatable("ic2.probe.crop.storage").withStyle(ChatFormatting.YELLOW)); // title
            Helpers.bar(iTooltip, fertilizer, 300, "ic2.probe.crop.info.fertilizer", ColorMix.BROWN);
            Helpers.bar(iTooltip, water, 200, "ic2.probe.crop.info.water", ColorMix.CORNFLOWER);
            Helpers.bar(iTooltip, weedex, 150, "ic2.probe.crop.info.weedex", ColorMix.PINK);

            Helpers.text(iTooltip, Component.translatable("ic2.probe.crop.env").withStyle(ChatFormatting.YELLOW)); // title
            Helpers.monoBar(iTooltip, nutrients, 20, "ic2.probe.crop.info.nutrients", ColorMix.MONO_LIME);
            Helpers.bar(iTooltip, humidity, 20, "ic2.probe.crop.info.humidity", ColorMix.CORNFLOWER);
            Helpers.monoBar(iTooltip, env, 10, "ic2.probe.crop.info.env", ColorMix.MONO_AQUA);
            Helpers.monoBar(iTooltip, light, 15, "ic2.probe.crop.info.light", ColorMix.MONO_YELLOW);
        }
    }

    @Override
    public ResourceLocation getUid() {
        return JadeIC2CPluginHandler.CROP_INFO;
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, ServerPlayer serverPlayer, Level level, BlockEntity blockEntity, boolean b) {
        if (blockEntity instanceof ICropTile tile) {
            CompoundTag tag = new CompoundTag();
            ICrop crop = tile.getCrop();
            if (crop != null) {
                // growth info
                tag.putInt("growthSteps", crop.getGrowthSteps());
                tag.putInt("growthStage", tile.getGrowthStage());
                tag.putInt("scanLevel", tile.getScanLevel());
                tag.putInt("growthSpeed", tile.calculateGrowthSpeed());
                tag.putInt("growthPoints", tile.getGrowthPoints());
                tag.putInt("growthDuration", crop.getGrowthDuration(tile));
                // main stats
                tag.putInt("growth", tile.getGrowthStat());
                tag.putInt("gain", tile.getGainStat());
                tag.putInt("resistance", tile.getResistanceStat());
                tag.putBoolean("canGrow", crop.canGrow(tile));
                tag.putBoolean("isWaterlogCompatible", crop.getCropType().isCompatible(tile.isWaterLogged()));
            }
            // main stats - storage
            tag.putInt("fertilizer", tile.getFertilizerStorage());
            tag.putInt("water", tile.getWaterStorage());
            tag.putInt("weedex", tile.getWeedExStorage());
            // env
            tag.putInt("nutrients", tile.getNutrients());
            tag.putInt("humidity", tile.getHumidity());
            tag.putInt("env", tile.getEnvironmentQuality());
            tag.putInt("light", tile.getLightLevel());
            compoundTag.put("CropInfo", tag);
        }
    }
}

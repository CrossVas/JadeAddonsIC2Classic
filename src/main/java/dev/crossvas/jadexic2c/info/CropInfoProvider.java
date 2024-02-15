package dev.crossvas.jadexic2c.info;

import dev.crossvas.jadexic2c.JadeIC2CPluginHandler;
import dev.crossvas.jadexic2c.helpers.BarHelper;
import dev.crossvas.jadexic2c.helpers.IHelper;
import dev.crossvas.jadexic2c.helpers.TextHelper;
import dev.crossvas.jadexic2c.utils.ColorMix;
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
                    iTooltip.add(Component.literal("Crop: ").append("Unknown").withStyle(ChatFormatting.WHITE));
                    BarHelper.bar(iTooltip, scanLevel, 4, Component.translatable("ic2.probe.crop.info.scan", scanLevel, 4).withStyle(ChatFormatting.WHITE), ColorMix.GREEN);
                } else {
                    iTooltip.add(Component.literal("Crop: ").append(crop.getName()).withStyle(ChatFormatting.WHITE));
                    iTooltip.append(iTooltip.getElementHelper().item(crop.getDisplayItem()).translate(new Vec2(0, -5)));
                    TextHelper.text(iTooltip, Component.literal("by " + crop.discoveredBy().getString()).withStyle(ChatFormatting.WHITE));
                    TextHelper.text(iTooltip, Component.translatable("ic2.probe.crop.growth").withStyle(ChatFormatting.YELLOW), true);
                    if (currentStage < maxStage) {
                        BarHelper.bar(iTooltip, currentStage, maxStage, Component.translatable("ic2.probe.crop.info.stage", currentStage, maxStage).withStyle(ChatFormatting.WHITE), ColorMix.GREEN);
                        BarHelper.bar(iTooltip, points, maxPoints, Component.translatable("ic2.probe.crop.info.points", points, maxPoints).withStyle(ChatFormatting.WHITE), ColorMix.GREEN);
                        if (tag.getBoolean("canGrow") && tag.getBoolean("isWaterlogCompatible")) {
                            TextHelper.text(iTooltip, Component.translatable("ic2.probe.crop.grow.rate", growthSpeed).withStyle(ChatFormatting.GOLD), true);
                        } else {
                            TextHelper.text(iTooltip, Component.translatable("ic2.probe.crop.grow.not").withStyle(ChatFormatting.RED), true);
                        }
                    } else {
                        BarHelper.bar(iTooltip, currentStage, maxStage, Component.translatable("ic2.probe.crop.info.stage_done").withStyle(ChatFormatting.WHITE), ColorMix.GREEN);
                    }

                    if (scanLevel >= 4) {
                        TextHelper.text(iTooltip, Component.translatable("ic2.probe.crop.stats").withStyle(ChatFormatting.YELLOW), true); // title
                        BarHelper.bar(iTooltip, growth, 31, Component.translatable("ic2.probe.crop.info.growth", growth, 31), ColorMix.AQUA);
                        BarHelper.bar(iTooltip, gain, 31, Component.translatable("ic2.probe.crop.info.gain", gain, 31), ColorMix.PURPLE);
                        BarHelper.bar(iTooltip, resistance, 31, Component.translatable("ic2.probe.crop.info.resistance", resistance, 31), ColorMix.GOLD);

                        int stress = (crop.getProperties().getTier() - 1) * 4 + growth + gain + resistance;
                        int maxStress = crop.getStatInfluence(tile, humidity, nutrients, env) * 5;
                        BarHelper.bar(iTooltip, stress, maxStress, Component.translatable("ic2.probe.crop.info.needs", stress, maxStress), ColorMix.AQUA);
                    }
                }
            }

            TextHelper.text(iTooltip, Component.translatable("ic2.probe.crop.storage").withStyle(ChatFormatting.YELLOW), true); // title
            BarHelper.bar(iTooltip, fertilizer, 300, Component.translatable("ic2.probe.crop.info.fertilizer", fertilizer, 300), ColorMix.BROWN);
            BarHelper.bar(iTooltip, water, 200, Component.translatable("ic2.probe.crop.info.water", water, 200), ColorMix.CORNFLOWER);
            BarHelper.bar(iTooltip, weedex, 150, Component.translatable("ic2.probe.crop.info.weedex", weedex, 150), ColorMix.PINK);

            TextHelper.text(iTooltip, Component.translatable("ic2.probe.crop.env").withStyle(ChatFormatting.YELLOW), true); // title
            BarHelper.monoBar(iTooltip, nutrients, 20, Component.translatable("ic2.probe.crop.info.nutrients", nutrients, 20).withStyle(ChatFormatting.WHITE), ColorMix.MONO_LIME);
            BarHelper.bar(iTooltip, humidity, 20, Component.translatable("ic2.probe.crop.info.humidity", humidity, 20).withStyle(ChatFormatting.WHITE), ColorMix.CORNFLOWER);
            BarHelper.monoBar(iTooltip, env, 10, Component.translatable("ic2.probe.crop.info.env", env, 10).withStyle(ChatFormatting.WHITE), ColorMix.MONO_AQUA);
            BarHelper.monoBar(iTooltip, light, 15, Component.translatable("ic2.probe.crop.info.light", light, 15).withStyle(ChatFormatting.WHITE), ColorMix.MONO_YELLOW);
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

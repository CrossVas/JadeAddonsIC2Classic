package dev.crossvas.jadexic2c.providers;

import dev.crossvas.jadexic2c.JadeTags;
import dev.crossvas.jadexic2c.base.JadeHelper;
import dev.crossvas.jadexic2c.base.interfaces.IInfoProvider;
import ic2.api.crops.ICrop;
import ic2.api.crops.ICropRegistry;
import ic2.api.crops.ICropTile;
import ic2.api.crops.ISeedCrop;
import ic2.core.inventory.filter.IFilter;
import ic2.core.platform.registries.IC2Items;
import ic2.core.utils.math.ColorUtils;
import ic2.core.utils.tooltips.ILangHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;
import snownee.jade.Jade;
import snownee.jade.addon.harvest.HarvestToolProvider;
import snownee.jade.api.*;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.IElement;
import snownee.jade.api.ui.IElementHelper;

import java.util.List;

public class CropInfo implements IInfoProvider {

    public static final CropInfo THIS = new CropInfo();

    @Override
    public IFilter getFilter() {
        return CROP_ANALYZER;
    }

    @Override
    public void addInfo(JadeHelper helper, BlockEntity blockEntity, Player player) {
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
                    helper.bar(scanLevel, 4, translate("ic2.probe.crop.info.scan", scanLevel, 4), ColorUtils.GREEN);
                } else {
                    helper.centered(translate("ic2.probe.crop.growth").withStyle(ChatFormatting.YELLOW));
                    if (currentStage < maxStage) {
                        helper.bar(currentStage, maxStage, translate("ic2.probe.crop.info.stage", currentStage, maxStage), ColorUtils.GREEN);
                        helper.bar(points, maxPoints, translate("ic2.probe.crop.info.points", points, maxPoints), ColorUtils.GREEN);
                        if (canGrow && waterLogCompat) {
                            helper.centered(translate("ic2.probe.crop.grow.rate", growthSpeed).withStyle(ChatFormatting.GOLD));
                        } else {
                            helper.centered(translate("ic2.probe.crop.grow.not").withStyle(ChatFormatting.RED));
                        }
                    } else {
                        helper.bar(currentStage, maxStage, translate("ic2.probe.crop.info.stage_done"), ColorUtils.GREEN);
                    }

                    if (scanLevel >= 4) {
                        // title
                        helper.centered(translate("ic2.probe.crop.stats").withStyle(ChatFormatting.YELLOW));
                        helper.bar(growth, 31, translate("ic2.probe.crop.info.growth", growth, 31), ColorUtils.CYAN);
                        helper.bar(gain, 31, translate("ic2.probe.crop.info.gain", gain, 31), -5829955);
                        helper.bar(resistance, 31, translate("ic2.probe.crop.info.resistance", resistance, 31), ColorUtils.rgb(255, 170, 0));

                        int stress = (crop.getProperties().getTier() - 1) * 4 + growth + gain + resistance;
                        int maxStress = crop.getStatInfluence(tile, humidity, nutrients, env) * 5;
                        helper.bar(stress, maxStress, translate("ic2.probe.crop.info.needs", stress, maxStress), ColorUtils.CYAN);
                    }
                }

                if (crop instanceof ISeedCrop seedCrop) {
                    boolean isDroppingSeed = seedCrop.isDroppingSeeds(tile);
                    helper.text(translate("ic2.probe.crop.seed_drop", (isDroppingSeed ? ChatFormatting.GREEN : ChatFormatting.RED) + String.valueOf(isDroppingSeed)).withStyle(ChatFormatting.GOLD));
                }
            }

            // title
            helper.centered(translate("ic2.probe.crop.storage").withStyle(ChatFormatting.YELLOW));
            helper.bar(fertilizer, 300, translate("ic2.probe.crop.info.fertilizer", fertilizer, 300), ColorUtils.rgb(86, 54, 36));
            helper.bar(water, 200, translate("ic2.probe.crop.info.water", water, 200), ColorUtils.rgb(93, 105, 255));
            helper.bar(weedex, 150, translate("ic2.probe.crop.info.weedex", weedex, 150), ColorUtils.rgb(255, 85, 255));

            // title
            helper.centered(translate("ic2.probe.crop.env").withStyle(ChatFormatting.YELLOW));
            helper.bar(nutrients, 20, translate("ic2.probe.crop.info.nutrients", nutrients, 20), ColorUtils.rgb(0, 255, 5));
            helper.bar(humidity, 20, translate("ic2.probe.crop.info.humidity", humidity, 20), ColorUtils.rgb(93, 105, 255));
            helper.bar(env, 10, translate("ic2.probe.crop.info.env", env, 10), ColorUtils.CYAN);
            helper.bar(light, 15, translate("ic2.probe.crop.info.light", light, 15), ColorUtils.rgb(255, 255, 85));
        }
    }

    public static class CropIcon implements IBlockComponentProvider, IServerDataProvider<BlockEntity>, ILangHelper {

        public static final CropIcon THIS = new CropIcon();

        @Override
        public @Nullable IElement getIcon(BlockAccessor accessor, IPluginConfig config, IElement currentIcon) {
            ItemStack iconStack = ItemStack.EMPTY;
            if (accessor.getBlockEntity() instanceof ICropTile cropTile) {
                CompoundTag tag = accessor.getServerData().getCompound("CropInfo");
                int maxStage = tag.getInt("growthSteps");
                int currentStage = tag.getInt("growthStage");
                int scanLevel = tag.getInt("scanLevel");
                if (cropTile.getCrop() != null) {
                    boolean condition = scanLevel < 1 && currentStage < maxStage && cropTile.getCrop() != ICropRegistry.WEED && cropTile.getCrop() != ICropRegistry.SEA_WEED;
                    if (condition) {
                        iconStack = IC2Items.CROP_SEED.getDefaultInstance();
                    } else {
                        iconStack = cropTile.getCrop().getDisplayItem();
                    }
                }
            }
            return IElementHelper.get().item(iconStack);
        }

        @Override
        public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
            CompoundTag tag = blockAccessor.getServerData().getCompound("CropInfo");
            if (blockAccessor.getBlockEntity() instanceof ICropTile tile) {
                int maxStage = tag.getInt("growthSteps");
                int currentStage = tag.getInt("growthStage");
                int scanLevel = tag.getInt("scanLevel");
                ICrop crop = tile.getCrop();
                if (crop != null) {
                    List<IElement> elements = HarvestToolProvider.INSTANCE.getText(blockAccessor, iPluginConfig, iTooltip.getElementHelper());
                    iTooltip.remove(Identifiers.MC_HARVEST_TOOL);
                    iTooltip.remove(Identifiers.CORE_OBJECT_NAME);
                    if (scanLevel < 1 && currentStage < maxStage && crop != ICropRegistry.WEED && crop != ICropRegistry.SEA_WEED) {
                        iTooltip.add(0, translate("info.crop.ic2.data.unknown").withStyle(ChatFormatting.WHITE));
                        elements.forEach(element -> iTooltip.append(0, element.align(IElement.Align.RIGHT)));
                    } else {
                        iTooltip.add(0, Jade.CONFIG.get().getFormatting().title(crop.getName()), Identifiers.CORE_OBJECT_NAME);
                        elements.forEach(element -> iTooltip.append(0, element.align(IElement.Align.RIGHT)));
                        iTooltip.add(1, iTooltip.getElementHelper().text(translate("jei.ic2.reactor.by", crop.discoveredBy().copy().withStyle(ChatFormatting.AQUA)).withStyle(ChatFormatting.WHITE)));
                    }
                }
            }
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
                }
                compoundTag.put("CropInfo", tag);
            }
        }

        @Override
        public ResourceLocation getUid() {
            return JadeTags.INFO_RENDERER;
        }
    }
}

package dev.crossvas.jadexic2c.providers;

import dev.crossvas.jadexic2c.base.interfaces.IInfoProvider;
import dev.crossvas.jadexic2c.base.interfaces.IJadeHelper;
import dev.crossvas.jadexic2c.utils.ColorUtils;
import ic2.api.classic.crops.ISeedCrop;
import ic2.api.crops.CropCard;
import ic2.api.crops.Crops;
import ic2.api.crops.ICropTile;
import ic2.core.block.crop.Ic2Crops;
import ic2.core.block.crop.TileEntityCrop;
import ic2.core.inventory.filters.IFilter;
import ic2.core.item.crop.ItemCropSeed;
import ic2.core.platform.registry.Ic2Items;
import ic2.core.util.misc.StackUtil;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class CropInfo implements IInfoProvider {

    public static final CropInfo THIS = new CropInfo();

    @Override
    public IFilter getFilter() {
        return CROP_ANALYZER;
    }

    @Override
    public void addInfo(IJadeHelper helper, TileEntity blockEntity, EntityPlayer player) {
        if (blockEntity instanceof TileEntityCrop) {
            TileEntityCrop tile = (TileEntityCrop) blockEntity;
            CropCard crop = tile.getCrop();

            // storage stats
            int fertilizer = tile.getStorageNutrients();
            int water = tile.getStorageWater();
            int weedex = tile.getStorageWeedEX();

            // env stats
            int nutrients = tile.getTerrainNutrients();
            int humidity = tile.getTerrainHumidity();
            int env = tile.getTerrainAirQuality();
            int light = tile.getLightLevel();

            if (crop != null) {
                // growth info
                int maxStage = crop.getMaxSize();
                int currentStage = tile.getCurrentSize();
                int scanLevel = tile.getScanLevel();
                int growthSpeed = tile.calculateGrowth();
                int points = tile.getGrowthPoints();
                int maxPoints = crop.getGrowthDuration(tile);

                // stats info
                int growth = tile.getStatGrowth();
                int gain = tile.getStatGain();
                int resistance = tile.getStatResistance();
                boolean canGrow = crop.canGrow(tile);

                if (scanLevel < 1 && currentStage < maxStage && crop != Crops.weed) {
                    text(helper, translatable("probe.crop.unknown"));
                } else {
                    text(helper, translatable("probe.crop.name", Ic2Crops.instance.getCropName(crop).getLocalized()).setStyle(new Style().setColor(TextFormatting.GOLD)));
                    text(helper, translatable("probe.crop.discovered", TextFormatting.AQUA + crop.getDiscoveredBy()));
                }

                if (scanLevel < 4 && currentStage < maxStage) {
                    bar(helper, scanLevel, 4, translatable("probe.crop.info.scan", scanLevel, 4), ColorUtils.GREEN);
                } else {
                    textCentered(helper, translatable("probe.crop.growth").setStyle(new Style().setColor(TextFormatting.YELLOW)));
                    if (currentStage < maxStage) {
                        bar(helper, currentStage, maxStage, translatable("probe.crop.info.stage", currentStage, maxStage), ColorUtils.GREEN);
                        bar(helper, points, maxPoints, translatable("probe.crop.info.points", points, maxPoints), ColorUtils.GREEN);
                        if (canGrow) {
                            textCentered(helper, translatable("probe.crop.grow.rate", growthSpeed).setStyle(new Style().setColor(TextFormatting.GOLD)));
                        } else {
                            textCentered(helper, translatable("probe.crop.grow.not").setStyle(new Style().setColor(TextFormatting.RED)));
                        }
                    } else {
                        bar(helper, currentStage, maxStage, translatable("probe.crop.info.stage_done"), ColorUtils.GREEN);
                    }

                    if (scanLevel >= 4) {
                        // title
                        textCentered(helper, translatable("probe.crop.stats").setStyle(new Style().setColor(TextFormatting.YELLOW)));
                        bar(helper, growth, 31, translatable("probe.crop.info.growth", growth, 31), ColorUtils.CYAN);
                        bar(helper, gain, 31, translatable("probe.crop.info.gain", gain, 31), -5829955);
                        bar(helper, resistance, 31, translatable("probe.crop.info.resistance", resistance, 31), ColorUtils.rgb(255, 170, 0));

                        int stress = (crop.getProperties().getTier() - 1) * 4 + growth + gain + resistance;
                        int maxStress = crop.getWeightInfluences(tile, humidity, nutrients, env) * 5;
                        bar(helper, stress, maxStress, translatable("probe.crop.info.needs", stress, maxStress), ColorUtils.CYAN);
                    }
                }

                if (crop instanceof ISeedCrop) {
                    ISeedCrop seedCrop = (ISeedCrop) crop;
                    boolean isDroppingSeed = seedCrop.doDropSeeds(tile);
                    textCentered(helper, translatable("probe.crop.seed_drop", status(isDroppingSeed)).setStyle(new Style().setColor(TextFormatting.GOLD)));
                }
            }

            // title
            textCentered(helper, translatable("probe.crop.storage").setStyle(new Style().setColor(TextFormatting.YELLOW)));
            bar(helper, fertilizer, 300, translatable("probe.crop.info.fertilizer", fertilizer, 300), ColorUtils.rgb(86, 54, 36));
            bar(helper, water, 200, translatable("probe.crop.info.water", water, 200), ColorUtils.rgb(93, 105, 255));
            bar(helper, weedex, 150, translatable("probe.crop.info.weedex", weedex, 150), ColorUtils.rgb(255, 85, 255));

            // title
            textCentered(helper, translatable("probe.crop.env").setStyle(new Style().setColor(TextFormatting.YELLOW)));
            bar(helper, nutrients, 20, translatable("probe.crop.info.nutrients", nutrients, 20), ColorUtils.rgb(0, 255, 5));
            bar(helper, humidity, 20, translatable("probe.crop.info.humidity", humidity, 20), ColorUtils.rgb(93, 105, 255));
            bar(helper, env, 10, translatable("probe.crop.info.env", env, 10), ColorUtils.CYAN);
            bar(helper, light, 15, translatable("probe.crop.info.light", light, 15), ColorUtils.rgb(255, 255, 85));
        }
    }

    public static class CropIconProvider implements IWailaDataProvider {

        public static final CropIconProvider THIS = new CropIconProvider();

        @Nonnull
        @Override
        public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
            ItemStack icon = ItemStack.EMPTY;
            if (accessor.getTileEntity() instanceof ICropTile) {
                ICropTile tile = (ICropTile) accessor.getTileEntity();
                NBTTagCompound tag = accessor.getNBTData().getCompoundTag("CropInfo");
                int maxStage = tag.getInteger("growthSteps");
                int currentStage = tag.getInteger("growthStage");
                int scanLevel = tag.getInteger("scanLevel");
                if (tile.getCrop() != null) {
                    boolean condition = scanLevel < 1 && tile.getCrop() != Crops.weed && currentStage < maxStage;
                    if (condition) {
                        icon = Ic2Items.cropSeed.copy();
                    } else {
                        ItemStack crop = ItemCropSeed.generateItemStack(tile.getCrop(), 1, 1, 1, 4);
                        StackUtil.getOrCreateNbtData(crop).setBoolean("Visible", true);
                        icon = crop;
                    }

                }
            }
            return icon;
        }

        @Nonnull
        @Override
        public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity tileEntity, NBTTagCompound tag, World world, BlockPos pos) {
            if (tileEntity instanceof ICropTile) {
                ICropTile tile = (ICropTile) tileEntity;
                CropCard crop = tile.getCrop();
                if (crop != null) {
                    NBTTagCompound data = new NBTTagCompound();
                    data.setInteger("growthSteps", crop.getMaxSize());
                    data.setInteger("growthStage", tile.getCurrentSize());
                    data.setInteger("scanLevel", tile.getScanLevel());
                    tag.setTag("CropInfo", data);
                }
            }
            return tag;
        }
    }
}

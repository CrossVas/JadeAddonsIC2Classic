package dev.crossvas.waila.ic2.providers;

import dev.crossvas.waila.ic2.base.interfaces.IInfoProvider;
import dev.crossvas.waila.ic2.base.interfaces.IWailaHelper;
import dev.crossvas.waila.ic2.utils.ColorUtils;
import dev.crossvas.waila.ic2.utils.TextFormatter;
import ic2.api.crops.CropCard;
import ic2.api.crops.Crops;
import ic2.api.crops.ICropTile;
import ic2.core.Ic2Items;
import ic2.core.block.TileEntityCrop;
import ic2.core.block.inventory.IItemTransporter;
import ic2.core.item.ItemCropSeed;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.List;

public class CropInfo implements IInfoProvider {

    public static final CropInfo THIS = new CropInfo();

    @Override
    public IItemTransporter.IFilter getFilter() {
        return ANALYZER;
    }

    @Override
    public void addInfo(IWailaHelper helper, TileEntity blockEntity, EntityPlayer player) {
        if (blockEntity instanceof TileEntityCrop) {
            TileEntityCrop tile = (TileEntityCrop) blockEntity;
            CropCard crop = tile.getCrop();

            // storage stats
            int fertilizer = tile.getNutrientStorage();
            int water = tile.getHydrationStorage();
            int weedex = tile.getWeedExStorage();

            // env stats
            int nutrients = tile.getNutrients();
            int humidity = tile.getHumidity();
            int env = tile.getAirQuality();
            int light = tile.getLightLevel();

            if (crop != null) {
                // growth info
                int maxStage = crop.maxSize();
                int currentStage = tile.getSize();
                int scanLevel = tile.getScanLevel();
                int growthSpeed = tile.calcGrowthRate();
                int points = tile.growthPoints;
                int maxPoints = crop.growthDuration(tile);

                // stats info
                int growth = tile.getGrowth();
                int gain = tile.getGain();
                int resistance = tile.getResistance();
                boolean canGrow = crop.canGrow(tile);

                if (scanLevel < 1 && currentStage < maxStage && crop != Crops.weed) {
                    text(helper, translate("probe.crop.unknown"));
                } else {
                    text(helper, translate(TextFormatter.GOLD, "probe.crop.name", crop.displayName()));
                    text(helper, translate("probe.crop.discovered", TextFormatter.AQUA.literal(crop.discoveredBy())));
                }

                if (scanLevel < 4 && currentStage < maxStage) {
                    bar(helper, scanLevel, 4, translate("probe.crop.info.scan", scanLevel, 4), ColorUtils.GREEN);
                } else {
                    textCentered(helper, translate(TextFormatter.YELLOW, "probe.crop.growth"));
                    if (currentStage < maxStage) {
                        bar(helper, currentStage, maxStage, translate("probe.crop.info.stage", currentStage, maxStage), ColorUtils.GREEN);
                        bar(helper, points, maxPoints, translate("probe.crop.info.points", points, maxPoints), ColorUtils.GREEN);
                        if (canGrow) {
                            textCentered(helper, translate(TextFormatter.GOLD, "probe.crop.grow.rate", growthSpeed));
                        } else {
                            textCentered(helper, translate(TextFormatter.RED, "probe.crop.grow.not"));
                        }
                    } else {
                        bar(helper, currentStage, maxStage, translate("probe.crop.info.stage_done"), ColorUtils.GREEN);
                    }

                    if (scanLevel >= 4) {
                        // title
                        textCentered(helper, translate(TextFormatter.YELLOW, "probe.crop.stats"));
                        bar(helper, growth, 31, translate("probe.crop.info.growth", growth, 31), ColorUtils.CYAN);
                        bar(helper, gain, 31, translate("probe.crop.info.gain", gain, 31), -5829955);
                        bar(helper, resistance, 31, translate("probe.crop.info.resistance", resistance, 31), ColorUtils.rgb(255, 170, 0));

                        int stress = (crop.tier() - 1) * 4 + growth + gain + resistance;
                        int maxStress = crop.weightInfluences(tile, humidity, nutrients, env) * 5;
                        bar(helper, stress, maxStress, translate("probe.crop.info.needs", stress, maxStress), ColorUtils.CYAN);
                    }
                }
            }

            // title
            textCentered(helper, translate(TextFormatter.YELLOW, "probe.crop.storage"));
            bar(helper, fertilizer, 300, translate("probe.crop.info.fertilizer", fertilizer, 300), ColorUtils.rgb(86, 54, 36));
            bar(helper, water, 200, translate("probe.crop.info.water", water, 200), ColorUtils.rgb(93, 105, 255));
            bar(helper, weedex, 150, translate("probe.crop.info.weedex", weedex, 150), ColorUtils.rgb(255, 85, 255));

            // title
            textCentered(helper, translate(TextFormatter.YELLOW, "probe.crop.env"));
            bar(helper, nutrients, 20, translate("probe.crop.info.nutrients", nutrients, 20), ColorUtils.rgb(0, 255, 5));
            bar(helper, humidity, 20, translate("probe.crop.info.humidity", humidity, 20), ColorUtils.rgb(93, 105, 255));
            bar(helper, env, 10, translate("probe.crop.info.env", env, 10), ColorUtils.CYAN);
            bar(helper, light, 15, translate("probe.crop.info.light", light, 15), ColorUtils.rgb(255, 255, 85));
        }
    }

    public static class CropIconProvider implements IWailaDataProvider {

        public static final CropIconProvider THIS = new CropIconProvider();

        @Nonnull
        @Override
        public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
            ItemStack icon = new ItemStack(Ic2Items.cropSeed.getItem());
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
                        icon = ItemCropSeed.generateItemStackFromValues((short) tile.getCrop().getId(), (byte) 1, (byte) 1, (byte) 1, (byte) 4);
                    }

                }
            }
            return icon;
        }

        @Nonnull
        @Override
        public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity tileEntity, NBTTagCompound tag, World world, int x, int y, int z) {
            if (tileEntity instanceof ICropTile) {
                ICropTile tile = (ICropTile) tileEntity;
                CropCard crop = tile.getCrop();
                if (crop != null) {
                    NBTTagCompound data = new NBTTagCompound();
                    data.setInteger("growthSteps", crop.maxSize());
                    data.setInteger("growthStage", tile.getSize());
                    data.setInteger("scanLevel", tile.getScanLevel());
                    tag.setTag("CropInfo", data);
                }
            }
            return tag;
        }

        // ###############################
        @Override
        public List<String> getWailaHead(ItemStack itemStack, List<String> list, IWailaDataAccessor iWailaDataAccessor, IWailaConfigHandler iWailaConfigHandler) {
            return list;
        }

        @Override
        public List<String> getWailaBody(ItemStack itemStack, List<String> list, IWailaDataAccessor iWailaDataAccessor, IWailaConfigHandler iWailaConfigHandler) {
            return list;
        }

        @Override
        public List<String> getWailaTail(ItemStack itemStack, List<String> list, IWailaDataAccessor iWailaDataAccessor, IWailaConfigHandler iWailaConfigHandler) {
            return list;
        }
    }
}

package dev.crossvas.waila.ic2.providers;

import dev.crossvas.waila.ic2.base.interfaces.IInfoProvider;
import dev.crossvas.waila.ic2.base.interfaces.IWailaHelper;
import dev.crossvas.waila.ic2.utils.ColorUtils;
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
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
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
                    text(helper, translatable("probe.crop.unknown"));
                } else {
                    text(helper, translatable("probe.crop.name", crop.displayName()).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GOLD)));
                    text(helper, translatable("probe.crop.discovered", EnumChatFormatting.AQUA + crop.discoveredBy()));
                }

                if (scanLevel < 4 && currentStage < maxStage) {
                    bar(helper, scanLevel, 4, translatable("probe.crop.info.scan", scanLevel, 4), ColorUtils.GREEN);
                } else {
                    textCentered(helper, translatable("probe.crop.growth").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.YELLOW)));
                    if (currentStage < maxStage) {
                        bar(helper, currentStage, maxStage, translatable("probe.crop.info.stage", currentStage, maxStage), ColorUtils.GREEN);
                        bar(helper, points, maxPoints, translatable("probe.crop.info.points", points, maxPoints), ColorUtils.GREEN);
                        if (canGrow) {
                            textCentered(helper, translatable("probe.crop.grow.rate", growthSpeed).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GOLD)));
                        } else {
                            textCentered(helper, translatable("probe.crop.grow.not").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
                        }
                    } else {
                        bar(helper, currentStage, maxStage, translatable("probe.crop.info.stage_done"), ColorUtils.GREEN);
                    }

                    if (scanLevel >= 4) {
                        // title
                        textCentered(helper, translatable("probe.crop.stats").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.YELLOW)));
                        bar(helper, growth, 31, translatable("probe.crop.info.growth", growth, 31), ColorUtils.CYAN);
                        bar(helper, gain, 31, translatable("probe.crop.info.gain", gain, 31), -5829955);
                        bar(helper, resistance, 31, translatable("probe.crop.info.resistance", resistance, 31), ColorUtils.rgb(255, 170, 0));

                        int stress = (crop.tier() - 1) * 4 + growth + gain + resistance;
                        int maxStress = crop.weightInfluences(tile, humidity, nutrients, env) * 5;
                        bar(helper, stress, maxStress, translatable("probe.crop.info.needs", stress, maxStress), ColorUtils.CYAN);
                    }
                }
            }

            // title
            textCentered(helper, translatable("probe.crop.storage").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.YELLOW)));
            bar(helper, fertilizer, 300, translatable("probe.crop.info.fertilizer", fertilizer, 300), ColorUtils.rgb(86, 54, 36));
            bar(helper, water, 200, translatable("probe.crop.info.water", water, 200), ColorUtils.rgb(93, 105, 255));
            bar(helper, weedex, 150, translatable("probe.crop.info.weedex", weedex, 150), ColorUtils.rgb(255, 85, 255));

            // title
            textCentered(helper, translatable("probe.crop.env").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.YELLOW)));
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

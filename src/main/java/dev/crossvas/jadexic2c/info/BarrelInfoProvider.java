package dev.crossvas.jadexic2c.info;

import dev.crossvas.jadexic2c.IHelper;
import dev.crossvas.jadexic2c.JadeIC2CPluginHandler;
import dev.crossvas.jadexic2c.utils.ColorMix;
import dev.crossvas.jadexic2c.utils.Helpers;
import ic2.core.block.misc.tiles.BarrelTileEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public enum BarrelInfoProvider implements IHelper<BlockEntity> {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (!shouldAddInfo(blockAccessor, "BarrelInfo")) {
            return;
        }

        CompoundTag tag = getData(blockAccessor, "BarrelInfo");
        if (blockAccessor.getBlockEntity() instanceof BarrelTileEntity) {
            int wheatAmount = tag.getInt("wheatAmount");
            int hopsAmount = tag.getInt("hopsAmount");
            int fluidAmount = tag.getInt("fluidAmount");

            int brewQuality = tag.getInt("brewQuality");
            int alcoholLevel = tag.getInt("alcoholLevel");
            int solidRatio = tag.getInt("solidRatio");

            int age;
            double maxValue;
            double current;
            int maxFluidCapacity = BarrelTileEntity.FLUID_CAPACITY;
            int maxPotionCapacity = BarrelTileEntity.POTION_FLUID_CAPACITY;

            DecimalFormat format = new DecimalFormat("#0.00", new DecimalFormatSymbols(Locale.US));
            FluidStack waterStack = new FluidStack(Fluids.WATER, fluidAmount);

            int brewType = tag.getInt("brewType");
            switch (brewType) {
                case 0:
                case 3:
                case 4:
                case 6:
                case 7:
                case 8:
                case 9:
                default:
                    break;
                case 1:
                    age = tag.getInt("age");
                    maxValue = 24000.0 * Math.pow(3.0, brewQuality == 4 ? 6.0 : (double) brewQuality);
                    current = age / maxValue * 100.0;

                    Helpers.text(iTooltip, getBrewType(brewType));
                    Helpers.text(iTooltip, Component.translatable("ic2.probe.barrel.status.storage.name").withStyle(ChatFormatting.YELLOW));
                    Helpers.bar(iTooltip, wheatAmount, 64, "ic2.probe.barrel.beer.wheat.name", ColorMix.YELLOW);
                    Helpers.bar(iTooltip, hopsAmount, 64, "ic2.probe.barrel.beer.hops.name", ColorMix.GREEN);
                    Helpers.addTank(iTooltip, waterStack, maxFluidCapacity);

                    Helpers.text(iTooltip, Component.translatable("ic2.probe.barrel.status.brew.name").withStyle(ChatFormatting.YELLOW));
                    Helpers.bar(iTooltip, brewQuality, 5, "ic2.probe.barrel.beer.quality." + brewQuality + ".name", ColorMix.BLUE);
                    Helpers.bar(iTooltip, alcoholLevel, 6, "ic2.probe.barrel.beer.alc." + alcoholLevel + ".name", ColorMix.GREEN);
                    Helpers.bar(iTooltip, solidRatio, 6, "ic2.probe.barrel.beer.solid." + solidRatio + ".name", ColorMix.YELLOW);
                    Helpers.barLiteral(iTooltip, age, (int) maxValue, format.format(current) + "%", ColorMix.BLUE);
                    break;
                case 2:
                    maxValue = tag.getInt("timeNeededForRum");
                    age = (int) Math.min(tag.getInt("age"), maxValue);
                    Helpers.text(iTooltip, getBrewType(brewType));
                    Helpers.text(iTooltip, Component.translatable("ic2.probe.barrel.status.brew.name").withStyle(ChatFormatting.YELLOW));
                    Helpers.bar(iTooltip, fluidAmount / 1000, 32, "ic2.probe.barrel.beer.sugar_cane.name", ColorMix.GREEN);
                    Helpers.barLiteral(iTooltip, age, (int) maxValue, format.format(Math.min(age, maxValue) * 100.0 / maxValue) + "%", ColorMix.BLUE);
                    break;
                case 5:
                    double ageWhisky = tag.getInt("age");
                    int whiskyBrewTime = tag.getInt("whiskBrewTime");

                    Helpers.text(iTooltip, getBrewType(brewType));
                    Helpers.text(iTooltip, Component.translatable("ic2.probe.barrel.status.storage.name").withStyle(ChatFormatting.YELLOW));
                    Helpers.bar(iTooltip, hopsAmount, 16, "ic2.probe.barrel.whisky.grist.name", ColorMix.GREEN);
                    Helpers.addTank(iTooltip, waterStack, maxFluidCapacity);
                    Helpers.text(iTooltip, Component.translatable("ic2.probe.barrel.status.brew.name").withStyle(ChatFormatting.YELLOW));
                    Helpers.bar(iTooltip, Math.min(brewQuality, 50), 50, "ic2.probe.barrel.whisky.years.name", ColorMix.BLUE);
                    Helpers.barLiteral(iTooltip, (int) ageWhisky, 1728000, format.format(ageWhisky / (whiskyBrewTime / 100.0)) + "%", ColorMix.BLUE);
                    break;
                case 10:
                    Helpers.text(iTooltip, getBrewType(brewType));
                    Helpers.text(iTooltip, Component.translatable("ic2.probe.barrel.status.storage.name").withStyle(ChatFormatting.YELLOW));
                    Helpers.bar(iTooltip, wheatAmount, 20, "ic2.probe.barrel.beer.redstone.name", ColorMix.RED);
                    Helpers.bar(iTooltip, hopsAmount, 20, "ic2.probe.barrel.beer.glowstone.name", ColorMix.YELLOW);
                    Helpers.addTank(iTooltip, waterStack, maxPotionCapacity);
                    Helpers.text(iTooltip, Component.translatable("ic2.probe.barrel.status.brew.name").withStyle(ChatFormatting.YELLOW));
                    int brewedPotion = tag.getInt("brewedPotion");
                    Component potionID = brewedPotion == -1 ? Component.translatable("tooltip.block.ic2.barrel.unknown") : MobEffect.byId(brewedPotion).getDisplayName();
                    Helpers.text(iTooltip, "ic2.probe.barrel.status.output.name", potionID);
                    Helpers.bar(iTooltip, brewQuality, 6, "ic2.probe.barrel.potion.quality." + brewQuality + ".name", ColorMix.BLUE);

                    age = tag.getInt("age");
                    maxValue = 5000.0 * Math.pow(3.0, brewQuality);
                    current = age / maxValue;
                    Helpers.barLiteral(iTooltip, age, (int) maxValue, format.format(current * 100.0) + "%", ColorMix.BLUE);
                    break;
            }
        }
    }

    @Override
    public ResourceLocation getUid() {
        return JadeIC2CPluginHandler.THERMOMETER_INFO;
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, ServerPlayer serverPlayer, Level level, BlockEntity blockEntity, boolean b) {
        if (blockEntity instanceof BarrelTileEntity tile) {
            CompoundTag tag = new CompoundTag();
            tag.putInt("brewType", tile.brewType);
            // 1
            tag.putInt("wheatAmount", tile.wheatAmount);
            tag.putInt("hopsAmount", tile.hopsAmount);
            tag.putInt("fluidAmount", tile.fluidAmount);
            // 2
            tag.putInt("timeNeededForRum", tile.timeNeededForRum());
            // 5
            tag.putInt("whiskBrewTime", tile.getWhiskBrewTime());
            // 10
            tag.putInt("brewedPotion", MobEffect.getId(tile.potionType));
            // common
            tag.putInt("brewQuality", tile.beerQuality);
            tag.putInt("alcoholLevel", tile.getAlcoholLevel());
            tag.putInt("solidRatio", tile.getSolidRatio());
            tag.putInt("age", tile.age);

            compoundTag.put("BarrelInfo", tag);
        }
    }

    public String getBrewType(int type) {
        return switch (type) {
            case 0 -> "ic2.probe.barrel.status.empty.name";
            case 1 -> "ic2.probe.barrel.status.beer.name";
            case 2 -> "ic2.probe.barrel.status.rum.name";
            default -> "I AM ERROR";
            case 5 -> "ic2.probe.barrel.status.whisky.name";
            case 10 -> "ic2.probe.barrel.status.potion.name";
        };
    }
}

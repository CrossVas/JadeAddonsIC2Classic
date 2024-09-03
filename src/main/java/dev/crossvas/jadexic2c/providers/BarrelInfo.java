package dev.crossvas.jadexic2c.providers;

import dev.crossvas.jadexic2c.base.interfaces.IInfoProvider;
import dev.crossvas.jadexic2c.base.interfaces.IJadeHelper;
import dev.crossvas.jadexic2c.utils.ColorUtils;
import ic2.core.block.misc.tile.TileEntityBarrel;
import ic2.core.inventory.filters.IFilter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class BarrelInfo implements IInfoProvider {

    public static final BarrelInfo THIS = new BarrelInfo();

    @Override
    public void addInfo(IJadeHelper helper, TileEntity blockEntity, EntityPlayer player) {
        if (blockEntity instanceof TileEntityBarrel) {
            TileEntityBarrel barrel = (TileEntityBarrel) blockEntity;
            // common
            int solidRatio = barrel.solidRatio;
            int brewQuality = barrel.timeRatio;
            int alcoholLevel = barrel.hopsRatio;

            // 1
            int wheatAmount = barrel.wheatCount;
            int hopsAmount = barrel.hopsCount;
            int sugarcane = barrel.boozeAmount;

            int waterAmount = barrel.boozeAmount;

            int age;
            double maxValue;
            double current;
            int brewType = barrel.type;
            DecimalFormat format = new DecimalFormat("#0.00", new DecimalFormatSymbols(Locale.ROOT));
            FluidStack waterFluid = new FluidStack(FluidRegistry.WATER, 1000);
            switch (brewType) {
                case 0:
                default:
                    break;
                case 1:
                    age = barrel.age;
                    maxValue = 24000.0 * Math.pow(3.0, brewQuality == 4 ? 6.0 : (double) brewQuality);
                    current = age / maxValue * 100.0;
                    text(helper, translatable(getBrewType(brewType)));
                    textCentered(helper, translatable("ic2.probe.barrel.status.storage.name").setStyle(new Style().setColor(TextFormatting.YELLOW)));
                    bar(helper, wheatAmount, 64, translatable("ic2.probe.barrel.beer.wheat.name", wheatAmount), ColorUtils.YELLOW);
                    bar(helper, hopsAmount, 64, translatable("ic2.probe.barrel.beer.hops.name", hopsAmount), ColorUtils.GREEN);
                    bar(helper, waterAmount, 32, translatable("ic2.barrel.info.fluid", FluidRegistry.WATER.getBlock().getLocalizedName(), waterAmount, 32 + "k"), ColorUtils.brighter(ColorUtils.BLUE));

                    String status = format.format(current) + "%";
                    int brewColor = 0;
                    switch (brewQuality) {
                        case 1:
                        case 2:
                        case 3:
                        case 4: brewColor = ColorUtils.GREEN; break;
                        case 5: {
                            brewColor = ColorUtils.RED;
                            status = "Wasted!";
                        } break;
                    }
                    textCentered(helper, translatable("ic2.probe.barrel.status.brew.name").setStyle(new Style().setColor(TextFormatting.YELLOW)));
                    text(helper, translatable("ic2.probe.barrel.beer.quality." + brewQuality + ".name"));
                    text(helper, translatable("ic2.probe.barrel.beer.alc." + alcoholLevel + ".name"));
                    text(helper, translatable("ic2.probe.barrel.beer.solid." + solidRatio + ".name"));
                    bar(helper, age, (int) maxValue, new TextComponentString(status), brewColor);
                    break;
            }
        }
    }

    public String getBrewType(int type) {
        switch (type) {
            case 0: return "ic2.probe.barrel.status.empty.name";
            case 1: return "ic2.probe.barrel.status.beer.name";
            case 2: return "ic2.probe.barrel.status.rum.name";
            case 5: return "ic2.probe.barrel.status.whisky.name";
            case 10: return "ic2.probe.barrel.status.potion.name";
            default: return "I AM ERROR";
        }
    }

    @Override
    public IFilter getFilter() {
        return THERMOMETER;
    }
}

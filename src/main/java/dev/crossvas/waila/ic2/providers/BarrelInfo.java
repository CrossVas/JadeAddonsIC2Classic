package dev.crossvas.waila.ic2.providers;

import dev.crossvas.waila.ic2.base.interfaces.IInfoProvider;
import dev.crossvas.waila.ic2.base.interfaces.IWailaHelper;
import dev.crossvas.waila.ic2.utils.ColorUtils;
import dev.crossvas.waila.ic2.utils.TextFormatter;
import ic2.core.block.TileEntityBarrel;
import ic2.core.block.inventory.IItemTransporter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.Tuple;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class BarrelInfo implements IInfoProvider {

    public static final BarrelInfo THIS = new BarrelInfo();

    @Override
    public void addInfo(IWailaHelper helper, TileEntity blockEntity, EntityPlayer player) {
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
            double current = 0;
            int brewType = barrel.type;

            FluidStack waterFluid = new FluidStack(FluidRegistry.WATER, waterAmount);
            DecimalFormat format = new DecimalFormat("#0.00", new DecimalFormatSymbols(Locale.ROOT));
            Tuple STATUS;
                    switch (brewType) {
                case 0:
                default:
                    break;
                case 1:
                    age = barrel.age;
                    maxValue = 24000.0 * Math.pow(3.0, brewQuality == 4 ? 6.0 : (double) brewQuality);
                    current = age / maxValue * 100.0;
                    text(helper, translate(getBrewType(brewType)));
                    textCentered(helper, translate(TextFormatter.YELLOW, "probe.barrel.status.storage.name"));
                    bar(helper, wheatAmount, 64, translate("probe.barrel.beer.wheat.name", wheatAmount), ColorUtils.YELLOW);
                    bar(helper, hopsAmount, 64, translate("probe.barrel.beer.hops.name", hopsAmount), ColorUtils.GREEN);
                    bar(helper, waterAmount, 32, translate("probe.info.fluid", waterFluid.getLocalizedName(), waterAmount, 32 + "k"), -1, waterFluid.getFluid().getName());

                    textCentered(helper, translate(TextFormatter.YELLOW, "probe.barrel.status.brew.name"));
                    text(helper, translate("probe.barrel.beer.quality." + brewQuality + ".name"));
                    text(helper, translate("probe.barrel.beer.alc." + alcoholLevel + ".name"));
                    text(helper, translate("probe.barrel.beer.solid." + solidRatio + ".name"));
                    STATUS = getStatus(current, brewQuality);
                    bar(helper, age, (int) maxValue, new ChatComponentText((String) STATUS.getFirst()), (int) STATUS.getSecond());
                    break;
                case 2:
                    maxValue = barrel.timeNedForRum(sugarcane);
                    age = (int) Math.min(barrel.age, maxValue);
                    text(helper, translate(getBrewType(brewType)));
                    textCentered(helper, translate(TextFormatter.YELLOW, "probe.barrel.status.brew.name"));
                    bar(helper, sugarcane, 32, translate("probe.barrel.beer.sugar_cane.name", sugarcane), ColorUtils.GREEN);
                    bar(helper, age, (int) maxValue, new ChatComponentText(format.format(Math.min(age, maxValue) * 100.0 / maxValue) + "%"), ColorUtils.PROGRESS);
                    break;
            }
        }
    }

    public Tuple getStatus(double current, int beerQuality) {
        DecimalFormat format = new DecimalFormat("#0.00", new DecimalFormatSymbols(Locale.ROOT));
        String status = format.format(current) + "%";
        int brewColor = 0;
        switch (beerQuality) {
            case 1:
            case 2:
            case 3:
            case 4: brewColor = ColorUtils.GREEN; break;
            case 5: {
                brewColor = ColorUtils.RED;
                status = "Wasted!";
            } break;
        }
        return new Tuple(status, brewColor);
    }

    public String getBrewType(int type) {
        switch (type) {
            case 0: return "probe.barrel.status.empty.name";
            case 1: return "probe.barrel.status.beer.name";
            case 2: return "probe.barrel.status.rum.name";
            default: return "I AM ERROR";
        }
    }

    @Override
    public IItemTransporter.IFilter getFilter() {
        return TREETAP;
    }
}

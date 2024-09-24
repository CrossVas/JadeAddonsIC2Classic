package dev.crossvas.waila.ic2.base;

import dev.crossvas.waila.ic2.base.elements.CommonBarElement;
import dev.crossvas.waila.ic2.base.interfaces.IInfoProvider;
import dev.crossvas.waila.ic2.base.interfaces.IWailaHelper;
import dev.crossvas.waila.ic2.providers.EUStorageInfo;
import dev.crossvas.waila.ic2.providers.WrenchableInfo;
import dev.crossvas.waila.ic2.utils.Formatter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

public class WailaCommonHandler {

    public static List<IInfoProvider> INFO_PROVIDERS = new ArrayList<>();
    public static WeakHashMap<String, String> MAPPED_FLUIDS = new WeakHashMap<>();

    static {
        INFO_PROVIDERS.add(EUStorageInfo.THIS);

        INFO_PROVIDERS.add(WrenchableInfo.THIS);
    }

    public static void addInfo(IWailaHelper helper, TileEntity blockEntity, EntityPlayer player) {
        if (blockEntity != null) {
            INFO_PROVIDERS.forEach(infoProvider -> {
                if (infoProvider.canHandle(player)) {
                    infoProvider.addInfo(helper, blockEntity, player);
                }
            });
        }
    }

    public static void addTankInfo(IWailaHelper helper, TileEntity blockEntity) {
        if (blockEntity instanceof IFluidHandler) {
            loadTankData(helper, (IFluidHandler) blockEntity);
        } /*else {
            IFluidHandler fluidHandler = blockEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);

            if (fluidHandler != null) {
                loadTankData(helper, fluidHandler);
            }
        }*/
    }

    public static void loadTankData(IWailaHelper helper, IFluidHandler fluidHandler) {
//        for (int i = 0; i < fluidHandler.getTankProperties().length; i++) {
//            FluidStack fluid = fluidHandler.getTankProperties()[i].getContents();
//            int capacity = fluidHandler.getTankProperties()[i].getCapacity();
//            if (fluid != null) {
//                if (fluid.amount > 0) {
//                    loadTankInfo(helper, fluid, capacity);
//                }
//            }
//        }
        FluidTankInfo[] tanks = fluidHandler.getTankInfo(ForgeDirection.UNKNOWN);
        for (int i = 0; i < tanks.length; i++) {
            FluidStack fluid = tanks[i].fluid;
            int capacity = tanks[i].capacity;
            if (fluid != null) {
                if (fluid.amount > 0) {
                    loadTankInfo(helper, fluid, capacity);
                }
            }
        }
    }

    public static void loadTankInfo(IWailaHelper helper, FluidStack fluidStack, int capacity) {
        MAPPED_FLUIDS.put(fluidStack.getFluid().getName(), fluidStack.getFluid().getIcon().toString());
        int color = fluidStack.getUnlocalizedName().contains("lava") ? -29925 : fluidStack.getFluid().getColor(fluidStack) | -16777216;
        helper.add(new CommonBarElement(fluidStack.amount, capacity,
            new ChatComponentTranslation("probe.info.fluid", fluidStack.getLocalizedName(),
                Formatter.formatNumber(fluidStack.amount, String.valueOf(fluidStack.amount).length() - 1),
                Formatter.formatNumber(capacity, String.valueOf(capacity).length() - 1)),
            color, fluidStack.getFluid().getName()));
    }
}

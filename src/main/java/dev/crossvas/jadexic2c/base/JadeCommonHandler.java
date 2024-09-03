package dev.crossvas.jadexic2c.base;

import dev.crossvas.jadexic2c.base.elements.CommonBarElement;
import dev.crossvas.jadexic2c.base.interfaces.IInfoProvider;
import dev.crossvas.jadexic2c.base.interfaces.IJadeHelper;
import dev.crossvas.jadexic2c.providers.*;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import java.util.List;

public class JadeCommonHandler {

    public static List<IInfoProvider> INFO_PROVIDERS = new ObjectArrayList<>();

    static {
        INFO_PROVIDERS.add(EUStorageInfo.THIS);
        INFO_PROVIDERS.add(BaseEnergyStorageInfo.THIS);
        INFO_PROVIDERS.add(AdjustableTransformerInfo.THIS);
        INFO_PROVIDERS.add(BarrelInfo.THIS);
        INFO_PROVIDERS.add(WrenchableInfo.THIS);
    }

    public static void addInfo(IJadeHelper helper, TileEntity blockEntity, EntityPlayer player) {
        if (blockEntity != null) {
            INFO_PROVIDERS.forEach(infoProvider -> {
                if (infoProvider.canHandle(player)) {
                    infoProvider.addInfo(helper, blockEntity, player);
                }
            });
        }
    }

    public static void addTankInfo(IJadeHelper helper, TileEntity blockEntity) {
//        TANK_REMOVAL.add(blockEntity);
        if (blockEntity instanceof IFluidHandler) {
            loadTankData(helper, (IFluidHandler) blockEntity);
        } else {
            IFluidHandler fluidHandler = blockEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
            if (fluidHandler != null) {
                loadTankData(helper, fluidHandler);
            }
        }
    }

    public static void loadTankData(IJadeHelper helper, IFluidHandler fluidHandler) {
        for (int i = 0; i < fluidHandler.getTankProperties().length; i++) {
            FluidStack fluid = fluidHandler.getTankProperties()[i].getContents();
            if (fluid != null) {
                if (fluid.amount > 0) {
                    int color = fluid.getUnlocalizedName().contains("lava") ? -29925 : fluid.getFluid().getColor(fluid) | -16777216;
                    helper.add(new CommonBarElement(fluid.amount, fluidHandler.getTankProperties()[i].getCapacity(), new TextComponentString(fluid.getLocalizedName()), color, fluid.getFluid().getStill().toString()));
                }
            }
        }
    }
}

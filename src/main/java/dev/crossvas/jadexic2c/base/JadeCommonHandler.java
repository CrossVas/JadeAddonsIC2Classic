package dev.crossvas.jadexic2c.base;

import dev.crossvas.jadexic2c.base.interfaces.IInfoProvider;
import dev.crossvas.jadexic2c.base.interfaces.IJadeHelper;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import java.util.List;

public class JadeCommonHandler {

    public static List<IInfoProvider> INFO_PROVIDERS = new ObjectArrayList<>();

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
            for (EnumFacing facing : EnumFacing.values()) {
                IFluidHandler fluidHandler = blockEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing);
                if (fluidHandler != null) {
                    loadTankData(helper, fluidHandler);
                }
            }
        }
    }

    public static void loadTankData(IJadeHelper helper, IFluidHandler fluidHandler) {
        for (int i = 0; i < fluidHandler.getTankProperties().length; i++) {
            FluidStack fluid = fluidHandler.getTankProperties()[i].getContents();
            if (fluid.amount > 0) {
//                helper.add(new CommonFluidBarElement(fluid, fluidHandler.getTankProperties()[i], false));
            }
        }
    }
}

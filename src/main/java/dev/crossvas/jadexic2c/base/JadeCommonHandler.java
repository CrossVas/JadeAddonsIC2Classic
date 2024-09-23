package dev.crossvas.jadexic2c.base;

import dev.crossvas.jadexic2c.base.elements.CommonBarElement;
import dev.crossvas.jadexic2c.base.interfaces.IInfoProvider;
import dev.crossvas.jadexic2c.base.interfaces.IJadeHelper;
import dev.crossvas.jadexic2c.providers.*;
import dev.crossvas.jadexic2c.utils.Formatter;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import java.util.List;
import java.util.WeakHashMap;

public class JadeCommonHandler {

    public static List<IInfoProvider> INFO_PROVIDERS = new ObjectArrayList<>();
    public static WeakHashMap<String, String> MAPPED_FLUIDS = new WeakHashMap<>();

    static {
        INFO_PROVIDERS.add(EUStorageInfo.THIS);
        INFO_PROVIDERS.add(BaseEnergyStorageInfo.THIS);
        INFO_PROVIDERS.add(AdjustableTransformerInfo.THIS);
        INFO_PROVIDERS.add(BarrelInfo.THIS);
        INFO_PROVIDERS.add(BaseGeneratorInfo.THIS);
        INFO_PROVIDERS.add(BaseMachineInfo.THIS);
        INFO_PROVIDERS.add(CableInfo.THIS);
        INFO_PROVIDERS.add(ChargePadInfo.THIS);
        INFO_PROVIDERS.add(CropLibraryInfo.THIS);
        INFO_PROVIDERS.add(ElectricBlockInfo.THIS);
        INFO_PROVIDERS.add(ElectrolyzerInfo.THIS);
        INFO_PROVIDERS.add(FluidOMatInfo.THIS);
        INFO_PROVIDERS.add(FuelBoilerInfo.THIS);
        INFO_PROVIDERS.add(LuminatorInfo.THIS);
        INFO_PROVIDERS.add(MinerInfo.THIS);
        INFO_PROVIDERS.add(NuclearInfo.THIS);

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
            int capacity = fluidHandler.getTankProperties()[i].getCapacity();
            if (fluid != null) {
                if (fluid.amount > 0) {
                    loadTankInfo(helper, fluid, capacity);
                }
            }
        }
    }

    public static void loadTankInfo(IJadeHelper helper, FluidStack fluidStack, int capacity) {
        MAPPED_FLUIDS.put(fluidStack.getFluid().getName(), fluidStack.getFluid().getStill().toString());
        int color = fluidStack.getUnlocalizedName().contains("lava") ? -29925 : fluidStack.getFluid().getColor(fluidStack) | -16777216;
        helper.add(new CommonBarElement(fluidStack.amount, capacity,
                new TextComponentTranslation("probe.info.fluid", fluidStack.getLocalizedName(),
                        Formatter.formatNumber(fluidStack.amount, String.valueOf(fluidStack.amount).length() - 1),
                        Formatter.formatNumber(capacity, String.valueOf(capacity).length() - 1)),
                color, fluidStack.getFluid().getName()));
    }
}

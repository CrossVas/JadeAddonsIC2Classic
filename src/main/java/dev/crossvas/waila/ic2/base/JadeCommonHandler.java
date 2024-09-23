package dev.crossvas.waila.ic2.base;

import dev.crossvas.waila.ic2.base.elements.CommonFluidBarElement;
import dev.crossvas.waila.ic2.base.interfaces.IInfoProvider;
import dev.crossvas.waila.ic2.base.interfaces.IJadeHelper;
import dev.crossvas.jadexic2c.providers.*;
import dev.crossvas.waila.ic2.providers.*;
import dev.crossvas.waila.ic2.providers.expansions.FluidExpansionInfo;
import dev.crossvas.waila.ic2.providers.expansions.MemoryExpansionInfo;
import dev.crossvas.waila.ic2.providers.expansions.StorageExpansionInfo;
import dev.crossvas.waila.ic2.providers.expansions.UUMExpansionInfo;
import dev.crossvas.jadexic2c.providers.transport.*;
import dev.crossvas.waila.ic2.providers.transport.*;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import java.util.List;

public class JadeCommonHandler {

    public static List<IInfoProvider> INFO_PROVIDERS = new ObjectArrayList<>();
    public static final List<BlockEntity> TANK_REMOVAL = new ObjectArrayList<>();

    static {
        INFO_PROVIDERS.add(EUStorageInfo.THIS);
        INFO_PROVIDERS.add(AdjustableTransformerInfo.THIS);
        INFO_PROVIDERS.add(BarrelInfo.THIS);
        INFO_PROVIDERS.add(BaseEnergyStorageInfo.THIS);
        INFO_PROVIDERS.add(BaseGeneratorInfo.THIS);
        INFO_PROVIDERS.add(BaseMachineInfo.THIS);
        INFO_PROVIDERS.add(BaseMultiBlockMachineInfo.THIS);
        INFO_PROVIDERS.add(BaseTeleporterInfo.THIS);
        INFO_PROVIDERS.add(BasicPipeInfo.THIS);
        INFO_PROVIDERS.add(BatteryStationInfo.THIS);
        INFO_PROVIDERS.add(CableInfo.THIS);
        INFO_PROVIDERS.add(ChargePadInfo.THIS);
        INFO_PROVIDERS.add(ChargingBenchInfo.THIS);
        INFO_PROVIDERS.add(ColorFilterTubeInfo.THIS);
        INFO_PROVIDERS.add(CropInfo.THIS);
        INFO_PROVIDERS.add(CropLibraryInfo.THIS);
        INFO_PROVIDERS.add(DirectionalTubeInfo.THIS);
        INFO_PROVIDERS.add(DynamicTankInfo.THIS);
        INFO_PROVIDERS.add(ElectricBlockInfo.THIS);
        INFO_PROVIDERS.add(ElectricFisherInfo.THIS);
        INFO_PROVIDERS.add(ElectricLoaderInfo.THIS);
        INFO_PROVIDERS.add(ElectricUnloaderInfo.THIS);
        INFO_PROVIDERS.add(ElectricWoodGassifierInfo.THIS);
        INFO_PROVIDERS.add(ElectrolyzerInfo.THIS);
        INFO_PROVIDERS.add(ExtractionTubeInfo.THIS);
        INFO_PROVIDERS.add(FilterTubeInfo.THIS);
        INFO_PROVIDERS.add(FilteredExtractionTubeInfo.THIS);
        INFO_PROVIDERS.add(FluidExpansionInfo.THIS);
        INFO_PROVIDERS.add(FluidOMatInfo.THIS);
        INFO_PROVIDERS.add(FuelBoilerInfo.THIS);
        INFO_PROVIDERS.add(InsertionTubeInfo.THIS);
        INFO_PROVIDERS.add(LimiterTubeInfo.THIS);
        INFO_PROVIDERS.add(LuminatorInfo.THIS);
        INFO_PROVIDERS.add(MemoryExpansionInfo.THIS);
        INFO_PROVIDERS.add(MinerInfo.THIS);
        INFO_PROVIDERS.add(NuclearInfo.THIS);
        INFO_PROVIDERS.add(OceanGenInfo.THIS);
        INFO_PROVIDERS.add(OreScannerInfo.THIS);
        INFO_PROVIDERS.add(PersonalInfo.THIS);
        INFO_PROVIDERS.add(PickupTubeInfo.THIS);
        INFO_PROVIDERS.add(PipePumpInfo.THIS);
        INFO_PROVIDERS.add(PlasmafierInfo.THIS);
        INFO_PROVIDERS.add(ProviderTubeInfo.THIS);
        INFO_PROVIDERS.add(PumpInfo.THIS);
        INFO_PROVIDERS.add(PushingValveInfo.THIS);
        INFO_PROVIDERS.add(RangedPumpInfo.THIS);
        INFO_PROVIDERS.add(RedirectorMasterInfo.THIS);
        INFO_PROVIDERS.add(RedirectorSlaveInfo.THIS);
        INFO_PROVIDERS.add(RequestTubeInfo.THIS);
        INFO_PROVIDERS.add(RoundRobinTubeInfo.THIS);
        INFO_PROVIDERS.add(SolarPanelInfo.THIS);
        INFO_PROVIDERS.add(StackingTubeInfo.THIS);
        INFO_PROVIDERS.add(SteamTunnelInfo.THIS);
        INFO_PROVIDERS.add(SteamTurbineInfo.THIS);
        INFO_PROVIDERS.add(StoneMachineInfo.THIS);
        INFO_PROVIDERS.add(StorageExpansionInfo.THIS);
        INFO_PROVIDERS.add(TeleportTubeInfo.THIS);
        INFO_PROVIDERS.add(TeleporterInfo.THIS);
        INFO_PROVIDERS.add(ThermonuclearReactorInfo.THIS);
        INFO_PROVIDERS.add(TransformerInfo.THIS);
        INFO_PROVIDERS.add(UUMExpansionInfo.THIS);
        INFO_PROVIDERS.add(UraniumEnricherInfo.THIS);
        INFO_PROVIDERS.add(VillagerOMatInfo.THIS);
        INFO_PROVIDERS.add(WaveGenInfo.THIS);
        INFO_PROVIDERS.add(WindmillGenInfo.THIS);
        INFO_PROVIDERS.add(BasicTubeInfo.THIS);
    }

    public static void addInfo(IJadeHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity != null) {
            INFO_PROVIDERS.forEach(infoProvider -> {
                if (infoProvider.canHandle(player)) {
                    infoProvider.addInfo(helper, blockEntity, player);
                }
            });
        }
    }

    public static void addTankInfo(IJadeHelper helper, BlockEntity blockEntity) {
        TANK_REMOVAL.add(blockEntity);
        if (blockEntity instanceof IFluidHandler fluidHandler) {
            loadTankData(helper, fluidHandler);
        } else {
            blockEntity.getCapability(ForgeCapabilities.FLUID_HANDLER).ifPresent(handler -> loadTankData(helper, handler));
        }
    }

    public static void loadTankData(IJadeHelper helper, IFluidHandler fluidHandler) {
        for (int i = 0; i < fluidHandler.getTanks(); i++) {
            FluidStack fluid = fluidHandler.getFluidInTank(i);
            if (fluid.getAmount() > 0) {
                helper.add(new CommonFluidBarElement(fluid, fluidHandler.getTankCapacity(i), false));
            }
        }
    }
}

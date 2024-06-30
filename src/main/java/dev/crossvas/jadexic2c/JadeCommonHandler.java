package dev.crossvas.jadexic2c;

import dev.crossvas.jadexic2c.base.IInfoProvider;
import dev.crossvas.jadexic2c.base.IJadeHelper;
import dev.crossvas.jadexic2c.providers.*;
import ic2.core.block.base.features.multiblock.IStructureListener;
import ic2.core.platform.events.StructureManager;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import java.util.List;
import java.util.Map;

public class JadeCommonHandler {

    public static List<IInfoProvider> INFO_PROVIDERS = new ObjectArrayList<>();
    public static final List<Block> TANK_REMOVAL = new ObjectArrayList<>();
    public static Map<BlockEntity, IInfoProvider> MAPPED_INFO = new Object2ObjectLinkedOpenHashMap<>();

    static {
        INFO_PROVIDERS.add(EUStorageInfo.THIS);
        INFO_PROVIDERS.add(CableInfo.THIS);
        INFO_PROVIDERS.add(SolarPanelInfo.THIS);
        INFO_PROVIDERS.add(SteamTunnelInfo.THIS);
        INFO_PROVIDERS.add(SteamTurbineInfo.THIS);
        INFO_PROVIDERS.add(StoneMachineInfo.THIS);
        INFO_PROVIDERS.add(TeleporterInfo.THIS);
        INFO_PROVIDERS.add(ThermonuclearReactorInfo.THIS);
        INFO_PROVIDERS.add(TransformerInfo.THIS);
        INFO_PROVIDERS.add(UraniumEnricherInfo.THIS);
        INFO_PROVIDERS.add(VillagerOMatInfo.THIS);
        INFO_PROVIDERS.add(WaveGenInfo.THIS);
        INFO_PROVIDERS.add(WindmillGenInfo.THIS);
        INFO_PROVIDERS.add(WrenchInfo.THIS);
    }

    public static BlockEntity getMultiBlockController(Level level, BlockPos pos) {
        IStructureListener structureListener = StructureManager.INSTANCE.getListener(level, pos);
        if (structureListener instanceof BlockEntity master) {
            return master;
        } else {
            return level.getBlockEntity(pos);
        }
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
        TANK_REMOVAL.add(blockEntity.getBlockState().getBlock());
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
                helper.addFluidElement(fluid, fluidHandler.getTankCapacity(i));
            }
        }
    }
}

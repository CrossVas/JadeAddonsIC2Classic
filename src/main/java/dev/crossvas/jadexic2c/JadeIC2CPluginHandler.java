package dev.crossvas.jadexic2c;

import dev.crossvas.jadexic2c.info.*;
import dev.crossvas.jadexic2c.info.pump.PumpInfoProvider;
import dev.crossvas.jadexic2c.info.pump.RangedPumpInfoProvider;
import dev.crossvas.jadexic2c.utils.removals.ModNameRender;
import dev.crossvas.jadexic2c.utils.removals.TankRender;
import ic2.core.block.base.blocks.BaseTexturedBlock;
import ic2.core.block.base.blocks.ValveBlock;
import ic2.core.block.base.features.multiblock.IMultiBlockClickable;
import ic2.core.block.base.tiles.BaseElectricTileEntity;
import ic2.core.block.base.tiles.BaseInventoryTileEntity;
import ic2.core.block.base.tiles.BaseTileEntity;
import ic2.core.block.base.tiles.impls.*;
import ic2.core.block.base.tiles.impls.machine.single.BaseMachineTileEntity;
import ic2.core.block.cables.CableBlock;
import ic2.core.block.cables.CableTileEntity;
import ic2.core.block.cables.luminator.ConstructionLightBlock;
import ic2.core.block.cables.luminator.ConstructionLightTileEntity;
import ic2.core.block.cables.luminator.LuminatorBlock;
import ic2.core.block.cables.luminator.LuminatorTileEntity;
import ic2.core.block.cables.mointor.MonitorBlock;
import ic2.core.block.crops.CropBlock;
import ic2.core.block.crops.CropTileEntity;
import ic2.core.block.generators.*;
import ic2.core.block.generators.tiles.*;
import ic2.core.block.machines.BaseMachineBlock;
import ic2.core.block.machines.NoStateMachineBlock;
import ic2.core.block.machines.customBlocks.TeleporterBlock;
import ic2.core.block.machines.customBlocks.VillagerOMatBlock;
import ic2.core.block.machines.tiles.ev.ElectricFisherTileEntity;
import ic2.core.block.machines.tiles.ev.PlasmafierTileEntity;
import ic2.core.block.machines.tiles.hv.OreScannerTileEntity;
import ic2.core.block.machines.tiles.hv.TeleporterTileEntity;
import ic2.core.block.machines.tiles.hv.UraniumEnchricherTileEntity;
import ic2.core.block.machines.tiles.hv.VillagerOMatTileEntity;
import ic2.core.block.machines.tiles.lv.MinerTileEntity;
import ic2.core.block.machines.tiles.lv.PumpTileEntity;
import ic2.core.block.machines.tiles.lv.WoodGassifierTileEntity;
import ic2.core.block.machines.tiles.mv.RangedPumpTileEntity;
import ic2.core.block.misc.BarrelBlock;
import ic2.core.block.misc.TreeTapAndBucketBlock;
import ic2.core.block.misc.tiles.BarrelTileEntity;
import ic2.core.block.multi.BaseMultiBlock;
import ic2.core.block.multi.TurbineMultiBlock;
import ic2.core.block.personal.PersonalBlock;
import ic2.core.block.personal.tile.FluidOMatTileEntity;
import ic2.core.block.storage.*;
import ic2.core.block.storage.tiles.RedirectorMasterTileEntity;
import ic2.core.block.storage.tiles.RedirectorSlaveTileEntity;
import ic2.core.block.storage.tiles.TankBlock;
import ic2.core.block.storage.tiles.transformer.AdjustableTransformerTileEntity;
import ic2.core.block.transport.fluid.PipeBlock;
import ic2.core.block.transport.fluid.PumpBlock;
import ic2.core.block.transport.fluid.tiles.ElectricPipePumpTileEntity;
import ic2.core.block.transport.item.TubeBlock;
import ic2.core.block.transport.item.TubeTileEntity;
import ic2.core.platform.events.MultiBlockManager;
import ic2.core.platform.registries.IC2Blocks;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;
import snownee.jade.api.*;
import snownee.jade.api.callback.JadeRayTraceCallback;

import java.util.stream.Stream;

@WailaPlugin("ic2")
public class JadeIC2CPluginHandler implements IWailaPlugin {

    public static final ResourceLocation EU_READER_INFO = new ResourceLocation("ic2", "eu_reader");
    public static final ResourceLocation THERMOMETER_INFO = new ResourceLocation("ic2", "thermometer");
    public static final ResourceLocation CROP_INFO = new ResourceLocation("ic2", "crop");
    public static final ResourceLocation EU_STORAGE_INFO = new ResourceLocation("ic2", "eu_storage_info");

    /**
     * {@link ic2.core.platform.registries.IC2Blocks}
     * for quick access to IC2Blocks info
     * */

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(WrenchableInfoProvider.INSTANCE, Block.class);
        registration.registerBlockComponent(CropInfoProvider.INSTANCE, CropBlock.class);
        registration.registerBlockComponent(BarrelInfoProvider.INSTANCE, BarrelBlock.class);

        registerProvidersForBlock(registration, BaseMachineBlock.class,
                BaseMachineInfoProvider.INSTANCE,
                MinerInfoProvider.INSTANCE,
                OreScannerInfoProvider.INSTANCE,
                PlasmafierInfoProvider.INSTANCE,
                PumpInfoProvider.INSTANCE,
                RangedPumpInfoProvider.INSTANCE,
                UraniumEnricherInfoProvider.INSTANCE,
                ElectricWoodGassifierInfoProvider.INSTANCE,
                StoneMachineInfoProvider.INSTANCE,
                ElectrolyzerInfoProvider.INSTANCE,
                CropLibraryInfoProvider.INSTANCE,
                BaseTeleporterInfoProvider.INSTANCE
        );

        registerProvidersForBlock(registration, BaseGeneratorBlock.class,
                SolarPanelInfoProvider.INSTANCE,
                WaveGenInfoProvider.INSTANCE,
                NuclearReactorInfoProvider.INSTANCE,
                OceanGenInfoProvider.INSTANCE,
                WindmillGenInfoProvider.INSTANCE,
                SteamTurbineInfoProvider.INSTANCE
        );

        registerProvidersForBlock(registration, TransformerBlock.class,
                TransformerInfoProvider.INSTANCE,
                AdjustableTransformerInfoProvider.INSTANCE);

        registerProvidersForBlock(registration, BaseLoaderBlock.class,
                ElectricLoaderInfoProvider.INSTANCE,
                ElectricUnloaderInfoProvider.INSTANCE
        );

        registerProvidersForBlock(registration, RedirectorBlock.class,
                RedirectorMasterInfoProvider.INSTANCE,
                RedirectorSlaveInfoProvider.INSTANCE
        );

        registerBlocks(registration, ElectricBlockInfoProvider.INSTANCE,
                BaseMachineBlock.class,
                MonitorBlock.class,
                NoStateMachineBlock.class);

        registerBlocks(registration, BaseGeneratorInfoProvider.INSTANCE,
                BaseGeneratorBlock.class,
                ThermalGeneratorBlock.class);

        registerBlocks(registration, LuminatorInfoProvider.INSTANCE, LuminatorBlock.class, ConstructionLightBlock.class);

        registerBlocks(registration, BaseMultiBlockMachineInfoProvider.INSTANCE,
                BaseMachineBlock.class,
                BaseMultiBlock.class
        );

        registerBlocks(registration, SteamTunnelInfoProvider.INSTANCE,
                TurbineBlock.class,
                TurbineMultiBlock.class,
                ValveBlock.class
        );

        registerBlocks(registration, PipePumpInfoProvider.INSTANCE,
                PipeBlock.class,
                PumpBlock.class
        );

        registerBlocks(registration, FuelBoilerInfoProvider.INSTANCE,
                FuelBoilerBlock.class,
                BaseMultiBlock.class);

        registerBlocks(registration, ThermonuclearReactorInfoProvider.INSTANCE,
                NoStateMachineBlock.class,
                ValveBlock.class
        );

        registerBlocks(registration, DynamicTankInfoProvider.INSTANCE,
                TankBlock.class,
                ValveBlock.class
        );

        registration.registerBlockComponent(NuclearReactorInfoProvider.INSTANCE, ReactorChamberBlock.class);
        registration.registerBlockComponent(BaseEnergyStorageInfoProvider.INSTANCE, EnergyStorageBlock.class);
        registration.registerBlockComponent(CableInfoProvider.INSTANCE, CableBlock.class);
        registration.registerBlockComponent(BatteryStationInfoProvider.INSTANCE, BaseTexturedBlock.class);
        registration.registerBlockComponent(ChargePadInfoProvider.INSTANCE, ChargePadBlock.class);
        registration.registerBlockComponent(ChargingBenchInfoProvider.INSTANCE, ChargingBenchBlock.class);
        registration.registerBlockComponent(TeleporterInfoProvider.INSTANCE, TeleporterBlock.class);
        registration.registerBlockComponent(ElectricFisherInfoProvider.INSTANCE, NoStateMachineBlock.class);
        registration.registerBlockComponent(PushingValveInfoProvider.INSTANCE, ValveBlock.class);
        registration.registerBlockComponent(VillagerOMatInfoProvider.INSTANCE, VillagerOMatBlock.class);
        registration.registerBlockComponent(FluidOMatInfoProvider.INSTANCE, PersonalBlock.class);
        registration.registerBlockComponent(TreetapAndBucketInfoProvider.INSTANCE, TreeTapAndBucketBlock.class);
        registration.registerBlockComponent(TubeInfoProvider.INSTANCE, TubeBlock.class);
        registration.usePickedResult(IC2Blocks.COLOSSAL_BASE);
        registration.usePickedResult(IC2Blocks.PRESSURE_ALLOY_FURNACE_MULTIBLOCK);
        registration.usePickedResult(IC2Blocks.FUEL_BOILER_MULTIBLOCK);

        // TODO: rethink this
        registration.addRayTraceCallback(new JadeRayTraceCallback() {
            @Override
            public @Nullable Accessor<?> onRayTrace(HitResult hitResult, @Nullable Accessor<?> accessor, @Nullable Accessor<?> accessor1) {
                if (accessor instanceof BlockAccessor target) {
                    Block targetBlock = target.getBlock();
                    if (targetBlock instanceof TurbineMultiBlock) {
                        BlockState targetState = target.getBlockState();
                        if (targetState.getValue(TurbineMultiBlock.FORMED) == 3) {
                            return registration.blockAccessor().from(target).blockState(IC2Blocks.STEAM_TUNNEL.defaultBlockState()).build();
                        }
                    }
                }
                return accessor;
            }
        });

        // add tank info for MultiTank Casing
        registration.addRayTraceCallback((hitResult, accessor, originalAccessor) -> {
            if (accessor instanceof BlockAccessor blockAccessor) {
                Level level = blockAccessor.getLevel();
                BlockPos pos = blockAccessor.getPosition();
                IMultiBlockClickable multi = MultiBlockManager.INSTANCE.getMultiBlock(level, pos);
                if (multi != null) {
                    BlockPos originPos = multi.getOrigin();
                    BlockEntity origin = level.getBlockEntity(originPos);
                    BlockHitResult blockHitResult = blockAccessor.getHitResult();
                    return registration.blockAccessor()
                            .from(blockAccessor)
                            .hit(blockHitResult.withPosition(originPos))
                            .blockState(level.getBlockState(originPos))
                            .blockEntity(origin)
                            .fakeBlock(new ItemStack(level.getBlockState(originPos).getBlock()))
                            .build();
                }
            }
            return accessor;
        });

        // remove builtin fluid tank render for IC2 FluidHandlers. We use our own style.
        registration.registerBlockComponent(TankRender.INSTANCE, Block.class);
        registration.registerBlockComponent(ModNameRender.ModNameRemover.INSTANCE, Block.class);
        registration.registerBlockComponent(ModNameRender.ModNameRelocator.INSTANCE, Block.class);
        registration.registerBlockComponent(EUStorageInfoProvider.INSTANCE, Block.class);
    }

    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerBlockDataProvider(CropInfoProvider.INSTANCE, CropTileEntity.class);
        registration.registerBlockDataProvider(BarrelInfoProvider.INSTANCE, BarrelTileEntity.class);
        registration.registerBlockDataProvider(BaseMachineInfoProvider.INSTANCE, BaseMachineTileEntity.class);
        registration.registerBlockDataProvider(ElectricBlockInfoProvider.INSTANCE, BaseElectricTileEntity.class);
        registration.registerBlockDataProvider(MinerInfoProvider.INSTANCE, MinerTileEntity.class);
        registration.registerBlockDataProvider(OreScannerInfoProvider.INSTANCE, OreScannerTileEntity.class);
        registration.registerBlockDataProvider(PlasmafierInfoProvider.INSTANCE, PlasmafierTileEntity.class);
        registration.registerBlockDataProvider(PumpInfoProvider.INSTANCE, PumpTileEntity.class);
        registration.registerBlockDataProvider(RangedPumpInfoProvider.INSTANCE, RangedPumpTileEntity.class);
        registration.registerBlockDataProvider(UraniumEnricherInfoProvider.INSTANCE, UraniumEnchricherTileEntity.class);
        registration.registerBlockDataProvider(ElectricWoodGassifierInfoProvider.INSTANCE, WoodGassifierTileEntity.class);
        registration.registerBlockDataProvider(StoneMachineInfoProvider.INSTANCE, BaseInventoryTileEntity.class);
        registration.registerBlockDataProvider(ElectrolyzerInfoProvider.INSTANCE, BaseInventoryTileEntity.class);
        registration.registerBlockDataProvider(CropLibraryInfoProvider.INSTANCE, BaseCropLibraryTileEntity.class);
        registration.registerBlockDataProvider(BaseTeleporterInfoProvider.INSTANCE, BaseTileEntity.class);
        registration.registerBlockDataProvider(BaseGeneratorInfoProvider.INSTANCE, BaseGeneratorTileEntity.class);
        registration.registerBlockDataProvider(SolarPanelInfoProvider.INSTANCE, SolarPanelTileEntity.class);
        registration.registerBlockDataProvider(WaveGenInfoProvider.INSTANCE, WaveGenTileEntity.class);
        registration.registerBlockDataProvider(NuclearReactorInfoProvider.INSTANCE, BaseNuclearReactorTileEntity.class);
        registration.registerBlockDataProvider(NuclearReactorInfoProvider.INSTANCE, BaseReactorChamberTileEntity.class);
        registration.registerBlockDataProvider(OceanGenInfoProvider.INSTANCE, OceanGeneratorTileEntity.class);
        registration.registerBlockDataProvider(WindmillGenInfoProvider.INSTANCE, WindmillTileEntity.class);
        registration.registerBlockDataProvider(SteamTurbineInfoProvider.INSTANCE, SteamTurbineTileEntity.class);
        registration.registerBlockDataProvider(BaseEnergyStorageInfoProvider.INSTANCE, BaseEnergyStorageTileEntity.class);
        registration.registerBlockDataProvider(CableInfoProvider.INSTANCE, CableTileEntity.class);
        registration.registerBlockDataProvider(BatteryStationInfoProvider.INSTANCE, BaseBatteryStationTileEntity.class);
        registration.registerBlockDataProvider(TransformerInfoProvider.INSTANCE, BaseTransformerTileEntity.class);
        registration.registerBlockDataProvider(AdjustableTransformerInfoProvider.INSTANCE, AdjustableTransformerTileEntity.class);
        registration.registerBlockDataProvider(LuminatorInfoProvider.INSTANCE, LuminatorTileEntity.class);
        registration.registerBlockDataProvider(LuminatorInfoProvider.INSTANCE, ConstructionLightTileEntity.class);
        registration.registerBlockDataProvider(ChargePadInfoProvider.INSTANCE, BaseChargePadTileEntity.class);
        registration.registerBlockDataProvider(ChargingBenchInfoProvider.INSTANCE, BaseChargingBenchTileEntity.class);
        registration.registerBlockDataProvider(TeleporterInfoProvider.INSTANCE, TeleporterTileEntity.class);
        registration.registerBlockDataProvider(ElectricLoaderInfoProvider.INSTANCE, BaseElectricLoaderTileEntity.class);
        registration.registerBlockDataProvider(ElectricUnloaderInfoProvider.INSTANCE, BaseElectricUnloaderTileEntity.class);
        registration.registerBlockDataProvider(BaseMultiBlockMachineInfoProvider.INSTANCE, BaseTileEntity.class);
        registration.registerBlockDataProvider(SteamTunnelInfoProvider.INSTANCE, BaseTileEntity.class);
        registration.registerBlockDataProvider(ElectricFisherInfoProvider.INSTANCE, ElectricFisherTileEntity.class);
        registration.registerBlockDataProvider(DynamicTankInfoProvider.INSTANCE, BaseTileEntity.class);
//        registration.registerBlockDataProvider(CasingInfoProvider.INSTANCE, BaseTileEntity.class);
//        registration.registerBlockDataProvider(CasingInfoProvider.INSTANCE, BaseTileEntity.class);
        registration.registerBlockDataProvider(RedirectorMasterInfoProvider.INSTANCE, RedirectorMasterTileEntity.class);
        registration.registerBlockDataProvider(RedirectorSlaveInfoProvider.INSTANCE, RedirectorSlaveTileEntity.class);
        registration.registerBlockDataProvider(PipePumpInfoProvider.INSTANCE, ElectricPipePumpTileEntity.class);
        registration.registerBlockDataProvider(VillagerOMatInfoProvider.INSTANCE, VillagerOMatTileEntity.class);
        registration.registerBlockDataProvider(FluidOMatInfoProvider.INSTANCE, FluidOMatTileEntity.class);
        registration.registerBlockDataProvider(FuelBoilerInfoProvider.INSTANCE, BaseTileEntity.class);
        registration.registerBlockDataProvider(ThermonuclearReactorInfoProvider.INSTANCE, BaseTileEntity.class);
        registration.registerBlockDataProvider(TubeInfoProvider.INSTANCE, TubeTileEntity.class);

        registration.registerBlockDataProvider(EUStorageInfoProvider.INSTANCE, BaseTileEntity.class);
    }

    private static void registerBlocks(IWailaClientRegistration r, IBlockComponentProvider p, Class<? extends Block>... c) {
        Stream.of(c).forEach(block -> r.registerBlockComponent(p, block));
    }

    private static void registerProvidersForBlock(IWailaClientRegistration r, Class<? extends Block> blockClass, IBlockComponentProvider... providers) {
        Stream.of(providers).forEach(provider -> r.registerBlockComponent(provider, blockClass));
    }
}

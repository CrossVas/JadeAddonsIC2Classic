package dev.crossvas.jadexic2c;

import dev.crossvas.jadexic2c.base.JadeBlockEntityDataProvider;
import dev.crossvas.jadexic2c.base.JadeTankInfoRenderer;
import dev.crossvas.jadexic2c.base.JadeTooltipRenderer;
import dev.crossvas.jadexic2c.providers.TreetapAndBucketInfo;
import dev.crossvas.jadexic2c.providers.WrenchInfo;
import ic2.core.block.base.features.multiblock.IStructureListener;
import ic2.core.block.misc.TreeTapAndBucketBlock;
import ic2.core.block.misc.textured.TexturedBlockBlock;
import ic2.core.block.storage.tiles.tank.BaseValveTileEntity;
import ic2.core.platform.events.StructureManager;
import ic2.core.platform.registries.IC2Blocks;
import ic2.core.platform.registries.IC2Tiles;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import snownee.jade.api.*;

@WailaPlugin(JadeXIC2C.ID_IC2)
public class JadePluginHandler implements IWailaPlugin {

    public static final ResourceLocation EU_READER_INFO = JadeXIC2C.rl("eu_reader");
    public static final ResourceLocation THERMOMETER_INFO = JadeXIC2C.rl("thermometer");
    public static final ResourceLocation CROP_INFO = JadeXIC2C.rl("crop");
    public static final ResourceLocation EU_STORAGE_INFO = JadeXIC2C.rl("eu_storage_info");

    public static final ResourceLocation TOP_STYLE = JadeXIC2C.rl("force_top_style");
    public static final ResourceLocation TANK_RENDER = JadeXIC2C.rl("remove_renders_fluid");

    /**
     * {@link IC2Blocks}
     * {@link IC2Tiles}
     * for quick access
     */

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.addConfig(TOP_STYLE, true);
        registration.addConfig(TANK_RENDER, true);

        registration.registerBlockComponent(JadeTooltipRenderer.INSTANCE, Block.class);

        // multiblock handler
        registration.addRayTraceCallback((hitResult, accessor, originalAccessor) -> {
            if (accessor instanceof BlockAccessor blockAccessor) {
                Level level = blockAccessor.getLevel();
                BlockPos pos = blockAccessor.getPosition();
                BlockHitResult blockHitResult = blockAccessor.getHitResult();
                IStructureListener listener = StructureManager.INSTANCE.getListener(level, pos);
                if (listener instanceof BlockEntity master) {
                    if (!(blockAccessor.getBlockEntity() instanceof BaseValveTileEntity)) { // we handle each valve individually for each multiblock
                        CompoundTag structureTag = new CompoundTag();
                        structureTag.putBoolean("isStructure", true);
                        blockAccessor.getServerData().put("structureData", structureTag);
                        return registration.blockAccessor()
                                .from(blockAccessor)
                                .hit(blockHitResult.withPosition(master.getBlockPos()))
                                .blockState(level.getBlockState(master.getBlockPos()))
                                .blockEntity(master)
                                .build();
                    }
                }
            }
            return accessor;
        });

        registration.addRayTraceCallback((hitResult, accessor, original) -> {
            if (accessor instanceof BlockAccessor blockAccessor) {
                Block block = blockAccessor.getBlock();
                if (block instanceof TexturedBlockBlock textured) {
                    return registration.blockAccessor().from(blockAccessor).fakeBlock(textured.getCloneItemStack(blockAccessor.getBlockState(),
                            blockAccessor.getHitResult(), blockAccessor.getLevel(), blockAccessor.getPosition(), blockAccessor.getPlayer())).build();
                }
            }
            return accessor;
        });

        registration.registerBlockComponent(JadeTankInfoRenderer.INSTANCE, Block.class);
        registration.registerBlockComponent(TreetapAndBucketInfo.THIS, TreeTapAndBucketBlock.class);

        // we handle this here because we need to send/receive server data directly
        registration.registerBlockComponent(WrenchInfo.THIS, Block.class);
//        registration.registerBlockComponent(CropInfoProvider.INSTANCE, CropBlock.class);
//        registration.registerBlockIcon(CropInfoProvider.INSTANCE, CropBlock.class);
//        registration.registerBlockComponent(BarrelInfoProvider.INSTANCE, BarrelBlock.class);
//
//        registerProvidersForBlock(registration, BaseMachineBlock.class,
//                BaseMachineInfoProvider.INSTANCE,
//                MinerInfoProvider.INSTANCE,
//                OreScannerInfoProvider.INSTANCE,
//                PlasmafierInfoProvider.INSTANCE,
//                PumpInfoProvider.INSTANCE,
//                RangedPumpInfoProvider.INSTANCE,
//                UraniumEnricherInfoProvider.INSTANCE,
//                ElectricWoodGassifierInfoProvider.INSTANCE,
//                StoneMachineInfoProvider.INSTANCE,
//                ElectrolyzerInfoProvider.INSTANCE,
//                CropLibraryInfoProvider.INSTANCE,
//                BaseTeleporterInfoProvider.INSTANCE
//        );
//
//        registerProvidersForBlock(registration, BaseGeneratorBlock.class,
//                SolarPanelInfoProvider.INSTANCE,
//                WaveGenInfoProvider.INSTANCE,
//                NuclearReactorInfoProvider.INSTANCE,
//                OceanGenInfoProvider.INSTANCE,
//                WindmillGenInfoProvider.INSTANCE,
//                SteamTurbineInfoProvider.INSTANCE
//        );
//
//        registerProvidersForBlock(registration, TransformerBlock.class,
//                TransformerInfoProvider.INSTANCE,
//                AdjustableTransformerInfoProvider.INSTANCE);
//
//        registerProvidersForBlock(registration, BaseLoaderBlock.class,
//                ElectricLoaderInfoProvider.INSTANCE,
//                ElectricUnloaderInfoProvider.INSTANCE
//        );
//
//        registerProvidersForBlock(registration, RedirectorBlock.class,
//                RedirectorMasterInfoProvider.INSTANCE,
//                RedirectorSlaveInfoProvider.INSTANCE
//        );
//
//        registerProvidersForBlock(registration, TubeBlock.class,
//                StackingTubeInfoProvider.INSTANCE,
//                RequestTubeInfoProvider.INSTANCE,
//                RoundRobinTubeInfoProvider.INSTANCE,
//                DirectionalTubeInfoProvider.INSTANCE,
//                PickupTubeInfoProvider.INSTANCE,
//                TeleportTubeInfoProvider.INSTANCE,
//                FilterTubeInfoProvider.INSTANCE,
//                FilteredExtractionTubeInfoProvider.INSTANCE,
//                ExtractionTubeInfoProvider.INSTANCE,
//                InsertionTubInfoProvider.INSTANCE,
//                ColorFilterTubeInfoProvider.INSTANCE,
//                LimiterTubeInfoProvider.INSTANCE,
//                BasicTubeInfoProvider.INSTANCE
//        );
//
//        registerBlocks(registration, BasicPipeInfoProvider.INSTANCE,
//                PipeBlock.class,
//                TubeBlock.class);
//
//        registerBlocks(registration, ElectricBlockInfoProvider.INSTANCE,
//                BaseMachineBlock.class,
//                MonitorBlock.class,
//                NoStateMachineBlock.class);
//
//        registerBlocks(registration, BaseGeneratorInfoProvider.INSTANCE,
//                BaseGeneratorBlock.class,
//                ThermalGeneratorBlock.class);
//
//        registerBlocks(registration, LuminatorInfoProvider.INSTANCE, LuminatorBlock.class, ConstructionLightBlock.class);
//        registerBlocks(registration, PipePumpInfoProvider.INSTANCE,
//                PipeBlock.class,
//                PumpBlock.class
//        );
//
//        // multiblocks with valves
//        registerBlocks(registration, SteamTunnelInfoProvider.INSTANCE,
//                TurbineBlock.class,
//                ValveBlock.class
//        );
//        registerBlocks(registration, ThermonuclearReactorInfoProvider.INSTANCE,
//                NoStateMachineBlock.class,
//                ValveBlock.class
//        );
//        registerBlocks(registration, DynamicTankInfoProvider.INSTANCE,
//                TankBlock.class,
//                ValveBlock.class
//        );
//
//        registration.registerBlockComponent(BaseMultiBlockMachineInfoProvider.INSTANCE, BaseMachineBlock.class);
//        registration.registerBlockComponent(FuelBoilerInfoProvider.INSTANCE, FuelBoilerBlock.class);
//        registration.registerBlockComponent(NuclearReactorInfoProvider.INSTANCE, ReactorChamberBlock.class);
//        registration.registerBlockComponent(BaseEnergyStorageInfoProvider.INSTANCE, EnergyStorageBlock.class);
//        registration.registerBlockComponent(CableInfoProvider.INSTANCE, CableBlock.class);
//        registration.registerBlockComponent(BatteryStationInfoProvider.INSTANCE, BaseTexturedBlock.class);
//        registration.registerBlockComponent(ChargePadInfoProvider.INSTANCE, ChargePadBlock.class);
//        registration.registerBlockComponent(ChargingBenchInfoProvider.INSTANCE, ChargingBenchBlock.class);
//        registration.registerBlockComponent(TeleporterInfoProvider.INSTANCE, TeleporterBlock.class);
//        registration.registerBlockComponent(ElectricFisherInfoProvider.INSTANCE, NoStateMachineBlock.class);
//        registration.registerBlockComponent(PushingValveInfoProvider.INSTANCE, ValveBlock.class);
//        registration.registerBlockComponent(VillagerOMatInfoProvider.INSTANCE, VillagerOMatBlock.class);
//        registration.registerBlockComponent(FluidOMatInfoProvider.INSTANCE, PersonalBlock.class);
//        registration.registerBlockComponent(TreetapAndBucketInfoProvider.INSTANCE, TreeTapAndBucketBlock.class);
//        registration.registerBlockComponent(UUMExpansionInfoProvider.INSTANCE, NoStateMachineBlock.class);
//        registration.registerBlockComponent(FluidExpansionInfoProvider.INSTANCE, FluidExpansionBlock.class);
//        registration.registerBlockComponent(MemoryExpansionInfoProvider.INSTANCE, NoStateMachineBlock.class);
//        registration.registerBlockComponent(StorageExpansionInfoProvider.INSTANCE, NoStateMachineBlock.class);
//
//
//        // common tooltips keep last
//        registration.registerBlockComponent(TankRender.INSTANCE, Block.class);
//        registration.registerBlockComponent(ModNameRender.MOD_NAME_REMOVER, Block.class);
//        registration.registerBlockComponent(ModNameRender.MOD_NAME_RELOCATOR, Block.class);
//        registration.registerBlockComponent(EUStorageInfoProvider.INSTANCE, Block.class);
//        registration.registerBlockComponent(WrenchableInfoProvider.INSTANCE, Block.class); // keep very last
    }

    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerBlockDataProvider(JadeBlockEntityDataProvider.INSTANCE, BlockEntity.class);
//        registration.registerBlockDataProvider(CropInfoProvider.INSTANCE, CropTileEntity.class);
//        registration.registerBlockDataProvider(BarrelInfoProvider.INSTANCE, BarrelTileEntity.class);
//        registration.registerBlockDataProvider(BaseMachineInfoProvider.INSTANCE, BaseMachineTileEntity.class);
//        registration.registerBlockDataProvider(ElectricBlockInfoProvider.INSTANCE, BaseElectricTileEntity.class);
//        registration.registerBlockDataProvider(MinerInfoProvider.INSTANCE, MinerTileEntity.class);
//        registration.registerBlockDataProvider(OreScannerInfoProvider.INSTANCE, OreScannerTileEntity.class);
//        registration.registerBlockDataProvider(PlasmafierInfoProvider.INSTANCE, PlasmafierTileEntity.class);
//        registration.registerBlockDataProvider(PumpInfoProvider.INSTANCE, PumpTileEntity.class);
//        registration.registerBlockDataProvider(RangedPumpInfoProvider.INSTANCE, RangedPumpTileEntity.class);
//        registration.registerBlockDataProvider(UraniumEnricherInfoProvider.INSTANCE, UraniumEnchricherTileEntity.class);
//        registration.registerBlockDataProvider(ElectricWoodGassifierInfoProvider.INSTANCE, WoodGassifierTileEntity.class);
//        registration.registerBlockDataProvider(StoneMachineInfoProvider.INSTANCE, BaseInventoryTileEntity.class);
//        registration.registerBlockDataProvider(ElectrolyzerInfoProvider.INSTANCE, BaseInventoryTileEntity.class);
//        registration.registerBlockDataProvider(CropLibraryInfoProvider.INSTANCE, BaseCropLibraryTileEntity.class);
//        registration.registerBlockDataProvider(BaseTeleporterInfoProvider.INSTANCE, BaseTileEntity.class);
//        registration.registerBlockDataProvider(BaseGeneratorInfoProvider.INSTANCE, BaseGeneratorTileEntity.class);
//        registration.registerBlockDataProvider(SolarPanelInfoProvider.INSTANCE, SolarPanelTileEntity.class);
//        registration.registerBlockDataProvider(WaveGenInfoProvider.INSTANCE, WaveGenTileEntity.class);
//        registration.registerBlockDataProvider(NuclearReactorInfoProvider.INSTANCE, BaseNuclearReactorTileEntity.class);
//        registration.registerBlockDataProvider(NuclearReactorInfoProvider.INSTANCE, BaseReactorChamberTileEntity.class);
//        registration.registerBlockDataProvider(OceanGenInfoProvider.INSTANCE, OceanGeneratorTileEntity.class);
//        registration.registerBlockDataProvider(WindmillGenInfoProvider.INSTANCE, WindmillTileEntity.class);
//        registration.registerBlockDataProvider(SteamTurbineInfoProvider.INSTANCE, SteamTurbineTileEntity.class);
//        registration.registerBlockDataProvider(BaseEnergyStorageInfoProvider.INSTANCE, BaseEnergyStorageTileEntity.class);
//        registration.registerBlockDataProvider(CableInfoProvider.INSTANCE, CableTileEntity.class);
//        registration.registerBlockDataProvider(BatteryStationInfoProvider.INSTANCE, BaseBatteryStationTileEntity.class);
//        registration.registerBlockDataProvider(TransformerInfoProvider.INSTANCE, BaseTransformerTileEntity.class);
//        registration.registerBlockDataProvider(AdjustableTransformerInfoProvider.INSTANCE, AdjustableTransformerTileEntity.class);
//        registration.registerBlockDataProvider(LuminatorInfoProvider.INSTANCE, LuminatorTileEntity.class);
//        registration.registerBlockDataProvider(LuminatorInfoProvider.INSTANCE, ConstructionLightTileEntity.class);
//        registration.registerBlockDataProvider(ChargePadInfoProvider.INSTANCE, BaseChargePadTileEntity.class);
//        registration.registerBlockDataProvider(ChargingBenchInfoProvider.INSTANCE, BaseChargingBenchTileEntity.class);
//        registration.registerBlockDataProvider(TeleporterInfoProvider.INSTANCE, TeleporterTileEntity.class);
//        registration.registerBlockDataProvider(ElectricLoaderInfoProvider.INSTANCE, BaseElectricLoaderTileEntity.class);
//        registration.registerBlockDataProvider(ElectricUnloaderInfoProvider.INSTANCE, BaseElectricUnloaderTileEntity.class);
//        registration.registerBlockDataProvider(BaseMultiBlockMachineInfoProvider.INSTANCE, BaseTileEntity.class);
//        registration.registerBlockDataProvider(SteamTunnelInfoProvider.INSTANCE, BaseTileEntity.class);
//        registration.registerBlockDataProvider(ElectricFisherInfoProvider.INSTANCE, ElectricFisherTileEntity.class);
//        registration.registerBlockDataProvider(DynamicTankInfoProvider.INSTANCE, BaseTileEntity.class);
//        registration.registerBlockDataProvider(RedirectorMasterInfoProvider.INSTANCE, RedirectorMasterTileEntity.class);
//        registration.registerBlockDataProvider(RedirectorSlaveInfoProvider.INSTANCE, RedirectorSlaveTileEntity.class);
//        registration.registerBlockDataProvider(PipePumpInfoProvider.INSTANCE, ElectricPipePumpTileEntity.class);
//        registration.registerBlockDataProvider(VillagerOMatInfoProvider.INSTANCE, VillagerOMatTileEntity.class);
//        registration.registerBlockDataProvider(FluidOMatInfoProvider.INSTANCE, FluidOMatTileEntity.class);
//        registration.registerBlockDataProvider(FuelBoilerInfoProvider.INSTANCE, BaseTileEntity.class);
//        registration.registerBlockDataProvider(ThermonuclearReactorInfoProvider.INSTANCE, BaseTileEntity.class);
//
//        registration.registerBlockDataProvider(BasicTubeInfoProvider.INSTANCE, TubeTileEntity.class);
//        registration.registerBlockDataProvider(StackingTubeInfoProvider.INSTANCE, StackingTubeTileEntity.class);
//        registration.registerBlockDataProvider(RequestTubeInfoProvider.INSTANCE, RequestTubeTileEntity.class);
//        registration.registerBlockDataProvider(RoundRobinTubeInfoProvider.INSTANCE, RoundRobinTubeTileEntity.class);
//        registration.registerBlockDataProvider(DirectionalTubeInfoProvider.INSTANCE, DirectionalTubeTileEntity.class);
//        registration.registerBlockDataProvider(PickupTubeInfoProvider.INSTANCE, PickupTubeTileEntity.class);
//        registration.registerBlockDataProvider(TeleportTubeInfoProvider.INSTANCE, TeleportTubeTileEntity.class);
//        registration.registerBlockDataProvider(ColorFilterTubeInfoProvider.INSTANCE, ColorFilterTubeTileEntity.class);
//        registration.registerBlockDataProvider(FilterTubeInfoProvider.INSTANCE, FilterTubeTileEntity.class);
//        registration.registerBlockDataProvider(FilteredExtractionTubeInfoProvider.INSTANCE, FilteredExtractionTubeTileEntity.class);
//        registration.registerBlockDataProvider(LimiterTubeInfoProvider.INSTANCE, LimiterTubeTileEntity.class);
//        registration.registerBlockDataProvider(InsertionTubInfoProvider.INSTANCE, InsertionTubeTileEntity.class);
//        registration.registerBlockDataProvider(EUStorageInfoProvider.INSTANCE, BaseTileEntity.class);
//
//        registration.registerBlockDataProvider(BasicPipeInfoProvider.INSTANCE, PipeTileEntity.class);
//        registration.registerBlockDataProvider(BasicPipeInfoProvider.INSTANCE, FluidTubeTileEntity.class);
//        registration.registerBlockDataProvider(UUMExpansionInfoProvider.INSTANCE, UUMatterExpansionTileEntity.class);
//        registration.registerBlockDataProvider(FluidExpansionInfoProvider.INSTANCE, TankExpansionTileEntity.class);
//        registration.registerBlockDataProvider(MemoryExpansionInfoProvider.INSTANCE, MemoryExpansionTileEntity.class);
//        registration.registerBlockDataProvider(StorageExpansionInfoProvider.INSTANCE, BufferStorageExpansionTileEntity.class);
//        registration.registerBlockDataProvider(StorageExpansionInfoProvider.INSTANCE, StorageExpansionTileEntity.class);
    }

//    @SafeVarargs
//    private static void registerBlocks(IWailaClientRegistration r, IBlockComponentProvider p, Class<? extends Block>... c) {
//        Stream.of(c).forEach(block -> r.registerBlockComponent(p, block));
//    }
//
//    private static void registerProvidersForBlock(IWailaClientRegistration r, Class<? extends Block> blockClass, IBlockComponentProvider... providers) {
//        Stream.of(providers).forEach(provider -> r.registerBlockComponent(provider, blockClass));
//    }
}

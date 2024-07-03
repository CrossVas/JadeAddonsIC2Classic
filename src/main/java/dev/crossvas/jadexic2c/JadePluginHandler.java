package dev.crossvas.jadexic2c;

import dev.crossvas.jadexic2c.base.JadeBlockEntityDataProvider;
import dev.crossvas.jadexic2c.base.JadeTankInfoRenderer;
import dev.crossvas.jadexic2c.base.JadeTooltipRenderer;
import dev.crossvas.jadexic2c.providers.removals.ModNameRender;
import dev.crossvas.jadexic2c.providers.*;
import ic2.core.block.base.features.multiblock.IStructureListener;
import ic2.core.block.cables.CableBlock;
import ic2.core.block.crops.CropBlock;
import ic2.core.block.crops.CropTileEntity;
import ic2.core.block.misc.TreeTapAndBucketBlock;
import ic2.core.block.misc.textured.TexturedBlockBlock;
import ic2.core.block.misc.textured.TexturedSlabBlock;
import ic2.core.block.misc.textured.TexturedStairsBlock;
import ic2.core.block.misc.textured.TexturedWallBlock;
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

        registration.registerBlockComponent(CropInfo.CropIcon.THIS, CropBlock.class);
        registration.registerBlockIcon(CropInfo.CropIcon.THIS, CropBlock.class);
        registration.registerBlockComponent(JadeTooltipRenderer.INSTANCE, Block.class);
        registration.registerBlockComponent(new CableInfo.CableIconProvider(), CableBlock.class);
        registration.registerBlockComponent(TexturedBlockInfo.INSTANCE, TexturedBlockBlock.class);
        registration.registerBlockIcon(TexturedBlockInfo.INSTANCE, TexturedBlockBlock.class);
        registration.registerBlockComponent(TexturedBlockInfo.INSTANCE, TexturedSlabBlock.class);
        registration.registerBlockIcon(TexturedBlockInfo.INSTANCE, TexturedSlabBlock.class);
        registration.registerBlockComponent(TexturedBlockInfo.INSTANCE, TexturedWallBlock.class);
        registration.registerBlockIcon(TexturedBlockInfo.INSTANCE, TexturedWallBlock.class);
        registration.registerBlockComponent(TexturedBlockInfo.INSTANCE, TexturedStairsBlock.class);
        registration.registerBlockIcon(TexturedBlockInfo.INSTANCE, TexturedStairsBlock.class);

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

        registration.registerBlockComponent(JadeTankInfoRenderer.INSTANCE, Block.class);
        registration.registerBlockComponent(TreetapAndBucketInfo.THIS, TreeTapAndBucketBlock.class);

        registration.registerBlockComponent(ModNameRender.MOD_NAME_REMOVER, Block.class);
        registration.registerBlockComponent(ModNameRender.MOD_NAME_RELOCATOR, Block.class);
        // we handle this here because we need to send/receive server data directly
        registration.registerBlockComponent(WrenchInfo.THIS, Block.class);
    }

    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerBlockDataProvider(JadeBlockEntityDataProvider.INSTANCE, BlockEntity.class);
        registration.registerBlockDataProvider(JadeTankInfoRenderer.INSTANCE, BlockEntity.class);
        registration.registerBlockDataProvider(CropInfo.CropIcon.THIS, CropTileEntity.class);
    }
}

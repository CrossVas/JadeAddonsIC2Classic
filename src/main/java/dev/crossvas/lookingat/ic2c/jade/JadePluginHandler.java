package dev.crossvas.lookingat.ic2c.jade;

import dev.crossvas.lookingat.ic2c.LookingAtIC2C;
import dev.crossvas.lookingat.ic2c.LookingAtTags;
import dev.crossvas.lookingat.ic2c.jade.individual.*;
import dev.crossvas.lookingat.ic2c.jade.removals.JadeTankInfoRenderer;
import dev.crossvas.lookingat.ic2c.jade.removals.ModNameRender;
import ic2.core.block.base.features.multiblock.IStructureListener;
import ic2.core.block.cables.CableBlock;
import ic2.core.block.crops.CropBlock;
import ic2.core.block.crops.CropTileEntity;
import ic2.core.block.misc.BarrelBlock;
import ic2.core.block.misc.TreeTapAndBucketBlock;
import ic2.core.block.misc.tiles.BarrelTileEntity;
import ic2.core.block.storage.tiles.tank.BaseValveTileEntity;
import ic2.core.platform.events.StructureManager;
import ic2.core.platform.registries.IC2Blocks;
import ic2.core.platform.registries.IC2Tiles;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import snownee.jade.api.*;

@WailaPlugin(LookingAtIC2C.ID_IC2)
public class JadePluginHandler implements IWailaPlugin {

    /**
     * {@link IC2Blocks}
     * {@link IC2Tiles}
     * for quick access
     */

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.addConfig(LookingAtTags.JADE_TOP_STYLE, true);

        registration.registerBlockIcon(BarrelIcon.THIS, BarrelBlock.class);
        registration.registerBlockComponent(CropIcon.THIS, CropBlock.class);
        registration.registerBlockIcon(CropIcon.THIS, CropBlock.class);
        registration.registerBlockComponent(JadeTooltipRenderer.INSTANCE, Block.class);
        registration.registerBlockComponent(CableIcon.THIS, CableBlock.class);
        registration.registerBlockComponent(TexturedBlockInfo.INSTANCE, Block.class);
        registration.registerBlockIcon(TexturedBlockInfo.INSTANCE, Block.class);

        registration.registerEntityComponent(new HiveProvider(), LivingEntity.class);

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
        registration.registerBlockDataProvider(BarrelIcon.THIS, BarrelTileEntity.class);
        registration.registerBlockDataProvider(JadeTooltipRenderer.INSTANCE, BlockEntity.class);
        registration.registerBlockDataProvider(JadeTankInfoRenderer.INSTANCE, BlockEntity.class);
        registration.registerBlockDataProvider(CropIcon.THIS, CropTileEntity.class);
    }
}

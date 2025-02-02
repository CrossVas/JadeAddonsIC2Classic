package ic2.jadeplugin;

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
import ic2.jadeplugin.base.JadeTooltipRenderer;
import ic2.jadeplugin.base.removals.JadeTankInfoRenderer;
import ic2.jadeplugin.base.removals.ModNameRender;
import ic2.jadeplugin.providers.*;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import snownee.jade.api.*;

@WailaPlugin(IC2JadePlugin.ID_IC2)
public class IC2JadePluginHandler implements IWailaPlugin {

    /**
     * {@link IC2Blocks}
     * {@link IC2Tiles}
     * for quick access
     */

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.addConfig(JadeTags.TOP_STYLE, true);
        registration.addConfig(JadeTags.SNEAK_FOR_DETAILS, false);

        registration.registerBlockIcon(new BarrelInfo.BarrelIconProvider(), BarrelBlock.class);
        registration.registerBlockComponent(CropInfo.CropIcon.THIS, CropBlock.class);
        registration.registerBlockIcon(CropInfo.CropIcon.THIS, CropBlock.class);
        registration.registerBlockComponent(JadeTooltipRenderer.INSTANCE, Block.class);
        registration.registerBlockComponent(new CableInfo.CableIconProvider(), CableBlock.class);
        registration.registerBlockComponent(TexturedBlockInfo.INSTANCE, Block.class);
        registration.registerBlockIcon(TexturedBlockInfo.INSTANCE, Block.class);

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
                        blockAccessor.getServerData().put(JadeTags.TAG_STRUCTURE, structureTag);
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
        registration.registerBlockDataProvider(new BarrelInfo.BarrelIconProvider(), BarrelTileEntity.class);
        registration.registerBlockDataProvider(JadeTooltipRenderer.INSTANCE, BlockEntity.class);
        registration.registerBlockDataProvider(JadeTankInfoRenderer.INSTANCE, BlockEntity.class);
        registration.registerBlockDataProvider(CropInfo.CropIcon.THIS, CropTileEntity.class);
    }
}

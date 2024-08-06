package dev.crossvas.lookingat.ic2c.waila;

import dev.crossvas.lookingat.ic2c.LookingAtTags;
import dev.crossvas.lookingat.ic2c.waila.individual.BarrelIcon;
import dev.crossvas.lookingat.ic2c.waila.individual.WrenchInfo;
import dev.crossvas.lookingat.ic2c.waila.removals.ModNameRender;
import dev.crossvas.lookingat.ic2c.waila.removals.WailaTankInfoRenderer;
import ic2.core.block.base.features.multiblock.IStructureListener;
import ic2.core.block.cables.CableBlock;
import ic2.core.block.misc.BarrelBlock;
import ic2.core.block.storage.tiles.tank.BaseValveTileEntity;
import ic2.core.platform.events.StructureManager;
import ic2.core.platform.registries.IC2Blocks;
import ic2.core.platform.registries.IC2Tiles;
import mcp.mobius.waila.api.*;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;

public class WailaPluginHandler implements IWailaPlugin {

    /**
     * {@link IC2Blocks}
     * {@link IC2Tiles}
     * for quick access
     */

    @Override
    public void register(IRegistrar registration) {
        registration.addConfig(LookingAtTags.INFO_RENDERER, true);
        registration.addConfig(LookingAtTags.WRENCHABLE, true);
        registration.addConfig(LookingAtTags.RELOCATE_MODID, true);
        registration.addConfig(LookingAtTags.TANK_RENDER, true);

        registration.addIcon(BarrelIcon.THIS, BarrelBlock.class);
        registration.addBlockData(BarrelIcon.THIS, BlockEntity.class);

        registration.addComponent((IBlockComponentProvider) WailaTooltipRenderer.INSTANCE, TooltipPosition.BODY, Block.class);
        registration.addBlockData(WailaTooltipRenderer.INSTANCE, BlockEntity.class, 0);
        registration.addBlockData(WailaTankInfoRenderer.THIS, BlockEntity.class, 500);

        registration.addRedirect(new IBlockComponentProvider() {
            @Nullable
            @Override
            public ITargetRedirector.Result redirect(ITargetRedirector redirect, IBlockAccessor accessor, IPluginConfig config) {
                Level level = accessor.getWorld();
                BlockPos pos = accessor.getPosition();
                IStructureListener listener = StructureManager.INSTANCE.getListener(level, pos);
                if (listener instanceof BlockEntity master) {
                    if (!(accessor.getBlockEntity() instanceof BaseValveTileEntity)) {
                        return redirect.to(accessor.getBlockHitResult().withPosition(master.getBlockPos()));
                    }
                }
                return null;
            }
        }, Block.class);
        registration.addComponent(WrenchInfo.THIS, TooltipPosition.TAIL, Block.class);
        registration.addComponent(ModNameRender.THIS, TooltipPosition.TAIL, Block.class);
        registration.addComponent(ModNameRender.ModNameProvider.THIS, TooltipPosition.HEAD, Block.class);
    }
}

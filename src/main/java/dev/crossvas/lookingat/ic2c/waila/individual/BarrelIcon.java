package dev.crossvas.lookingat.ic2c.waila.individual;

import ic2.core.block.misc.tiles.BarrelTileEntity;
import ic2.core.platform.registries.IC2Blocks;
import mcp.mobius.waila.api.*;
import mcp.mobius.waila.api.component.ItemComponent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;

public class BarrelIcon implements IBlockComponentProvider, IDataProvider<BlockEntity> {

    public static final BarrelIcon THIS = new BarrelIcon();

    @Override
    public @Nullable ITooltipComponent getIcon(IBlockAccessor accessor, IPluginConfig config) {
        ItemStack icon = new ItemStack(IC2Blocks.BARREL);
        if (accessor.getData().raw().contains("BarrelInfo")) {
            CompoundTag tag = accessor.getData().raw().getCompound("BarrelInfo");
            int brewType = tag.getInt("type");
            icon.getOrCreateTag().putInt("type", brewType);
        }
        return new ItemComponent(icon);
    }

    @Override
    public void appendData(IDataWriter iDataWriter, IServerAccessor<BlockEntity> iServerAccessor, IPluginConfig iPluginConfig) {
        if (iServerAccessor.getTarget() instanceof BarrelTileEntity barrel) {
            CompoundTag tag = new CompoundTag();
            tag.putInt("type", barrel.brewType);
            iDataWriter.raw().put("BarrelInfo", tag);
        }
    }

}

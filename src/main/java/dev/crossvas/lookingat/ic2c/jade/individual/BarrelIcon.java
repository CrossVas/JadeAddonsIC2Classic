package dev.crossvas.lookingat.ic2c.jade.individual;

import dev.crossvas.lookingat.ic2c.LookingAtTags;
import ic2.core.block.misc.tiles.BarrelTileEntity;
import ic2.core.platform.registries.IC2Blocks;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.IElement;
import snownee.jade.api.ui.IElementHelper;

public class BarrelIcon implements IBlockComponentProvider, IServerDataProvider<BlockEntity> {

    public static final BarrelIcon THIS = new BarrelIcon();

    @Override
    public @Nullable IElement getIcon(BlockAccessor accessor, IPluginConfig config, IElement currentIcon) {
        ItemStack icon = new ItemStack(IC2Blocks.BARREL);
        if (accessor.getServerData().contains("BarrelInfo")) {
            CompoundTag tag = accessor.getServerData().getCompound("BarrelInfo");
            int brewType = tag.getInt("type");
            icon.getOrCreateTag().putInt("type", brewType);
        }
        return IElementHelper.get().item(icon);
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, ServerPlayer serverPlayer, Level level, BlockEntity blockEntity, boolean b) {
        if (blockEntity instanceof BarrelTileEntity barrel) {
            CompoundTag tag = new CompoundTag();
            tag.putInt("type", barrel.brewType);
            compoundTag.put("BarrelInfo", tag);
        }
    }

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {}

    @Override
    public ResourceLocation getUid() {
        return LookingAtTags.INFO_RENDERER;
    }
}

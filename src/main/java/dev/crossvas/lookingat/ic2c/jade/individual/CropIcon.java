package dev.crossvas.lookingat.ic2c.jade.individual;

import dev.crossvas.lookingat.ic2c.LookingAtTags;
import dev.crossvas.lookingat.ic2c.base.providers.CropInfo;
import ic2.api.crops.ICrop;
import ic2.api.crops.ICropRegistry;
import ic2.api.crops.ICropTile;
import ic2.core.inventory.filter.SpecialFilters;
import ic2.core.platform.registries.IC2Items;
import ic2.core.utils.helpers.StackUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;
import snownee.jade.addon.harvest.HarvestToolProvider;
import snownee.jade.api.*;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.IElement;
import snownee.jade.api.ui.IElementHelper;

import java.util.List;

public class CropIcon implements IBlockComponentProvider, IServerDataProvider<BlockEntity> {

    public static final CropIcon THIS = new CropIcon();

    @Override
    public @Nullable IElement getIcon(BlockAccessor accessor, IPluginConfig config, IElement currentIcon) {
        ItemStack iconStack = ItemStack.EMPTY;
        if (accessor.getBlockEntity() instanceof ICropTile cropTile) {
            CompoundTag tag = accessor.getServerData().getCompound("CropInfo");
            int maxStage = tag.getInt("growthSteps");
            int currentStage = tag.getInt("growthStage");
            int scanLevel = tag.getInt("scanLevel");
            if (cropTile.getCrop() != null) {
                boolean condition = scanLevel < 1 && currentStage < maxStage && cropTile.getCrop() != ICropRegistry.WEED && cropTile.getCrop() != ICropRegistry.SEA_WEED;
                if (condition) {
                    iconStack = IC2Items.CROP_SEED.getDefaultInstance();
                } else {
                    iconStack = cropTile.getCrop().getDisplayItem();
                }
            }
        }
        return IElementHelper.get().item(iconStack);
    }

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (!StackUtil.hasHotbarItems(blockAccessor.getPlayer(), SpecialFilters.CROP_SCANNER)) {
            return;
        }

        CompoundTag tag = blockAccessor.getServerData().getCompound("CropInfo");
        if (blockAccessor.getBlockEntity() instanceof ICropTile tile) {
            int maxStage = tag.getInt("growthSteps");
            int currentStage = tag.getInt("growthStage");
            int scanLevel = tag.getInt("scanLevel");
            ICrop crop = tile.getCrop();
            if (crop != null) {
                List<IElement> elements = HarvestToolProvider.INSTANCE.getText(blockAccessor, iPluginConfig, iTooltip.getElementHelper());
                iTooltip.remove(Identifiers.MC_HARVEST_TOOL);
                iTooltip.remove(Identifiers.CORE_OBJECT_NAME);
                if (scanLevel < 1 && currentStage < maxStage && crop != ICropRegistry.WEED && crop != ICropRegistry.SEA_WEED) {
                    iTooltip.add(0, Component.translatable("info.crop.ic2.data.unknown").withStyle(ChatFormatting.WHITE));
                    elements.forEach(element -> iTooltip.append(0, element.align(IElement.Align.RIGHT)));
                } else {
                    iTooltip.add(0, iPluginConfig.getWailaConfig().getFormatting().title(crop.getName()), Identifiers.CORE_OBJECT_NAME);
                    elements.forEach(element -> iTooltip.append(0, element.align(IElement.Align.RIGHT)));
                    iTooltip.add(1, iTooltip.getElementHelper().text(Component.translatable("jei.ic2.reactor.by", crop.discoveredBy().getString()).withStyle(ChatFormatting.WHITE).withStyle(ChatFormatting.ITALIC)));
                }
            }
        }
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, ServerPlayer serverPlayer, Level level, BlockEntity blockEntity, boolean b) {
        if (blockEntity instanceof ICropTile tile) {
            CompoundTag tag = new CompoundTag();
            ICrop crop = tile.getCrop();
            if (crop != null) {
                // growth info
                tag.putInt("growthSteps", crop.getGrowthSteps());
                tag.putInt("growthStage", tile.getGrowthStage());
                tag.putInt("scanLevel", tile.getScanLevel());
            }
            compoundTag.put("CropInfo", tag);
        }
    }

    @Override
    public ResourceLocation getUid() {
        return LookingAtTags.INFO_RENDERER;
    }
}

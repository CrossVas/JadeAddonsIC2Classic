package dev.crossvas.jadexic2c.providers;

import dev.crossvas.jadexic2c.JadeTags;
import dev.crossvas.jadexic2c.base.removals.ModNameRender;
import ic2.core.block.misc.textured.TexturedBlockBlock;
import ic2.core.block.misc.textured.TexturedSlabBlock;
import ic2.core.block.misc.textured.TexturedStairsBlock;
import ic2.core.block.misc.textured.TexturedWallBlock;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec2;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.Identifiers;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.IElement;
import snownee.jade.api.ui.IElementHelper;
import snownee.jade.impl.ui.TextElement;
import snownee.jade.util.ModIdentification;

import javax.annotation.Nullable;

public class TexturedBlockInfo implements IBlockComponentProvider {

    public static final TexturedBlockInfo INSTANCE = new TexturedBlockInfo();

    @Override
    public @Nullable IElement getIcon(BlockAccessor blockAccessor, IPluginConfig config, IElement currentIcon) {
        ItemStack icon = ItemStack.EMPTY;
        Block block = blockAccessor.getBlock();
        if (block instanceof TexturedBlockBlock texturedBlock) {
            icon = getFakeIcon(texturedBlock, blockAccessor);
        }
        if (block instanceof TexturedSlabBlock texturedSlabBlock) {
            icon = getFakeIcon(texturedSlabBlock, blockAccessor);
        }
        if (block instanceof TexturedWallBlock texturedWallBlock) {
            icon = getFakeIcon(texturedWallBlock, blockAccessor);
        }
        if (block instanceof TexturedStairsBlock texturedStairsBlock) {
            icon = getFakeIcon(texturedStairsBlock, blockAccessor);
        }
        return IElementHelper.get().item(icon);
    }

    public static ItemStack getFakeIcon(Block block, BlockAccessor blockAccessor) {
        return block.getCloneItemStack(blockAccessor.getBlockState(), blockAccessor.getHitResult(), blockAccessor.getLevel(), blockAccessor.getPosition(), blockAccessor.getPlayer());
    }

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        Block block = blockAccessor.getBlock();
        if (block instanceof TexturedBlockBlock texturedBlockBlock) {
            addFakeInfo(texturedBlockBlock, iTooltip, blockAccessor, iPluginConfig);
        }
        if (block instanceof TexturedSlabBlock texturedSlabBlock) {
            addFakeInfo(texturedSlabBlock, iTooltip, blockAccessor, iPluginConfig);
        }
        if (block instanceof TexturedWallBlock texturedWallBlock) {
            addFakeInfo(texturedWallBlock, iTooltip, blockAccessor, iPluginConfig);
        }
        if (block instanceof TexturedStairsBlock texturedStairsBlock) {
            addFakeInfo(texturedStairsBlock, iTooltip, blockAccessor, iPluginConfig);
        }
    }

    public static void addFakeInfo(Block texturedBlock, ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        ItemStack fakeStack = texturedBlock.getCloneItemStack(blockAccessor.getBlockState(), blockAccessor.getHitResult(), blockAccessor.getLevel(), blockAccessor.getPosition(), blockAccessor.getPlayer());
        iTooltip.remove(Identifiers.CORE_OBJECT_NAME); // remove block name
        iTooltip.remove(Identifiers.CORE_MOD_NAME); // remove mod name
        iTooltip.remove(ModNameRender.RELOCATE); // disable relocator
        Component fakeNameComponent = fakeStack.getHoverName().copy().withStyle(ChatFormatting.WHITE);
        String fakeModName = ModIdentification.getModName(texturedBlock.getCloneItemStack(blockAccessor.getBlockState(), blockAccessor.getHitResult(), blockAccessor.getLevel(), blockAccessor.getPosition(), blockAccessor.getPlayer()));
        String fakeModNameFormatted = String.format(iPluginConfig.getWailaConfig().getFormatting().getModName(), fakeModName);
        // add fake block name
        iTooltip.add(new TextElement(fakeNameComponent).size(new Vec2(Minecraft.getInstance().font.width(fakeNameComponent.getString()) + 16, Minecraft.getInstance().font.lineHeight)));
        // add fake block modid
        iTooltip.add(Component.literal(fakeModNameFormatted));
    }

    @Override
    public ResourceLocation getUid() {
        return JadeTags.INFO_RENDERER;
    }
}

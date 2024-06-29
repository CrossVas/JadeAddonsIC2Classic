package dev.crossvas.jadexic2c.info;

import dev.crossvas.jadexic2c.JadeIC2CPluginHandler;
import dev.crossvas.jadexic2c.info.removals.ModNameRender;
import ic2.core.block.misc.textured.TexturedBlockBlock;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.Identifiers;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.util.ModIdentification;

public class TexturedBlockInfoProvider implements IBlockComponentProvider {

    public static final TexturedBlockInfoProvider INSTANCE = new TexturedBlockInfoProvider();

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        Block block = blockAccessor.getBlock();
        if (block instanceof TexturedBlockBlock texturedBlockBlock) {
            iTooltip.remove(Identifiers.CORE_MOD_NAME);
            iTooltip.remove(ModNameRender.RELOCATE);
            String fakeModName = ModIdentification.getModName(texturedBlockBlock.getCloneItemStack(blockAccessor.getBlockState(), blockAccessor.getHitResult(), blockAccessor.getLevel(), blockAccessor.getPosition(), blockAccessor.getPlayer()));
            String fakeModNameFormatted = String.format(iPluginConfig.getWailaConfig().getFormatting().getModName(), fakeModName);
            iTooltip.add(Component.literal(fakeModNameFormatted));
        }
    }

    @Override
    public ResourceLocation getUid() {
        return JadeIC2CPluginHandler.TOOLTIP_RENDERER;
    }
}

package dev.crossvas.jadexic2c.base;

import dev.crossvas.jadexic2c.JadeCommonHandler;
import dev.crossvas.jadexic2c.JadePluginHandler;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.TooltipPosition;
import snownee.jade.api.config.IPluginConfig;

public class JadeTankInfoRenderer implements IBlockComponentProvider {

    public static final JadeTankInfoRenderer INSTANCE = new JadeTankInfoRenderer();

    public static final ResourceLocation FORGE_FLUID = new ResourceLocation("fluid");

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (JadeCommonHandler.TANK_REMOVAL.contains(blockAccessor.getBlock())) {
            iTooltip.remove(FORGE_FLUID);
        }
    }

    @Override
    public int getDefaultPriority() {
        return TooltipPosition.TAIL;
    }

    @Override
    public ResourceLocation getUid() {
        return JadePluginHandler.TANK_RENDER;
    }
}

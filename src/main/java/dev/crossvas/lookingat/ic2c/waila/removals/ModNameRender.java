package dev.crossvas.lookingat.ic2c.waila.removals;

import ic2.core.block.base.IToolProvider;
import mcp.mobius.waila.api.*;
import net.minecraft.network.chat.Component;

public class ModNameRender implements IBlockComponentProvider {

    public static final ModNameRender THIS = new ModNameRender();

    @Override
    public void appendTail(ITooltip tooltip, IBlockAccessor accessor, IPluginConfig config) {
        if (accessor.getBlock() instanceof IToolProvider) {
            Component MOD_NAME = IWailaConfig.get().getFormatter().modName(IModInfo.get(accessor.getBlock()).getName());
            if (IWailaConfig.get().getFormatter().modName(IModInfo.get(accessor.getBlock()).getName()).equals(MOD_NAME)) {
                tooltip.setLine(WailaConstants.MOD_NAME_TAG);
            }
        }
    }

    public static class ModNameProvider implements IBlockComponentProvider {

        public static final ModNameProvider THIS = new ModNameProvider();

        @Override
        public void appendHead(ITooltip tooltip, IBlockAccessor accessor, IPluginConfig config) {
            if (accessor.getBlock() instanceof IToolProvider) {
                Component MOD_NAME = IWailaConfig.get().getFormatter().modName(IModInfo.get(accessor.getBlock()).getName());
                tooltip.addLine(MOD_NAME);
            }
        }
    }
}

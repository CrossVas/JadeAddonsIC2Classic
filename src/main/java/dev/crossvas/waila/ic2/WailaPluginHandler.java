package dev.crossvas.waila.ic2;

import dev.crossvas.waila.ic2.base.WailaTooltipRenderer;
import dev.crossvas.waila.ic2.base.tooltiprenderers.BaseProgressBarRenderer;
import mcp.mobius.waila.api.IWailaRegistrar;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;

public class WailaPluginHandler {

    public static void register(IWailaRegistrar registration) {
        registration.registerBodyProvider(WailaTooltipRenderer.THIS, Block.class);
        registration.registerNBTProvider(WailaTooltipRenderer.THIS, TileEntity.class);
        registration.registerTooltipRenderer("jade.progress", new BaseProgressBarRenderer());
    }
}

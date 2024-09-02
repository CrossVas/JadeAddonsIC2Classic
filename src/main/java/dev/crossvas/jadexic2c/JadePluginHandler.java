package dev.crossvas.jadexic2c;

import dev.crossvas.jadexic2c.base.JadeTooltipRenderer;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.IWailaRegistrar;
import mcp.mobius.waila.api.WailaPlugin;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;

@WailaPlugin(JadeIC2Classic.ID_IC2)
public class JadePluginHandler implements IWailaPlugin {

    @Override
    public void register(IWailaRegistrar registration) {
        registration.registerBodyProvider(JadeTooltipRenderer.THIS, Block.class);
        registration.registerNBTProvider(JadeTooltipRenderer.THIS, TileEntity.class);
    }
}

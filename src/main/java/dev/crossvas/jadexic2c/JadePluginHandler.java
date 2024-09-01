package dev.crossvas.jadexic2c;

import dev.crossvas.jadexic2c.providers.TestProvider;
import ic2.core.block.base.tile.TileEntityElectricBlock;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.IWailaRegistrar;
import mcp.mobius.waila.api.WailaPlugin;

@WailaPlugin(JadeIC2Classic.ID_IC2)
public class JadePluginHandler implements IWailaPlugin {

    @Override
    public void register(IWailaRegistrar registration) {
        registration.registerBodyProvider(new TestProvider(), TileEntityElectricBlock.class);
        registration.registerNBTProvider(new TestProvider(), TileEntityElectricBlock.class);
    }
}

package dev.crossvas.lookingat.ic2c.waila.removals;

import dev.crossvas.lookingat.ic2c.base.LookingAtCommonHandler;
import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IDataWriter;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerAccessor;
import mcp.mobius.waila.api.data.FluidData;
import net.minecraft.world.level.block.entity.BlockEntity;

public class WailaTankInfoRenderer implements IDataProvider<BlockEntity> {

    public static final WailaTankInfoRenderer THIS = new WailaTankInfoRenderer();

    @Override
    public void appendData(IDataWriter iDataWriter, IServerAccessor<BlockEntity> iServerAccessor, IPluginConfig iPluginConfig) {
        if (LookingAtCommonHandler.TANK_REMOVAL.contains(iServerAccessor.getTarget())) {
            iDataWriter.blockAll(FluidData.class);
        }
    }
}

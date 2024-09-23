package dev.crossvas.jadexic2c;

import dev.crossvas.jadexic2c.base.JadeTooltipRenderer;
import dev.crossvas.jadexic2c.providers.CropInfo;
import ic2.core.block.crop.BlockCrop;
import ic2.core.block.crop.TileEntityCrop;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.IWailaRegistrar;
import mcp.mobius.waila.api.WailaPlugin;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;

@WailaPlugin(JadeIC2Classic.ID_IC2)
public class JadePluginHandler implements IWailaPlugin {

    @Override
    public void register(IWailaRegistrar registration) {
        registration.registerStackProvider(CropInfo.CropIconProvider.THIS, BlockCrop.class);
        registration.registerNBTProvider(CropInfo.CropIconProvider.THIS, TileEntityCrop.class);
        registration.registerBodyProvider(JadeTooltipRenderer.THIS, Block.class);
        registration.registerNBTProvider(JadeTooltipRenderer.THIS, TileEntity.class);
    }
}

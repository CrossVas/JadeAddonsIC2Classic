package dev.crossvas.jadexic2c.base;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.List;

public class JadeTooltipRenderer implements IWailaDataProvider {

    public static final JadeTooltipRenderer THIS = new JadeTooltipRenderer();

    @Nonnull
    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> tooltip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        this.addTooltips(tooltip, accessor, config);
        return tooltip;
    }

    public void addTooltips(List<String> tooltip, IWailaDataAccessor accessor, IWailaConfigHandler config) {

    }

    @Nonnull
    @Override
    public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity tileEntity, NBTTagCompound tag, World world, BlockPos pos) {
        JadeHelper helper = new JadeHelper();
        JadeCommonHandler.addInfo(helper, tileEntity, player);
        helper.transferData(tag);
        return tag;
    }
}

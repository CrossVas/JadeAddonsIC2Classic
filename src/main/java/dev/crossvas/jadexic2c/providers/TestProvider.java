package dev.crossvas.jadexic2c.providers;

import dev.crossvas.jadexic2c.utils.Colors;
import ic2.api.classic.tile.machine.IEUStorage;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.SpecialChars;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.List;

public class TestProvider implements IWailaDataProvider {

    @Nonnull
    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> tooltip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        if (accessor.getNBTData().hasKey("EUData")) {
            NBTTagCompound tag = accessor.getNBTData().getCompoundTag("EUData");
            int current = tag.getInteger("currEU");
            int maxEU = tag.getInteger("maxEU");
            String[] energy = new String[5];
            energy[0] = String.valueOf(current);
            energy[1] = String.valueOf(maxEU);
            energy[2] = String.valueOf(Colors.RED);
            energy[3] = String.valueOf(0);
            energy[4] = I18n.format("probe.info.energy", current, maxEU);
            tooltip.add(SpecialChars.getRenderString("jade.progress", energy));

            String[] fluid = new String[5];
            fluid[0] = String.valueOf(current);
            fluid[1] = String.valueOf(maxEU);
            fluid[2] = String.valueOf(Color.WHITE.getRGB());
            fluid[3] = Blocks.WATER.getRegistryName().getPath();
            fluid[4] = Blocks.WATER.getLocalizedName();
            tooltip.add(SpecialChars.getRenderString("jade.progress.fluid", fluid));
        }

        return tooltip;
    }

    @Nonnull
    @Override
    public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity tileEntity, NBTTagCompound tag, World world, BlockPos pos) {
        if (tileEntity instanceof IEUStorage) {
            NBTTagCompound data = new NBTTagCompound();
            data.setInteger("currEU", ((IEUStorage) tileEntity).getStoredEU());
            data.setInteger("maxEU", ((IEUStorage) tileEntity).getMaxEU());
            tag.setTag("EUData", data);
        }
        return tag;
    }
}

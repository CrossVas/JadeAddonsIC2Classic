package dev.crossvas.jadexic2c.providers.transport;

import dev.crossvas.jadexic2c.base.JadeCommonHandler;
import dev.crossvas.jadexic2c.base.interfaces.IInfoProvider;
import dev.crossvas.jadexic2c.base.interfaces.IJadeHelper;
import dev.crossvas.jadexic2c.helpers.FluidContainer;
import ic2.core.block.machines.tiles.lv.PumpTileEntity;
import ic2.core.block.machines.tiles.mv.RangedPumpTileEntity;
import ic2.core.block.transport.fluid.graph.FluidNet;
import ic2.core.block.transport.fluid.graph.IFluidPipe;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class BasicPipeInfo implements IInfoProvider {

    public static final BasicPipeInfo THIS = new BasicPipeInfo();

    @Override
    public void addInfo(IJadeHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof IFluidPipe fluidPipe && !(blockEntity instanceof PumpTileEntity || blockEntity instanceof RangedPumpTileEntity)) {
            JadeCommonHandler.TANK_REMOVAL.add(blockEntity);
            FluidNet.TransportStats stats = FluidNet.INSTANCE.getStats(fluidPipe);
            FluidContainer container = FluidContainer.getContainer(fluidPipe);
            for (Fluid fluid : stats.getTransfered().keySet()) {
                int avg = container.getAverage(fluid);
                if (avg <= 0) continue;
                if (fluid != null) {
                    fluid(helper, new FluidStack(fluid, 1), 1, true);
                }
            }
        }
    }
}

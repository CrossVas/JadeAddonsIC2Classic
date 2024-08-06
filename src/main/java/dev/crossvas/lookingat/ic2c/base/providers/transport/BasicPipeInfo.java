package dev.crossvas.lookingat.ic2c.base.providers.transport;

import dev.crossvas.lookingat.ic2c.base.LookingAtCommonHandler;
import dev.crossvas.lookingat.ic2c.base.interfaces.IInfoProvider;
import dev.crossvas.lookingat.ic2c.base.interfaces.IHelper;
import dev.crossvas.lookingat.ic2c.helpers.FluidContainer;
import ic2.core.block.transport.fluid.graph.FluidNet;
import ic2.core.block.transport.fluid.graph.IFluidPipe;
import ic2.core.block.transport.fluid.tiles.PipeTileEntity;
import ic2.core.block.transport.item.tubes.FluidTubeTileEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class BasicPipeInfo implements IInfoProvider {

    public static final BasicPipeInfo THIS = new BasicPipeInfo();

    @Override
    public void addInfo(IHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof PipeTileEntity pipe) {
            addPipeInfo(helper, pipe);
        }
        if (blockEntity instanceof FluidTubeTileEntity fluidTube) {
            addPipeInfo(helper, fluidTube);
        }
    }

    public void addPipeInfo(IHelper helper, BlockEntity blockEntity) {
        LookingAtCommonHandler.TANK_REMOVAL.add(blockEntity);
        FluidNet.TransportStats stats = FluidNet.INSTANCE.getStats((IFluidPipe) blockEntity);
        FluidContainer container = FluidContainer.getContainer((IFluidPipe) blockEntity);
        for (Fluid fluid : stats.getTransfered().keySet()) {
            int avg = container.getAverage(fluid);
            if (avg <= 0) continue;
            if (fluid != null) {
                fluid(helper, new FluidStack(fluid, 1), 1, true);
            }
        }
    }
}

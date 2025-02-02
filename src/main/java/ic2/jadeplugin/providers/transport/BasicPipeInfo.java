package ic2.jadeplugin.providers.transport;

import ic2.core.block.transport.fluid.graph.FluidNet;
import ic2.core.block.transport.fluid.graph.IFluidPipe;
import ic2.core.block.transport.fluid.tiles.PipeTileEntity;
import ic2.core.block.transport.item.tubes.FluidTubeTileEntity;
import ic2.jadeplugin.base.JadeHelper;
import ic2.jadeplugin.base.interfaces.IInfoProvider;
import ic2.jadeplugin.helpers.FluidContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class BasicPipeInfo implements IInfoProvider {

    public static final BasicPipeInfo THIS = new BasicPipeInfo();

    @Override
    public void addInfo(JadeHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof PipeTileEntity pipe) {
            addPipeInfo(helper, pipe);
        }
        if (blockEntity instanceof FluidTubeTileEntity fluidTube) {
            addPipeInfo(helper, fluidTube);
        }
    }

    public void addPipeInfo(JadeHelper helper, BlockEntity blockEntity) {
        JadeHelper.TANK_REMOVAL.add(blockEntity);
        FluidNet.TransportStats stats = FluidNet.INSTANCE.getStats((IFluidPipe) blockEntity);
        FluidContainer container = FluidContainer.getContainer((IFluidPipe) blockEntity);
        for (Fluid fluid : stats.getTransfered().keySet()) {
            int avg = container.getAverage(fluid);
            if (avg <= 0) continue;
            if (fluid != null) {
                helper.fluid(new FluidStack(fluid, 1), 1, true);
            }
        }
    }
}

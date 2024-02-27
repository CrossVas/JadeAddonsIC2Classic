//package dev.crossvas.jadexic2c.info.pipes;
//
//import com.google.common.cache.Cache;
//import com.google.common.cache.CacheBuilder;
//import dev.crossvas.jadexic2c.JadeIC2CPluginHandler;
//import dev.crossvas.jadexic2c.helpers.IHelper;
//import dev.crossvas.jadexic2c.helpers.PluginHelper;
//import dev.crossvas.jadexic2c.helpers.TextHelper;
//import ic2.core.block.transport.fluid.graph.FluidNet;
//import ic2.core.block.transport.fluid.tiles.PipeTileEntity;
//import ic2.core.utils.collection.LongAverager;
//import net.minecraft.ChatFormatting;
//import net.minecraft.core.BlockPos;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.nbt.ListTag;
//import net.minecraft.nbt.Tag;
//import net.minecraft.network.chat.Component;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.server.level.ServerPlayer;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.entity.BlockEntity;
//import net.minecraft.world.level.material.Fluid;
//import net.minecraftforge.fluids.FluidStack;
//import snownee.jade.api.BlockAccessor;
//import snownee.jade.api.ITooltip;
//import snownee.jade.api.config.IPluginConfig;
//
//import java.util.List;
//import java.util.concurrent.TimeUnit;
//
////TODO: revisit this when it's time to revisit this
//
//public class BasicPipeInfoProvider implements IHelper<BlockEntity> {
//
//    public static final BasicPipeInfoProvider INSTANCE = new BasicPipeInfoProvider();
//    static final Cache<BlockPos, FluidContainer> CACHE;
//
//    @Override
//    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
//        if (!shouldAddInfo(blockAccessor, "BasicPipeInfo")) {
//            return;
//        }
//
//        CompoundTag tag = getData(blockAccessor, "BasicPipeInfo");
//        if (blockAccessor.getBlockEntity() instanceof PipeTileEntity) {
//            ListTag fluidTagList = tag.getList("FluidStacks", Tag.TAG_COMPOUND);
//            int average = tag.getInt("average");
//            if (average > 0) {
//                TextHelper.text(iTooltip, Component.translatable("ic2.probe.pipe.transported").withStyle(ChatFormatting.GOLD));
//                PluginHelper.spacerY(iTooltip, 0);
//                for (Tag fluidTag : fluidTagList) {
//                    CompoundTag fluidStackTag = (CompoundTag) fluidTag;
//                    FluidStack fluidStack = FluidStack.loadFluidStackFromNBT(fluidStackTag.getCompound("fluidStack"));
//                    iTooltip.append(iTooltip.getElementHelper().fluid(fluidStack));
//                    TextHelper.text(iTooltip, Component.literal("Average: " + average));
//                }
//            }
//        }
//    }
//
//    @Override
//    public void appendServerData(CompoundTag compoundTag, ServerPlayer serverPlayer, Level level, BlockEntity blockEntity, boolean b) {
//        if (blockEntity instanceof PipeTileEntity pipe) {
//            CompoundTag tag = new CompoundTag();
//            FluidNet.TransportStats stats = FluidNet.INSTANCE.getStats(pipe);
//            ListTag fluidTagList = new ListTag();
//            for (Fluid fluid : stats.getTransfered().keySet()) {
//                CompoundTag fluidStackTag = new CompoundTag();
//                FluidStack fluidStack = new FluidStack(fluid, 1);
//                fluidStackTag.put("fluidStack", fluidStack.writeToNBT(new CompoundTag()));
//                fluidTagList.add(fluidStackTag);
//                FluidContainer container = FluidContainer.getContainer(pipe, fluid);
//                tag.putInt("average", container.getAverage());
//            }
//            if (!fluidTagList.isEmpty()) {
//                tag.put("FluidStacks", fluidTagList);
//            }
//
//            tag.putLong("totalTransferred", stats.getTotalTransfered());
//            compoundTag.put("BasicPipeInfo", tag);
//        }
//    }
//
//    @Override
//    public ResourceLocation getUid() {
//        return JadeIC2CPluginHandler.EU_READER_INFO;
//    }
//
//    static {
//        CACHE = CacheBuilder.newBuilder().expireAfterAccess(1L, TimeUnit.SECONDS).maximumSize(128L).build();
//    }
//
//    public static class FluidContainer {
//        LongAverager energyIn = new LongAverager(5);
//        Fluid fluid;
//
//        long lastTime;
//        long lastIn;
//
//        public FluidContainer() {}
//
//        public static FluidContainer getContainer(PipeTileEntity tile, Fluid fluid) {
//
//            FluidContainer result = CACHE.getIfPresent(tile.getPosition());
//            if (result == null) {
//                result = new FluidContainer();
//                CACHE.put(tile.getPosition(), result);
//            }
//
//            result.tick(tile.getWorldObj().getGameTime(), tile, fluid);
//            return result;
//        }
//
//        public void tick(long time, PipeTileEntity pipe, Fluid fluid) {
//            FluidNet.TransportStats stat = FluidNet.INSTANCE.getStats(pipe);
//            if (this.lastTime == 0L) {
//                this.lastTime = time;
//                this.lastIn = stat.getTransfered().getLong(fluid);
//            } else {
//                long diff = time - this.lastTime;
//                if (diff <= 0L) {
//                    return;
//                }
//                this.energyIn.addEntry((stat.getTransfered().getLong(fluid) - this.lastIn) / diff);
//                this.lastTime = time;
//                this.lastIn = stat.getTransfered().getLong(fluid);
//            }
//        }
//
//        public int getAverage() {
//            return (int) this.energyIn.getAverage();
//        }
//    }
//}

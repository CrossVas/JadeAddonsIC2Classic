package dev.crossvas.jadexic2c.info;

import dev.crossvas.jadexic2c.JadeIC2CPluginHandler;
import dev.crossvas.jadexic2c.info.removals.ModNameRender;
import ic2.core.block.cables.CableBlock;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.Identifiers;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.util.ModIdentification;

public class CableInfoSpecialProvider implements IBlockComponentProvider {

    public static final CableInfoSpecialProvider INSTANCE = new CableInfoSpecialProvider();

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        Block block = blockAccessor.getBlock();
        if (block instanceof CableBlock cableBlock) {
            BlockState state = blockAccessor.getBlockState();
            boolean foamed = state.getValue(CableBlock.FOAMED) == 2;
            if (foamed) {
                ItemStack fakeStack = cableBlock.getCloneItemStack(state, blockAccessor.getHitResult(), blockAccessor.getLevel(), blockAccessor.getPosition(), blockAccessor.getPlayer());
                iTooltip.remove(Identifiers.CORE_MOD_NAME);
                iTooltip.remove(Identifiers.CORE_OBJECT_NAME);
                iTooltip.remove(ModNameRender.RELOCATE);
                String fakeModName = ModIdentification.getModName(fakeStack);
                String fakeModNameFormatted = String.format(iPluginConfig.getWailaConfig().getFormatting().getModName(), fakeModName);
                iTooltip.add(0, iPluginConfig.getWailaConfig().getFormatting().title(fakeStack.getHoverName()));
                iTooltip.add(1, Component.literal(fakeModNameFormatted));
            }
        }
    }

    @Override
    public ResourceLocation getUid() {
        return JadeIC2CPluginHandler.TOOLTIP_RENDERER;
    }
}

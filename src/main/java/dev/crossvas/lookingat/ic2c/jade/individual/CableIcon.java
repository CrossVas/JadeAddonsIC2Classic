package dev.crossvas.lookingat.ic2c.jade.individual;

import dev.crossvas.lookingat.ic2c.LookingAtTags;
import dev.crossvas.lookingat.ic2c.jade.removals.ModNameRender;
import ic2.core.block.cables.CableBlock;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec2;
import snownee.jade.addon.harvest.HarvestToolProvider;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.Identifiers;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.IElement;
import snownee.jade.impl.ui.TextElement;
import snownee.jade.util.ModIdentification;

import java.util.List;

public class CableIcon implements IBlockComponentProvider {

    public static final CableIcon THIS = new CableIcon();

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (blockAccessor.getBlock() instanceof CableBlock cableBlock) {
            BlockState state = blockAccessor.getBlockState();
            boolean foamed = state.getValue(CableBlock.FOAMED) == 2;
            if (foamed) {
                List<IElement> elements = HarvestToolProvider.INSTANCE.getText(blockAccessor, iPluginConfig, iTooltip.getElementHelper());
                ItemStack fakeStack = cableBlock.getCloneItemStack(state, blockAccessor.getHitResult(), blockAccessor.getLevel(), blockAccessor.getPosition(), blockAccessor.getPlayer());
                iTooltip.remove(Identifiers.MC_HARVEST_TOOL);
                iTooltip.remove(Identifiers.CORE_OBJECT_NAME);

                Component fakeNameComponent = fakeStack.getHoverName().copy().withStyle(ChatFormatting.WHITE);
                iTooltip.add(0, new TextElement(fakeNameComponent).size(new Vec2(Minecraft.getInstance().font.width(fakeNameComponent.getString()) + 15, Minecraft.getInstance().font.lineHeight)));
                elements.forEach(element -> iTooltip.append(0, element.align(IElement.Align.RIGHT)));
                String fakeModName = ModIdentification.getModName(fakeStack);
                String fakeModNameFormatted = String.format(iPluginConfig.getWailaConfig().getFormatting().getModName(), fakeModName);
                iTooltip.add(Component.literal(fakeModNameFormatted));
                iTooltip.remove(ModNameRender.RELOCATE);
            }
        }
    }

    @Override
    public ResourceLocation getUid() {
        return LookingAtTags.INFO_RENDERER;
    }
}


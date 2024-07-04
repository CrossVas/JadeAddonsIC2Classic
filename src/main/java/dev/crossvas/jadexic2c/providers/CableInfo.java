package dev.crossvas.jadexic2c.providers;

import dev.crossvas.jadexic2c.JadeTags;
import dev.crossvas.jadexic2c.base.interfaces.IInfoProvider;
import dev.crossvas.jadexic2c.base.interfaces.IJadeHelper;
import dev.crossvas.jadexic2c.base.removals.ModNameRender;
import dev.crossvas.jadexic2c.helpers.EnergyContainer;
import ic2.core.block.cables.CableBlock;
import ic2.core.block.cables.CableTileEntity;
import ic2.core.utils.helpers.Formatters;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
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

public class CableInfo implements IInfoProvider {

    public static final CableInfo THIS = new CableInfo();

    @Override
    public void addInfo(IJadeHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof CableTileEntity cable) {
            defaultText(helper, "tooltip.item.ic2.eu_reader.cable_limit", cable.getConductorBreakdownEnergy() - 1);
            defaultText(helper, "tooltip.item.ic2.eu_reader.cable_loss", Formatters.CABLE_LOSS_FORMAT.format(cable.getConductionLoss()));
            EnergyContainer container = EnergyContainer.getContainer(cable);
            addCableAverages(helper, container.getAverageOut(), container.getPacketsOut());
        }
    }

    public static class CableIconProvider implements IBlockComponentProvider {

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
            return JadeTags.INFO_RENDERER;
        }
    }
}

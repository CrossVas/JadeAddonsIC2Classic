package ic2.jadeplugin.providers;

import ic2.core.block.cables.CableBlock;
import ic2.core.block.cables.CableTileEntity;
import ic2.core.utils.helpers.Formatters;
import ic2.core.utils.tooltips.ILangHelper;
import ic2.jadeplugin.JadeTags;
import ic2.jadeplugin.base.JadeHelper;
import ic2.jadeplugin.base.interfaces.IInfoProvider;
import ic2.jadeplugin.base.removals.ModNameRender;
import ic2.jadeplugin.helpers.EnergyContainer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec2;
import snownee.jade.Jade;
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
    public void addInfo(JadeHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof CableTileEntity cable) {
            int maxCap = cable.getConductorBreakdownEnergy() - 1;
            helper.tierFromPower(maxCap);
            helper.defaultText("tooltip.item.ic2.eu_reader.cable_limit", maxCap);
            helper.defaultText("tooltip.item.ic2.eu_reader.cable_loss", Formatters.CABLE_LOSS_FORMAT.format(cable.getConductionLoss()));
            EnergyContainer container = EnergyContainer.getContainer(cable);
            helper.addCableAverages(container.getAverageOut(), container.getPacketsOut());
        }
    }

    public static class CableIconProvider implements IBlockComponentProvider, ILangHelper {

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
                    String fakeModNameFormatted = String.format(Jade.CONFIG.get().getFormatting().getModName(), fakeModName);
                    iTooltip.add(string(fakeModNameFormatted));
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

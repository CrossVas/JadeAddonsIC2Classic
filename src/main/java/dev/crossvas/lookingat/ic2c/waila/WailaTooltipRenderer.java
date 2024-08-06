package dev.crossvas.lookingat.ic2c.waila;

import dev.crossvas.lookingat.ic2c.LookingAtTags;
import dev.crossvas.lookingat.ic2c.base.LookingAtCommonHandler;
import dev.crossvas.lookingat.ic2c.base.LookingAtHelper;
import dev.crossvas.lookingat.ic2c.base.elements.*;
import dev.crossvas.lookingat.ic2c.helpers.Formatter;
import dev.crossvas.lookingat.ic2c.helpers.PluginHelper;
import dev.crossvas.lookingat.ic2c.waila.elements.CustomWailaItemComponent;
import mcp.mobius.waila.api.*;
import mcp.mobius.waila.api.component.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec2;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;

import java.text.DecimalFormat;
import java.util.Objects;
import java.util.function.Function;

import static dev.crossvas.lookingat.ic2c.LookingAtTags.*;

public class WailaTooltipRenderer implements IBlockComponentProvider, IEntityComponentProvider, IDataProvider<BlockEntity> {

    public static final WailaTooltipRenderer INSTANCE = new WailaTooltipRenderer();

    @Override
    public void appendBody(ITooltip tooltip, IBlockAccessor accessor, IPluginConfig config) {
        appendTooltips(tooltip, accessor);
    }

    @Override
    public void appendBody(ITooltip tooltip, IEntityAccessor accessor, IPluginConfig config) {
        appendTooltips(tooltip, (IBlockAccessor) accessor);
    }

    private void appendTooltips(ITooltip tooltip, IBlockAccessor accessor) {
        CompoundTag serverData = accessor.getData().raw();
        if (serverData.contains(LookingAtTags.TAG_DATA, Tag.TAG_LIST)) {
            ListTag tagList = serverData.getList(LookingAtTags.TAG_DATA, Tag.TAG_COMPOUND);
            for (int i = 0; i < tagList.size(); i++) {
                CompoundTag serverTag = tagList.getCompound(i);
                // padding
                if (serverTag.contains(LOOKING_AT_PADDING_TAG)) {
                    CompoundTag elementTag = serverTag.getCompound(LOOKING_AT_PADDING_TAG);
                    CommonPaddingElement paddingElement = CommonPaddingElement.load(elementTag);
                    int x = paddingElement.getX();
                    int y = paddingElement.getY();
                    ITooltipComponent wailaComponent = new SpacingComponent(x, y);
                    addElement(tooltip, wailaComponent, elementTag);
                }
                // text
                if (serverTag.contains(LOOKING_AT_TEXT_TAG)) {
                    CompoundTag elementTag = serverTag.getCompound(LOOKING_AT_TEXT_TAG);
                    CommonTextElement textElement = CommonTextElement.load(elementTag);
                    boolean centered = textElement.isCentered();
                    ITooltipComponent wailaComponent = new WrappedComponent(textElement.getText());
                    if (centered) {
                        tooltip.addLine().with(GrowingComponent.INSTANCE).with(textElement.getText()).with(GrowingComponent.INSTANCE);
                    } else {
                        addElement(tooltip, wailaComponent, elementTag);
                    }
                }
                // bar
                if (serverTag.contains(LOOKING_AT_BAR_TAG)) {
                    CompoundTag elementTag = serverTag.getCompound(LOOKING_AT_BAR_TAG);
                    CommonBarElement barElement = CommonBarElement.load(elementTag);
                    int color = barElement.getColor();
                    int current = barElement.getCurrent();
                    int max = barElement.getMax();
                    Component label = barElement.getText();
                    Component newLabel = WailaHelper.getWailaComp(label);
                    Component args = Component.literal(Formatter.formatInt(current, 4) + (max == 0 ? "" : " / " + Formatter.formatInt(max, 4)));
                    if (Objects.equals(newLabel, Component.empty())) {
                        args = label;
                        tooltip.addLine().with(new BarComponent((float) current / max, color, args));
                    } else if (WailaHelper.SPEED_COMP.contains(label)) {
                        args = Component.literal(new DecimalFormat().format(((float) current / max) * 100.0) + "%");
                        tooltip.addLine(new PairComponent(new WrappedComponent(newLabel), new BarComponent((float) current / max, color, args)));
                    } else {
                        tooltip.addLine(new PairComponent(new WrappedComponent(newLabel), new BarComponent((float) current / max, color, args)));
                    }
                }
                // item
                if (serverTag.contains(LOOKING_AT_ITEM_TAG)) {
                    CompoundTag elementTag = serverTag.getCompound(LOOKING_AT_ITEM_TAG);
                    CommonItemStackElement stackElement = CommonItemStackElement.load(elementTag);
                    ItemStack stack = stackElement.getStack();
                    ITooltipComponent wailaComponent = new CustomWailaItemComponent(stack).size(new Vec2(16, 16)).translate(stackElement.getTranslation());
                    addElement(tooltip, wailaComponent, elementTag);
                }
                // fluid
                if (serverTag.contains(LOOKING_AT_FLUID_TAG)) {
                    CompoundTag elementTag = serverTag.getCompound(LOOKING_AT_FLUID_TAG);
                    CommonFluidBarElement fluidElement = CommonFluidBarElement.load(elementTag);
                    FluidStack fluid = fluidElement.getFluid();
                    int fluidAmount = fluid.getAmount();
                    int max = fluidElement.getMax();
                    boolean ignoreCapacity = fluidElement.ignoreCapacity();
                    if (ignoreCapacity) {
                        fluidAmount = 1;
                        max = 1;
                    }
                    if (fluidAmount > 0) {
                        Function<ResourceLocation, TextureAtlasSprite> map = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS);
                        ResourceLocation stillTexture = IClientFluidTypeExtensions.of(fluid.getFluid()).getStillTexture(fluid);
                        TextureAtlasSprite liquidIcon = map.apply(stillTexture);
                        int color = PluginHelper.getColorForFluid(fluid);
                        tooltip.addLine(new PairComponent(new WrappedComponent(fluid.getDisplayName()),
                                new SpriteBarComponent((float) fluid.getAmount() / max, liquidIcon, 16, 16, color,
                                        Component.literal(Formatter.formatNumber(fluid.getAmount(), String.valueOf(fluid.getAmount()).length() - 1) + " / " + Formatter.formatNumber(max, String.valueOf(max).length() - 1)))));
                    }
                }


            }
        }
    }

    public void addElement(ITooltip iTooltip, ITooltipComponent element, CompoundTag elementTag) {
        boolean add = elementTag.getBoolean(LookingAtHelper.ADD_TAG);
        boolean append = elementTag.getBoolean(LookingAtHelper.APPEND_TAG);
        if (add) {
            iTooltip.addLine(element);
        }
        if (append) {
            iTooltip.getLine(iTooltip.getLineCount() - 1).with(element);
        }
    }

    @Override
    public void appendData(IDataWriter iDataWriter, IServerAccessor<BlockEntity> iServerAccessor, IPluginConfig iPluginConfig) {
        LookingAtHelper helper = new LookingAtHelper();
        LookingAtCommonHandler.addInfo(helper, iServerAccessor.getTarget(), iServerAccessor.getPlayer());
        helper.transferData(iDataWriter.raw());
    }
}

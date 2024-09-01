package dev.crossvas.jadexic2c.base.tooltiprenderers;

import com.google.common.collect.Lists;
import dev.crossvas.jadexic2c.utils.Colors;
import dev.crossvas.jadexic2c.utils.GuiHelper;
import mcp.mobius.waila.api.IWailaCommonAccessor;
import mcp.mobius.waila.api.IWailaTooltipRenderer;
import mcp.mobius.waila.api.impl.ConfigHandler;
import mcp.mobius.waila.config.OverlayConfig;
import mcp.mobius.waila.overlay.Tooltip;
import mcp.mobius.waila.utils.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.common.config.Configuration;

import javax.annotation.Nonnull;
import java.awt.*;
import java.lang.reflect.Field;
import java.util.WeakHashMap;

public class BaseProgressBarRenderer implements IWailaTooltipRenderer {

    private final WeakHashMap<String[], Tooltip> subTooltips = new WeakHashMap<>();
    private static Field FIELD_WIDTH;

    static {
        try {
            FIELD_WIDTH = Tooltip.class.getDeclaredField("w");
            FIELD_WIDTH.setAccessible(true);
        } catch (NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public @Nonnull Dimension getSize(@Nonnull String[] params, @Nonnull IWailaCommonAccessor iWailaCommonAccessor) {
        Tooltip tooltip;
        if (subTooltips.containsKey(params)) {
            tooltip = subTooltips.get(params);
        } else {
            tooltip = new Tooltip(Lists.newArrayList(params), false);
            subTooltips.put(params, tooltip);
        }
        int width;
        try {
            width = FIELD_WIDTH.getInt(tooltip);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
            return new Dimension();
        }
        return new Dimension(width, 13 + 2);
    }

    @Override
    public void draw(@Nonnull String[] strings, @Nonnull IWailaCommonAccessor iWailaCommonAccessor) {
        Tooltip tooltip = subTooltips.get(strings);
        if (tooltip != null) {
            int width;
            int height = 13;
            try {
                width = FIELD_WIDTH.getInt(tooltip);
            } catch (IllegalAccessException | IllegalArgumentException exception) {
                exception.printStackTrace();
                return;
            }
            Point pos = new Point(ConfigHandler.instance().getConfig(Configuration.CATEGORY_GENERAL, Constants.CFG_WAILA_POSX, 0), ConfigHandler.instance().getConfig(Configuration.CATEGORY_GENERAL, Constants.CFG_WAILA_POSY, 0));
            ScaledResolution resolution = new ScaledResolution(Minecraft.getMinecraft());
            int x = ((int) (resolution.getScaledWidth() / OverlayConfig.scale) - width - 1) * pos.x / 10000;
            int y = ((int) (resolution.getScaledHeight() / OverlayConfig.scale) - height - 1) * pos.y / 10000;
            GlStateManager.enableBlend();
            int color = Color.WHITE.getRGB();
            Gui.drawRect(0, 0, 1, height, color);
            Gui.drawRect(1, 0, width + 1, 1, color);
            Gui.drawRect(width, 0, width + 1, height, color);
            Gui.drawRect(0, height, width + 1, height + 1, color);
            GlStateManager.translate(-x, -y, 0);
            GuiHelper.THIS.render(Integer.parseInt(strings[0]), Integer.parseInt(strings[1]), x, y, width, height + 1, Colors.valueOf(strings[2]), Integer.parseInt(strings[3]));
            FontRenderer font = Minecraft.getMinecraft().fontRenderer;
            x += width / 2 - font.getStringWidth(strings[4]) / 2 + 1;
            font.drawStringWithShadow(strings[4], x, y + 3, color);
        }
    }
}

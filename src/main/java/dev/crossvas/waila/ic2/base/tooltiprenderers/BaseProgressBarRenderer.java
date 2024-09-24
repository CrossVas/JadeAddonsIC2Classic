package dev.crossvas.waila.ic2.base.tooltiprenderers;

import com.google.common.collect.Lists;
import dev.crossvas.waila.ic2.base.WailaCommonHandler;
import dev.crossvas.waila.ic2.utils.ColorUtils;
import dev.crossvas.waila.ic2.utils.GuiHelper;
import mcp.mobius.waila.api.IWailaCommonAccessor;
import mcp.mobius.waila.api.IWailaTooltipRenderer;
import mcp.mobius.waila.api.impl.ConfigHandler;
import mcp.mobius.waila.overlay.OverlayConfig;
import mcp.mobius.waila.overlay.Tooltip;
import mcp.mobius.waila.overlay.WailaTickHandler;
import mcp.mobius.waila.utils.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.common.config.Configuration;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.awt.*;
import java.lang.reflect.Field;
import java.util.WeakHashMap;

@ParametersAreNonnullByDefault
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

    @Nonnull
    @Override
    public Dimension getSize(String[] strings, IWailaCommonAccessor iWailaCommonAccessor) {
        Tooltip tooltip;
        if (this.subTooltips.containsKey(strings)) {
            tooltip = this.subTooltips.get(strings);
        } else {
            tooltip = new Tooltip(Lists.newArrayList(strings), false);
            this.subTooltips.put(strings, WailaTickHandler.instance().tooltip);
        }
        int width;
        int height;
        if ("1".equals(strings[4])) {
            height = 10;
        } else {
            height = 14;
        }
        try {
            width = FIELD_WIDTH.getInt(tooltip);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
            return new Dimension();
        }
        return new Dimension(width, height);
    }

    @Override
    public void draw(String[] strings, IWailaCommonAccessor iWailaCommonAccessor) {

        Tooltip tooltip = subTooltips.get(strings);
        if (tooltip != null) {
            int width;
            int height = 13;
            try {
                width = FIELD_WIDTH.getInt(tooltip) - 37;
            } catch (IllegalAccessException | IllegalArgumentException exception) {
                exception.printStackTrace();
                return;
            }

            Point pos = new Point(ConfigHandler.instance().getConfig(Configuration.CATEGORY_GENERAL, Constants.CFG_WAILA_POSX, 0), ConfigHandler.instance().getConfig(Configuration.CATEGORY_GENERAL, Constants.CFG_WAILA_POSY, 0));
            Minecraft mc = Minecraft.getMinecraft();
            ScaledResolution resolution = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
            int x = ((int) (resolution.getScaledWidth() / OverlayConfig.scale) - width - 1) * pos.x / 10000;
            int y = ((int) (resolution.getScaledHeight() / OverlayConfig.scale) - height - 1) * pos.y / 10000;
            GL11.glEnable(3042); // GL_BLEND

            GL11.glTranslated(-x, -y, 0);
            int current = Integer.parseInt(strings[0]);
            int max = Integer.parseInt(strings[1]);
            int color = Integer.parseInt(strings[2]);
            String text = strings[3];
            boolean isStringOnly = "1".equals(strings[4]);
            String fluidIcon = WailaCommonHandler.MAPPED_FLUIDS.getOrDefault(strings[6], "0");
            FontRenderer font = Minecraft.getMinecraft().fontRenderer;
            if (!isStringOnly) {
                GuiHelper.THIS.render(current, max, x, y, width, height + 1, color, fluidIcon);
                x += width / 2 - font.getStringWidth(text) / 2 + 1;
            }
            boolean centered = "1".equals(strings[5]);
            if (centered) {
                x += width / 2 - font.getStringWidth(text) / 2 + 1;
            }
            font.drawStringWithShadow(text, x, y + 3, ColorUtils.WHITE);
        }
    }
}

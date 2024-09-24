package dev.crossvas.waila.ic2.utils;

public class ColorUtils {
    static final float DIVIDER = 0.003921569F;
    public static final int WHITE = -1;
    public static final int LIGHT_GRAY = rgb(192, 192, 192);
    public static final int GRAY = rgb(128, 128, 128);
    public static final int DARK_GRAY = rgb(64, 64, 64);
    public static final int BLACK = -16777216;
    public static final int PROGRESS = -16733185;
    public static final int SPEED = -295680;
    public static final int RED = rgb(255, 0, 0);
    public static final int PINK = rgb(255, 175, 175);
    public static final int ORANGE = rgb(255, 200, 0);
    public static final int YELLOW = rgb(255, 255, 0);
    public static final int GREEN = rgb(0, 255, 0);
    public static final int MAGENTA = rgb(255, 0, 255);
    public static final int CYAN = rgb(0, 255, 255);
    public static final int BLUE = rgb(0, 0, 255);

    public ColorUtils() {
    }

    public static int rgb(int r, int g, int b) {
        return rgb(r, g, b, 255);
    }

    public static int rgb(int r, int g, int b, int a) {
        return (a & 255) << 24 | (r & 255) << 16 | (g & 255) << 8 | b & 255;
    }

    public static int rgb(float r, float g, float b) {
        return rgb(r, g, b, 1.0F);
    }

    public static int rgb(float r, float g, float b, float a) {
        return rgb((int)((double)(r * 255.0F) + 0.5), (int)((double)(g * 255.0F) + 0.5), (int)((double)(b * 255.0F) + 0.5), (int)((double)(a * 255.0F) + 0.5));
    }

    public static float[] rgbFloat(int color) {
        return new float[]{getFloatR(color), getFloatG(color), getFloatB(color)};
    }

    public static int fromFluid(int color) {
        float r = ((color >> 16) & 0xFF) / 255f; // red
        float g = ((color >> 8) & 0xFF) / 255f; // green
        float b = ((color) & 0xFF) / 255f; // blue
        float a = ((color >> 24) & 0xFF) / 255f; // alpha
        return rgb(r, g, b, a);
    }

    public static int getRed(int rgb) {
        return rgb >> 16 & 255;
    }

    public static int getGreen(int rgb) {
        return rgb >> 8 & 255;
    }

    public static int getBlue(int rgb) {
        return rgb & 255;
    }

    public static int getAlpha(int rgb) {
        return rgb >> 24 & 255;
    }

    public static float getFloatR(int rgb) {
        return (float)(rgb >> 16 & 255) * 0.003921569F;
    }

    public static float getFloatG(int rgb) {
        return (float)(rgb >> 8 & 255) * 0.003921569F;
    }

    public static float getFloatB(int rgb) {
        return (float)(rgb & 255) * 0.003921569F;
    }

    public static float getFloatA(int rgb) {
        return (float)(rgb >> 24 & 255) * 0.003921569F;
    }

    public static boolean needsDarkColor(int rgba) {
        return getBrightness(rgba) >= 130;
    }

    public static int getBrightness(int rgba) {
        return getBrightness(rgba >> 16 & 255, rgba >> 8 & 255, rgba & 255);
    }

    public static int getBrightness(int r, int g, int b) {
        return (int)Math.sqrt((double)((float)(r * r) * 0.241F + (float)(g * g) * 0.691F + (float)(b * b) * 0.068F));
    }

    public static int mixColors(int from, int to, float progress) {
        float weight0 = 1.0F - progress;
        float weight1 = progress;
        int r = (int)((float)(from >> 16 & 255) * weight0 + (float)(to >> 16 & 255) * weight1);
        int g = (int)((float)(from >> 8 & 255) * weight0 + (float)(to >> 8 & 255) * weight1);
        int b = (int)((float)(from & 255) * weight0 + (float)(to & 255) * weight1);
        int a = (int)((float)(from >> 24 & 255) * weight0 + (float)(to >> 24 & 255) * weight1);
        return (a & 255) << 24 | (r & 255) << 16 | (g & 255) << 8 | b & 255;
    }

    public static int doubleDarker(int color) {
        return darker(darker(color));
    }

    public static int trippleDarker(int color) {
        return darker(darker(darker(color)));
    }

    public static int darker(int color) {
        return darker(color, 0.7F);
    }

    public static int darker(int color, float factor) {
        if (Float.compare(factor, 1.0F) == 0) {
            return color;
        } else {
            int r = Math.max(0, (int)((float)(color >> 16 & 255) * factor));
            int g = Math.max(0, (int)((float)(color >> 8 & 255) * factor));
            int b = Math.max(0, (int)((float)(color & 255) * factor));
            return color & -16777216 | (r & 255) << 16 | (g & 255) << 8 | b & 255;
        }
    }

    public static int doubleBrighter(int color) {
        return brighter(brighter(color));
    }

    public static int brighter(int color) {
        return brighter(color, 0.7F);
    }


    public static int brighter(int color, float factor) {
        if (Float.compare(factor, 1.0F) == 0) {
            return color;
        } else {
            int r = color >> 16 & 255;
            int g = color >> 8 & 255;
            int b = color & 255;
            int i = (int)(1.0 / (1.0 - (double)factor));
            if (r == 0 && g == 0 && b == 0) {
                return color & -16777216 | (i & 255) << 16 | (i & 255) << 8 | i & 255;
            } else {
                if (r > 0 && r < i) {
                    r = i;
                }

                if (g > 0 && g < i) {
                    g = i;
                }

                if (b > 0 && b < i) {
                    b = i;
                }

                return color & -16777216 | Math.min(255, (int)((float)r / factor)) << 16 | Math.min(255, (int)((float)g / factor)) << 8 | Math.min(255, (int)((float)b / factor));
            }
        }
    }

    public static String toHex(int color) {
        String var10000 = Integer.toHexString(16777216 | color & 16777215);
        return "#" + var10000.substring(1);
    }
}

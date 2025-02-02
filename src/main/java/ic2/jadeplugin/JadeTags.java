package ic2.jadeplugin;

import net.minecraft.resources.ResourceLocation;

public class JadeTags {

    public static final String TAG_DATA = "Data";
    public static final String TAG_STRUCTURE = "Structure";
    public static final String TAG_TANKS = "TankRemoval";

    // info providers
    public static final ResourceLocation INFO_RENDERER = IC2JadePlugin.rl("renderer");
    public static final ResourceLocation WRENCHABLE = IC2JadePlugin.rl("wrenchable_info");
    public static final ResourceLocation TANK_RENDER = IC2JadePlugin.rl("remove_renders_fluid");
    // general config
    public static final ResourceLocation TOP_STYLE = IC2JadePlugin.rl("force_top_style");
    public static final ResourceLocation SNEAK_FOR_DETAILS = IC2JadePlugin.rl("sneak_for_details");

    public static final String JADE_ADDON_TEXT_TAG = "jade_addon_text";
    public static final String JADE_ADDON_ITEM_TAG = "jade_addon_item";
    public static final String JADE_ADDON_FLUID_TAG = "jade_addon_fluid";
    public static final String JADE_ADDON_BAR_TAG = "jade_addon_bar";
    public static final String JADE_ADDON_PADDING_TAG = "jade_addon_padding";
}

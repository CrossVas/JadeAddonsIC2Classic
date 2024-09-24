package dev.crossvas.waila.ic2;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.ResourceLocation;

@Mod(modid = Refs.ID, name = Refs.NAME, version = Refs.VERSION, acceptedMinecraftVersions = Refs.MC, dependencies = Refs.DEPS)
public class WailaIC2Classic {

    public static final String ID_IC2 = "ic2";

    public WailaIC2Classic() {}

    public static ResourceLocation rl(String path) {
        return new ResourceLocation(ID_IC2, path);
    }

    @SideOnly(Side.CLIENT)
    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        FMLInterModComms.sendMessage("Waila", "register", "dev.crossvas.waila.ic2.WailaPluginHandler.register");
    }
}

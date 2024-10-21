package dev.crossvas.waila.ic2;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import dev.crossvas.waila.ic2.utils.TextFormatter;
import ic2.api.recipe.Recipes;
import ic2.core.IC2;
import ic2.core.Ic2Items;
import ic2.core.item.armor.ItemArmorNanoSuit;
import ic2.core.item.armor.ItemArmorQuantumSuit;
import ic2.core.util.StackUtil;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

@Mod(modid = Refs.ID, name = Refs.NAME, version = Refs.VERSION, acceptedMinecraftVersions = Refs.MC, dependencies = Refs.DEPS)
public class WailaIC2Classic {

    public static final String PROBE_ID = "probe";

    public static Item PROBE;

    public WailaIC2Classic() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        PROBE = new Item().setMaxStackSize(1).setCreativeTab(IC2.tabIC2).setNoRepair().setTextureName(Refs.ID + ":" + PROBE_ID).setUnlocalizedName(PROBE_ID);
        GameRegistry.registerItem(PROBE, PROBE_ID);
        FMLInterModComms.sendMessage("Waila", "register", "dev.crossvas.waila.ic2.WailaPluginHandler.register");
        ItemStack nano = Ic2Items.nanoHelmet.copy();
        ItemStack quant = Ic2Items.quantumHelmet.copy();
        Recipes.advRecipes.addShapelessRecipe(probeHelmet(nano), StackUtil.copyWithWildCard(nano), PROBE);
        Recipes.advRecipes.addShapelessRecipe(probeHelmet(quant), StackUtil.copyWithWildCard(quant), PROBE);
        Recipes.advRecipes.addRecipe(new ItemStack(PROBE),
            "CGC", "R#R", "CCC",
            'C', Ic2Items.insulatedCopperCableItem.copy(),
            'G', Items.glowstone_dust,
            '#', Ic2Items.electronicCircuit,
            'R', Items.redstone);
    }

    public static ItemStack probeHelmet(ItemStack stack) {
        ItemStack helmet = stack.copy();
        NBTTagCompound tag = StackUtil.getOrCreateNbtData(helmet);
        tag.setBoolean(PROBE_ID, true);
        return helmet;
    }

    @SubscribeEvent
    public void tooltipEvent(ItemTooltipEvent e) {
        ItemStack stack = e.itemStack;
        if (stack.getItem() instanceof ItemArmorNanoSuit || stack.getItem() instanceof ItemArmorQuantumSuit) {
            NBTTagCompound tag = StackUtil.getOrCreateNbtData(stack);
            if (tag.hasKey(PROBE_ID)) {
                e.toolTip.add(TextFormatter.GOLD.literal("Installed modules:").getFormattedText());
                e.toolTip.add(TextFormatter.AQUA.translate("item.probe.name").getFormattedText());
            }
        }
    }
}

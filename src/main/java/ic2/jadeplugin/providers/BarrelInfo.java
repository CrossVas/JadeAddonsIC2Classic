package ic2.jadeplugin.providers;

import ic2.core.block.misc.tiles.BarrelTileEntity;
import ic2.core.inventory.filter.IFilter;
import ic2.core.platform.registries.IC2Blocks;
import ic2.core.utils.math.ColorUtils;
import ic2.jadeplugin.JadeTags;
import ic2.jadeplugin.base.JadeHelper;
import ic2.jadeplugin.base.interfaces.IInfoProvider;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.IElement;
import snownee.jade.api.ui.IElementHelper;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class BarrelInfo implements IInfoProvider {

    public static final BarrelInfo THIS = new BarrelInfo();

    @Override
    public IFilter getFilter() {
        return THERMOMETER;
    }

    @Override
    public void addInfo(JadeHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof BarrelTileEntity barrelTile) {
            // common
            int brewQuality = barrelTile.beerQuality;
            int alcoholLevel = barrelTile.getAlcoholLevel();
            int solidRatio = barrelTile.getSolidRatio();

            // 1
            int wheatAmount = barrelTile.wheatAmount;
            int hopsAmount = barrelTile.hopsAmount;
            int fluidAmount = barrelTile.fluidAmount;

            int age;
            double maxValue;
            double current;
            int maxFluidCapacity = BarrelTileEntity.FLUID_CAPACITY;
            int maxPotionCapacity = BarrelTileEntity.POTION_FLUID_CAPACITY;

            DecimalFormat format = new DecimalFormat("#0.00", new DecimalFormatSymbols(Locale.ROOT));
            FluidStack waterStack = new FluidStack(Fluids.WATER, fluidAmount);

            int brewType = barrelTile.brewType;
            switch (brewType) {
                case 1:
                    age = barrelTile.age;
                    maxValue = 24000.0 * Math.pow(3.0, brewQuality == 4 ? 6.0 : (double) brewQuality);
                    current = age / maxValue * 100.0;

                    helper.defaultText(getBrewType(brewType));
                    helper.centered(translate("ic2.probe.barrel.status.storage.name").withStyle(ChatFormatting.YELLOW));
                    helper.bar(wheatAmount, 64, translate("ic2.probe.barrel.beer.wheat.name", wheatAmount), ColorUtils.YELLOW);
                    helper.bar(hopsAmount, 64, translate("ic2.probe.barrel.beer.hops.name", hopsAmount), ColorUtils.GREEN);
                    helper.fluid(waterStack, maxFluidCapacity);

                    helper.centered(translate("ic2.probe.barrel.status.brew.name").withStyle(ChatFormatting.YELLOW));
                    helper.text(translate("ic2.probe.barrel.beer.quality." + brewQuality + ".name"));
                    helper.text(translate("ic2.probe.barrel.beer.alc." + alcoholLevel + ".name"));
                    helper.text(translate("ic2.probe.barrel.beer.solid." + solidRatio + ".name"));
                    helper.bar(age, (int) maxValue, string(format.format(current) + "%"), -16733185);
                    break;
                case 2:
                    maxValue = barrelTile.timeNeededForRum();
                    age = (int) Math.min(barrelTile.age, maxValue);
                    helper.defaultText(getBrewType(brewType));
                    helper.centered(translate("ic2.probe.barrel.status.brew.name").withStyle(ChatFormatting.YELLOW));
                    helper.bar(fluidAmount / 1000, 32, translate("ic2.probe.barrel.beer.sugar_cane.name", fluidAmount / 1000), ColorUtils.GREEN);
                    helper.bar(age, (int) maxValue, string(format.format(Math.min(age, maxValue) * 100.0 / maxValue) + "%"), -16733185);
                    break;
                case 5:
                    double ageWhisky = barrelTile.age;
                    int whiskyBrewTime = barrelTile.getWhiskBrewTime();
                    helper.defaultText(getBrewType(brewType));
                    helper.centered(translate("ic2.probe.barrel.status.storage.name").withStyle(ChatFormatting.YELLOW));
                    helper.bar(hopsAmount, 16, translate("ic2.probe.barrel.whisky.grist.name", hopsAmount), ColorUtils.GREEN);
                    helper.fluid(waterStack, maxFluidCapacity);
                    helper.centered(translate("ic2.probe.barrel.status.brew.name").withStyle(ChatFormatting.YELLOW));
                    helper.bar(Math.min(brewQuality, 50), 50, translate("ic2.probe.barrel.whisky.years.name", Math.min(brewQuality, 50)), -16733185);
                    helper.bar((int) ageWhisky, 1728000, string(format.format(ageWhisky / (whiskyBrewTime / 100.0)) + "%"), -16733185);
                    break;
                case 10:
                    helper.defaultText(getBrewType(brewType));
                    helper.centered(translate("ic2.probe.barrel.status.storage.name").withStyle(ChatFormatting.YELLOW));
                    helper.bar(wheatAmount, 20, translate("ic2.probe.barrel.beer.redstone.name", wheatAmount), ColorUtils.RED);
                    helper.bar(hopsAmount, 20, translate("ic2.probe.barrel.beer.glowstone.name", hopsAmount), ColorUtils.YELLOW);
                    helper.fluid(waterStack, maxPotionCapacity);
                    helper.centered(translate("ic2.probe.barrel.status.brew.name").withStyle(ChatFormatting.YELLOW));
                    int brewedPotion = MobEffect.getId(barrelTile.potionType);
                    Component potionID = brewedPotion == -1 ? translate("tooltip.block.ic2.barrel.unknown") : barrelTile.potionType.getDisplayName();
                    helper.defaultText("ic2.probe.barrel.status.output.name", potionID);
                    helper.defaultText("ic2.probe.barrel.potion.quality." + brewQuality + ".name", brewQuality);

                    age = barrelTile.age;
                    maxValue = 5000.0 * Math.pow(3.0, brewQuality);
                    current = age / maxValue;
                    helper.bar(age, (int) maxValue, string(format.format(current * 100.0) + "%"), -16733185);
                    break;
                case 0:
                case 3:
                case 4:
                case 6:
                case 7:
                case 8:
                case 9:
                default:
                    break;
            }
        }
    }

    public String getBrewType(int type) {
        return switch (type) {
            case 0 -> "ic2.probe.barrel.status.empty.name";
            case 1 -> "ic2.probe.barrel.status.beer.name";
            case 2 -> "ic2.probe.barrel.status.rum.name";
            case 5 -> "ic2.probe.barrel.status.whisky.name";
            case 10 -> "ic2.probe.barrel.status.potion.name";
            default -> "I AM ERROR";
        };
    }

    public static class BarrelIconProvider implements IBlockComponentProvider, IServerDataProvider<BlockEntity> {

        @Override
        public @Nullable IElement getIcon(BlockAccessor accessor, IPluginConfig config, IElement currentIcon) {
            ItemStack icon = new ItemStack(IC2Blocks.BARREL);
            if (accessor.getServerData().contains("BarrelInfo")) {
                CompoundTag tag = accessor.getServerData().getCompound("BarrelInfo");
                int brewType = tag.getInt("type");
                icon.getOrCreateTag().putInt("type", brewType);
            }
            return IElementHelper.get().item(icon);
        }

        @Override
        public void appendServerData(CompoundTag compoundTag, ServerPlayer serverPlayer, Level level, BlockEntity blockEntity, boolean b) {
            if (blockEntity instanceof BarrelTileEntity barrel) {
                CompoundTag tag = new CompoundTag();
                tag.putInt("type", barrel.brewType);
                compoundTag.put("BarrelInfo", tag);
            }
        }

        @Override
        public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {}

        @Override
        public ResourceLocation getUid() {
            return JadeTags.INFO_RENDERER;
        }
    }
}

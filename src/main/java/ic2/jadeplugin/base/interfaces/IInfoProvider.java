package ic2.jadeplugin.base.interfaces;

import ic2.core.inventory.filter.IFilter;
import ic2.core.inventory.filter.SpecialFilters;
import ic2.core.utils.helpers.StackUtil;
import ic2.core.utils.tooltips.ILangHelper;
import ic2.jadeplugin.base.JadeHelper;
import ic2.jadeplugin.helpers.TextFormatter;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public interface IInfoProvider extends ILangHelper {

    IFilter READER = SpecialFilters.EU_READER;
    IFilter THERMOMETER = SpecialFilters.THERMOMETER;
    IFilter CROP_ANALYZER = SpecialFilters.CROP_SCANNER;
    IFilter ALWAYS = SpecialFilters.ALWAYS_TRUE;

    void addInfo(JadeHelper helper, BlockEntity blockEntity, Player player);

    default IFilter getFilter() {
        return READER;
    }

    default boolean canHandle(Player player) {
        return StackUtil.hasHotbarItems(player, getFilter()) || player.isCreative();
    }

    default MutableComponent status(boolean status) {
        return status ? TextFormatter.GREEN.literal(true + "") : TextFormatter.RED.literal(false + "");
    }

    interface IStatProvider {
        void addStats();
    }
}

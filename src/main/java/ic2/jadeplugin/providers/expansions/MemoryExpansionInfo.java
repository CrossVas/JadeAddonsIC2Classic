package ic2.jadeplugin.providers.expansions;

import ic2.core.block.machines.tiles.nv.MemoryExpansionTileEntity;
import ic2.jadeplugin.base.JadeHelper;
import ic2.jadeplugin.base.interfaces.IInfoProvider;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.List;

public class MemoryExpansionInfo implements IInfoProvider {

    public static final MemoryExpansionInfo THIS = new MemoryExpansionInfo();

    @Override
    public void addInfo(JadeHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof MemoryExpansionTileEntity memoryExpansion) {
            List<ItemStack> firstStick = new ObjectArrayList<>();
            List<ItemStack> secondStick = new ObjectArrayList<>();
            List<ItemStack> common = new ObjectArrayList<>();
            int j = memoryExpansion.crafting.getSlotCount();
            for (int i = 0; i < j; i++) {
                ItemStack recipeOutput = memoryExpansion.crafting.getStackInSlot(i);
                if (!recipeOutput.isEmpty()) {
                    if (i < 9) {
                        firstStick.add(recipeOutput);
                    } else {
                        secondStick.add(recipeOutput);
                    }
                    common.add(recipeOutput);
                }
            }
            boolean firstSlot = !memoryExpansion.inventory.getStackInSlot(0).isEmpty();
            boolean secondSlot = !memoryExpansion.inventory.getStackInSlot(1).isEmpty();
            if (firstSlot) {
                helper.bar(firstStick.size(), 9, translate("info.memory.recipes", firstStick.size(), 9), -16733185);
            }
            if (secondSlot) {
                helper.bar(secondStick.size(), 9, translate("info.memory.recipes", secondStick.size(), 9), -16733185);
            }
            helper.addGrid(common, translate("ic2.probe.memory_expansion.can_craft.name").withStyle(ChatFormatting.YELLOW));
        }
    }
}

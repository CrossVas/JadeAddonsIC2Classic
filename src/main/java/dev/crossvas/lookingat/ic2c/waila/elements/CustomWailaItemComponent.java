package dev.crossvas.lookingat.ic2c.waila.elements;

import com.mojang.blaze3d.vertex.PoseStack;
import mcp.mobius.waila.api.__internal__.IApiService;
import mcp.mobius.waila.api.component.ItemComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec2;

public class CustomWailaItemComponent extends ItemComponent {

    Vec2 SIZE;
    Vec2 TRANSLATION;

    public CustomWailaItemComponent(ItemStack stack) {
        super(stack);
    }

    public CustomWailaItemComponent size(Vec2 size) {
        this.SIZE = size;
        return this;
    }

    public CustomWailaItemComponent translate(Vec2 translation) {
        this.TRANSLATION = translation;
        return this;
    }

    @Override
    public int getWidth() {
        return (int) this.SIZE.x;
    }

    @Override
    public int getHeight() {
        return (int) this.SIZE.y;
    }

    @Override
    public void render(PoseStack matrices, int x, int y, float delta) {
        if (!this.stack.isEmpty()) {
            IApiService.INSTANCE.renderItem((int) (x + this.TRANSLATION.x), (int) (y + this.TRANSLATION.y) + 2, this.stack);
        }

    }
}

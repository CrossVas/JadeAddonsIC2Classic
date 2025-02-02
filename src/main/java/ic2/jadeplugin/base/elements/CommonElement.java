package ic2.jadeplugin.base.elements;

import ic2.jadeplugin.base.interfaces.IJadeElementBuilder;
import net.minecraft.world.phys.Vec2;

public abstract class CommonElement implements IJadeElementBuilder {

    Vec2 TRANSLATION;
    String SIDE;

    public CommonElement(Vec2 translation, String side) {
        this.TRANSLATION = translation;
        this.SIDE = side;
    }

    @Override
    public IJadeElementBuilder align(String side) {
        SIDE = side;
        return this;
    }

    @Override
    public IJadeElementBuilder translate(Vec2 translation) {
        TRANSLATION = translation;
        return this;
    }

    public Vec2 getTranslation() {
        return TRANSLATION;
    }

    public String getSide() {
        return SIDE;
    }
}

package dev.crossvas.jadexic2c.helpers;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public enum TextFormatter {
    BLACK(ChatFormatting.BLACK),
    DARK_BLUE(ChatFormatting.DARK_BLUE),
    DARK_GREEN(ChatFormatting.DARK_GREEN),
    DARK_AQUA(ChatFormatting.DARK_AQUA),
    DARK_RED(ChatFormatting.DARK_RED),
    DARK_PURPLE(ChatFormatting.DARK_PURPLE),
    GOLD(ChatFormatting.GOLD),
    GRAY(ChatFormatting.GRAY),
    DARK_GRAY(ChatFormatting.DARK_GRAY),
    BLUE(ChatFormatting.BLUE),
    GREEN(ChatFormatting.GREEN),
    AQUA(ChatFormatting.AQUA),
    RED(ChatFormatting.RED),
    LIGHT_PURPLE(ChatFormatting.LIGHT_PURPLE),
    YELLOW(ChatFormatting.YELLOW),
    WHITE(ChatFormatting.WHITE),
    OBFUSCATED(ChatFormatting.OBFUSCATED),
    BOLD(ChatFormatting.BOLD),
    STRIKETHROUGH(ChatFormatting.STRIKETHROUGH),
    UNDERLINE(ChatFormatting.UNDERLINE),
    ITALIC(ChatFormatting.ITALIC);

    final ChatFormatting FORMAT;

    TextFormatter(ChatFormatting formatting) {
        this.FORMAT = formatting;
    }

    public MutableComponent translate(String translatable) {
        return Component.translatable(translatable).withStyle(this.FORMAT);
    }

    public MutableComponent translate(String translatable, Object... args) {
        return Component.translatable(translatable, args).withStyle(this.FORMAT);
    }

    public MutableComponent literal(String literal) {
        return Component.literal(literal).withStyle(this.FORMAT);
    }

    public MutableComponent component(Component component) {
        return component.copy().withStyle(this.FORMAT);
    }

    public static TextFormatter formatPercentage(int dropChance) {
        if (dropChance >= 90) {
            return TextFormatter.GREEN;
        } else if (dropChance > 75) {
            return TextFormatter.YELLOW;
        } else if (dropChance > 50) {
            return TextFormatter.GOLD;
        } else if (dropChance > 35) {
            return TextFormatter.RED;
        } else {
            return TextFormatter.DARK_RED;
        }
    }

    public static TextFormatter getColor(int index) {
        return switch (index) {
            case 0 -> TextFormatter.AQUA;
            case 1 -> TextFormatter.RED;
            case 2 -> TextFormatter.YELLOW;
            case 3 -> TextFormatter.BLUE;
            case 4 -> TextFormatter.LIGHT_PURPLE;
            case 5 -> TextFormatter.GREEN;
            default -> TextFormatter.WHITE;
        };
    }

    public static TextFormatter tier(int tier) {
        return switch (tier) {
            case 0 -> DARK_GRAY; // ULV
            case 1 -> GRAY; // LV
            case 2 -> AQUA; // MV
            case 3 -> GOLD; // HV
            case 4 -> DARK_PURPLE; // EV
            case 5 -> BLUE; // IV
            case 6 -> LIGHT_PURPLE; // LuV
            case 7 -> RED; // ZPM
            case 8 -> DARK_AQUA; // UV
            default -> WHITE;
        };
    }
}

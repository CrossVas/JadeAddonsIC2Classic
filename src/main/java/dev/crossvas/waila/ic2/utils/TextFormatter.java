package dev.crossvas.waila.ic2.utils;

import net.minecraft.util.*;

public enum TextFormatter {
    BLACK(EnumChatFormatting.BLACK),
    DARK_BLUE(EnumChatFormatting.DARK_BLUE),
    DARK_GREEN(EnumChatFormatting.DARK_GREEN),
    DARK_AQUA(EnumChatFormatting.DARK_AQUA),
    DARK_RED(EnumChatFormatting.DARK_RED),
    DARK_PURPLE(EnumChatFormatting.DARK_PURPLE),
    GOLD(EnumChatFormatting.GOLD),
    GRAY(EnumChatFormatting.GRAY),
    DARK_GRAY(EnumChatFormatting.DARK_GRAY),
    BLUE(EnumChatFormatting.BLUE),
    GREEN(EnumChatFormatting.GREEN),
    AQUA(EnumChatFormatting.AQUA),
    RED(EnumChatFormatting.RED),
    LIGHT_PURPLE(EnumChatFormatting.LIGHT_PURPLE),
    YELLOW(EnumChatFormatting.YELLOW),
    WHITE(EnumChatFormatting.WHITE),
    OBFUSCATED(EnumChatFormatting.OBFUSCATED),
    BOLD(EnumChatFormatting.BOLD),
    STRIKETHROUGH(EnumChatFormatting.STRIKETHROUGH),
    UNDERLINE(EnumChatFormatting.UNDERLINE),
    ITALIC(EnumChatFormatting.ITALIC);

    final EnumChatFormatting FORMAT;

    TextFormatter(EnumChatFormatting formatting) {
        this.FORMAT = formatting;
    }

    public IChatComponent translate(String translatable) {
        return new ChatComponentTranslation(translatable).setChatStyle(new ChatStyle().setColor(this.FORMAT));
    }

    public IChatComponent translate(String translatable, Object... args) {
        return new ChatComponentTranslation(translatable, args).setChatStyle(new ChatStyle().setColor(this.FORMAT));
    }

    public IChatComponent literal(String literal) {
        return new ChatComponentText(literal).setChatStyle(new ChatStyle().setColor(this.FORMAT));
    }
}

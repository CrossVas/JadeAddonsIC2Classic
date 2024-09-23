package dev.crossvas.jadexic2c.utils;

public class SanityHelper {

    public static boolean check(CharSequence s) {
        return s == null || s.length() == 0 || s.charAt(0) <= ' ' || s.charAt(s.length() - 1) <= ' ';
    }

    public static String firstLetterUppercase(String string) {
        if (string != null && !string.isEmpty()) {
            String first = Character.toString(string.charAt(0));
            return string.replaceFirst(first, first.toUpperCase());
        } else {
            return string;
        }
    }

    public static String toPascalCase(String input) {
        StringBuilder builder = new StringBuilder();
        String[] var2 = input.replaceAll("_", " ").split(" ");
        int var3 = var2.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            String s = var2[var4];
            builder.append(firstLetterUppercase(s)).append(" ");
        }

        return builder.substring(0, builder.length() - 1);
    }
}

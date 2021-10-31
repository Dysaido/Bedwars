package hu.dysaido.bedwars.util;

import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Format {

    private Format() {
    }

    public static List<String> strings(String... messages) {
        return strings(Arrays.asList(messages));
    }

    public static List<String> strings(List<String> messages) {
        return messages.stream().map(Format::color).collect(Collectors.toList());
    }

    public static String color(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }
}

package hu.dysaido.bedwars.api.team;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;

import java.util.Locale;

public enum TeamColor {
    RED(ChatColor.RED, DyeColor.RED),
    BLUE(ChatColor.BLUE, DyeColor.BLUE),
    GREEN(ChatColor.GREEN, DyeColor.GREEN),
    YELLOW(ChatColor.YELLOW, DyeColor.YELLOW),
    AQUA(ChatColor.AQUA, DyeColor.CYAN),
    WHITE(ChatColor.WHITE, DyeColor.WHITE),
    PINK(ChatColor.LIGHT_PURPLE, DyeColor.PINK),
    GRAY(ChatColor.GRAY, DyeColor.GRAY);

    private final ChatColor chatColor;
    private final DyeColor dyeColor;

    TeamColor(ChatColor chatColor, DyeColor dyeColor) {
        this.chatColor = chatColor;
        this.dyeColor = dyeColor;
    }

    public String formattedName() {
        String str = this.toString();
        return String.valueOf(str.charAt(0)).toUpperCase(Locale.ROOT) + str.substring(1).toLowerCase(Locale.ROOT);
    }

    public ChatColor getChatColor() {
        return chatColor;
    }

    public DyeColor getDyeColor() {
        return dyeColor;
    }
}

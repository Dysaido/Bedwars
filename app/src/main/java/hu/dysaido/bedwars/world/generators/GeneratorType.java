package hu.dysaido.bedwars.world.generators;

import org.bukkit.Material;

import java.util.Locale;

public enum GeneratorType {
    IRON_INGOT("IRON_BLOCK"),
    GOLD_INGOT("GOLD_BLOCK"),
    DIAMOND("DIAMOND_BLOCK"),
    EMERALD("EMERALD_BLOCK");

    private final String blockType;

    GeneratorType(String blockType) {
        this.blockType = blockType;

    }

    public Material getMaterial() {
        return Material.valueOf(this.toString());
    }

    public Material getBlockMaterial() {
        return Material.valueOf(blockType);
    }

    public String formattedName() {
        String str = this.toString();
        if (str.contains("_"))  str = str.split("_")[0];
        return String.valueOf(str.charAt(0)).toUpperCase(Locale.ROOT) + str.substring(1).toLowerCase(Locale.ROOT);
    }

}

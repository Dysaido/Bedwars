package hu.dysaido.bedwars.world.generators;

import hu.dysaido.bedwars.gui.GUI;
import hu.dysaido.bedwars.util.Format;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class Generator extends BukkitRunnable {
    private final Location location;

    private final ItemStack itemStack;

    private final double xTime;
    private double time;

    public Generator(Location location, GeneratorType generatorType, double time, ConfigurationSection config) {
        this.location = location.add(0.5D, 2.0D, 0.5D);
        this.itemStack = GUI.speedCreateItem(generatorType.getMaterial(), 1, Format.color(config.getString("itemname")));
        this.time = time;
        this.xTime = time;
    }

    public void run() {
        if (this.time <= 0.0D) {
            this.location.getWorld().dropItem(this.location, this.itemStack);
            this.time = this.xTime;
        }
        this.time -= 0.05D;
    }

    @Override
    public String toString() {
        return "Generator{" +
                "Location=" + location +
                ", ItemStack=" + itemStack +
                ", xTime=" + xTime +
                ", Time=" + time +
                '}';
    }
}
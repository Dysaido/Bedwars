package hu.dysaido.bedwars.game;

import hu.dysaido.bedwars.BedWars;
import org.bukkit.configuration.ConfigurationSection;

public class Timer {
    private final int hour, minute, second;

    public Timer(BedWars bedWars) {
        ConfigurationSection section = bedWars.getConfig().getConfigurationSection("timer");
        this.hour = section.getInt("hour");
        this.minute = section.getInt("minute");
        this.second = section.getInt("second");
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public int getSecond() {
        return second;
    }

}

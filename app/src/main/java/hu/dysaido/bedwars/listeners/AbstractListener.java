package hu.dysaido.bedwars.listeners;

import hu.dysaido.bedwars.BedWars;
import hu.dysaido.bedwars.game.Game;
import hu.dysaido.bedwars.team.TeamManager;
import hu.dysaido.bedwars.world.WorldManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.Listener;

public class AbstractListener implements Listener {
    protected static final String TAG = "AbstractEvent";
    protected final BedWars bedWars;

    public AbstractListener(BedWars bedWars) {
        this.bedWars = bedWars;
        Bukkit.getServer().getPluginManager().registerEvents(this, bedWars);
    }
    protected Game getGame() {
        return bedWars.getGame();
    }

    protected static String key(Location location) {
        return WorldManager.key(location);
    }

}

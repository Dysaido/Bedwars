package hu.dysaido.bedwars.listeners;

import hu.dysaido.bedwars.BedWars;
import hu.dysaido.bedwars.api.events.*;
import org.bukkit.event.EventHandler;

public class GameCustomListeners extends AbstractListener {

    public GameCustomListeners(BedWars bedWars) {
        super(bedWars);
    }

    @EventHandler
    public void onGameActive(GameActiveEvent event) {

    }

    @EventHandler
    public void onGameCreator(GameCreatorEvent event) {

    }

    @EventHandler
    public void onGameEnd(GameEndEvent event) {

    }

    @EventHandler
    public void onGameReset(GameResetEvent event) {

    }

    @EventHandler
    public void onGameWait(GameWaitEvent event) {

    }

}

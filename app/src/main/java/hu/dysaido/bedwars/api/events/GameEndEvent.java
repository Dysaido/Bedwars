package hu.dysaido.bedwars.api.events;

import hu.dysaido.bedwars.game.Game;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GameEndEvent extends Event implements Cancellable {

    private static final HandlerList mHandlerList = new HandlerList();
    private boolean mCancelled;
    private final Game game;

    public GameEndEvent(Game game) {
        this.game = game;
        this.mCancelled = false;
    }

    @Override
    public boolean isCancelled() {
        return mCancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.mCancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return mHandlerList;
    }

    public static HandlerList getHandlerList() {
        return mHandlerList;
    }

    public Game getGame() {
        return game;
    }
}

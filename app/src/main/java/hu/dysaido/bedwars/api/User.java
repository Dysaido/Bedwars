package hu.dysaido.bedwars.api;

import hu.dysaido.bedwars.UserState;
import hu.dysaido.bedwars.api.command.AbstractCommand;
import hu.dysaido.bedwars.api.game.GameState;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface User {

    Player getPlayer();

    String getName();

    void sendMessage(String message);

    void sendTitle(String title, String subTitle);

    void kickUser();

    void kickUser(String message);

    void formatName(String name);

    String formattedName();

    void setRespawnLocation(Location location);

    Location getRespawnLocation();

    void setState(UserState userState);

    void setGameState(GameState gameState);

    GameState getGameState();

    void setFrozen(boolean state);

    boolean isAdmin();

    boolean isDev();

    boolean isDefault();

    boolean isFrozen();

    boolean isAuthorized(AbstractCommand command);

}

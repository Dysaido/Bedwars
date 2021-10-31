package hu.dysaido.bedwars;

import hu.dysaido.bedwars.api.User;
import hu.dysaido.bedwars.api.command.AbstractCommand;
import hu.dysaido.bedwars.api.game.GameState;
import hu.dysaido.bedwars.util.Format;
import hu.dysaido.bedwars.util.PlayOutTitleUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class UserImpl implements User {

    private final BedWars bedWars;
    private final Player player;
    private UserState userState;
    private GameState gameState;
    private Location respawnLocation;
    private String formatName;
    private boolean frozenStat;

    public UserImpl(BedWars bedWars, Player player) {
        this.bedWars = bedWars;
        this.player = player;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public String getName() {
        return player.getName();
    }

    @Override
    public void sendMessage(String message) {
        player.sendMessage(Format.color(message));
    }

    @Override
    public void sendTitle(String title, String subtitle) {
        if (title != null) PlayOutTitleUtil.setTitle(player, Format.color(title));
        if (subtitle != null) PlayOutTitleUtil.setSubTitle(player, Format.color(subtitle));
    }

    @Override
    public void kickUser() {
        player.kickPlayer("Kicked out!");
    }

    @Override
    public void kickUser(String message) {
        player.kickPlayer(Format.color(message));
    }

    @Override
    public void formatName(String name) {
        this.formatName = Format.color(name);
    }

    @Override
    public String formattedName() {
        return formatName;
    }

    @Override
    public void setRespawnLocation(Location location) {
        this.respawnLocation = location;
    }

    @Override
    public Location getRespawnLocation() {
        return respawnLocation;
    }

    @Override
    public void setState(UserState userState) {
        this.userState = userState;
    }

    @Override
    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    @Override
    public GameState getGameState() {
        return gameState;
    }

    @Override
    public void setFrozen(boolean state) {
        frozenStat = state;
    }

    @Override
    public boolean isAdmin() {
        return userState.equals(UserState.ADMINISTRATOR);
    }

    @Override
    public boolean isDev() {
        return userState.equals(UserState.DEVELOPER);
    }

    @Override
    public boolean isDefault() {
        return userState.equals(UserState.DEFAULT);
    }

    @Override
    public boolean isFrozen() {
        return frozenStat;
    }

    @Override
    public boolean isAuthorized(AbstractCommand command) {
        String path = "bedwars.";
        return isAuthorized(command, path);
    }

    private boolean isAuthorized(AbstractCommand command, String permissionPrefix) {
        return player.hasPermission(permissionPrefix + command.getName());
    }

    @Override
    public String toString() {
        return "User{" + "player=" + player + ", formatName='" + formatName + '\'' + '}';
    }
}

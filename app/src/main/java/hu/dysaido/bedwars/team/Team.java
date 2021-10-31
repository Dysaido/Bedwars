package hu.dysaido.bedwars.team;

import hu.dysaido.bedwars.BedWars;
import hu.dysaido.bedwars.api.User;
import hu.dysaido.bedwars.api.team.TeamColor;
import org.bukkit.GameMode;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Team {
    private final TeamColor teamColor;
    private final BedWars bedWars;
    private final Set<User> users;
    private final ConfigurationSection starting, beds;

    public Team(BedWars bedWars, TeamColor teamColor) {
        this.bedWars = bedWars;
        this.teamColor = teamColor;
        users = new HashSet<>();
        starting = bedWars.getConfig().getConfigurationSection("starting");
        beds = bedWars.getConfigUtil().getLocations().getFile().getConfigurationSection("teams").getConfigurationSection("beds");
    }

    public TeamColor getColor() {
        return teamColor;
    }

    public String getName() {
        return teamColor.getChatColor() + teamColor.formattedName();
    }

    public void setMember(User user) {
        users.add(user);
        String str = starting.getConfigurationSection("teams").getString("commontab").replaceAll("&", "§");
        str = str.replace("%teamcolor%", teamColor.getChatColor() + "")
                .replace("%team%", getName())
                .replace("%player%", user.getName());
        user.getPlayer().setPlayerListName(str);
    }

    public void removeMember(User user) {
        users.remove(user);
        user.getPlayer().setPlayerListName(user.getName());
    }

    public Set<User> getMembers() {
        return Collections.unmodifiableSet(users);
    }

    public boolean isMember(User user) {
        return users.contains(user);
    }

    public boolean isBedPlaced() {
        return beds.getConfigurationSection(getName()) != null;//todo: fix it
    }

    public boolean isDead() {
        return !isBedPlaced() && alivePlayerCount() == 0;
    }

    public int getMaxMembers() {
        return starting.getConfigurationSection("teams").getInt("maxnumber");
    }

    public int alivePlayerCount() {
        return users.stream().filter(user -> user.getPlayer().getGameMode() != GameMode.SPECTATOR).toArray().length;
    }

    public String getTeamChat(User user, String message) {
        String str = starting.getConfigurationSection("teams").getString("commonchat").replaceAll("&", "§");
        return str.replace("%teamcolor%", teamColor.getChatColor() + "")
                .replace("%team%", getName())
                .replace("%player%", user.getName())
                .replace("%message%", message);
    }
}

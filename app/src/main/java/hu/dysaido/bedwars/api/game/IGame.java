package hu.dysaido.bedwars.api.game;

import hu.dysaido.bedwars.api.User;
import hu.dysaido.bedwars.team.Team;
import org.bukkit.Location;

import java.util.Set;

public interface IGame {

    void setState(GameState gameState);

    GameState getState();

    void setUserState(User user);

    void addMember(User user);

    void removeMember(User user);

    Set<User> getMembers();

    void joinTeam(User user, Team team);

    void quitTeam(User user);

    Team findTeamByUser(User user);

    Location getLobby();

    int maxMember();

    int minMember();

    boolean isOver();

    boolean isFull();

    boolean hasEnoughMember();

    boolean hasEnoughTeam();

}

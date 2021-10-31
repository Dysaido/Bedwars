package hu.dysaido.bedwars.team;

import hu.dysaido.bedwars.BedWars;
import hu.dysaido.bedwars.api.User;
import hu.dysaido.bedwars.api.team.TeamColor;
import hu.dysaido.bedwars.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TeamManager {
    private final static String TAG = "TeamService";
    private final BedWars bedWars;
    private final List<Team> teams;

    public TeamManager(BedWars bedWars) {
        Log.information(TAG, "initialisation");
        this.bedWars = bedWars;
        teams = new ArrayList<>();
        initTeams();
    }

    public Team findTeamByColor(TeamColor teamColor) {
        for (Team team : teams) if (team.getColor().equals(teamColor)) return team;
        return null;
    }

    public Team findTeamByUser(User user) {
        for (Team team : teams) if (team.isMember(user)) return team;
        return null;
    }

    public void removeTeam(User user) {
        Objects.requireNonNull(user);
        teams.remove(findTeamByUser(user));
    }

    public void removeTeam(TeamColor teamColor) {
        Objects.requireNonNull(teamColor);
        teams.remove(findTeamByColor(teamColor));
    }

    public List<Team> getTeams() {
        return Collections.unmodifiableList(teams);
    }

    private void initTeams() {
        for (TeamColor teamColor : TeamColor.values()) teams.add(new Team(bedWars, teamColor));
    }

    public List<Team> isAliveTeams() {
        return teams.stream().filter(team -> team.isBedPlaced() && team.alivePlayerCount() != 0).collect(Collectors.toList());
    }

}

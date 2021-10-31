package hu.dysaido.bedwars.game;

import hu.dysaido.bedwars.BedWars;
import hu.dysaido.bedwars.api.User;
import hu.dysaido.bedwars.api.game.GameState;
import hu.dysaido.bedwars.api.game.IGame;
import hu.dysaido.bedwars.gui.GUI;
import hu.dysaido.bedwars.scoreboard.GameScoreboard;
import hu.dysaido.bedwars.sound.SoundManager;
import hu.dysaido.bedwars.team.Team;
import hu.dysaido.bedwars.team.TeamManager;
import hu.dysaido.bedwars.util.ConfigUtil;
import hu.dysaido.bedwars.util.LocationUtil;
import hu.dysaido.bedwars.util.Log;
import hu.dysaido.bedwars.world.WorldManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class Game implements IGame {

    private final static String TAG = "Game";

    private final Set<User> users;
    private final Map<User, Team> userTeamMap;
    private final BedWars bedWars;
    private final ConfigUtil configUtil;
    private GameState gameState;
    private final GameScoreboard scoreboard;
    private final TeamManager teamManager;
    private final SoundManager soundManager;
    private final WorldManager worldManager;
    private BukkitTask startProgressbar, scoreboardTask;

    public Game(BedWars bedWars) {
        this.bedWars = bedWars;
        users = new HashSet<>();
        userTeamMap = new HashMap<>();
        gameState = GameState.WAIT;
        configUtil = bedWars.getConfigUtil();
        teamManager = new TeamManager(bedWars);
        soundManager = new SoundManager(bedWars);
        worldManager = new WorldManager(bedWars);
        scoreboard = new GameScoreboard(this, bedWars);
    }

    @Override
    public void setState(GameState gameState) {
        if (gameState == null) return;
        this.gameState = gameState;
        users.forEach(this::setUserState);
    }

    @Override
    public GameState getState() {
        return gameState;
    }

    @Override
    public void setUserState(User user) {
        if (user == null) return;
        Player player = user.getPlayer();
        switch (gameState) {
            case ACTIVE:
                if (startProgressbar != null && !startProgressbar.isCancelled()) startProgressbar.cancel();
                player.setGameMode(GameMode.SURVIVAL);
                player.getInventory().clear();
                player.setCanPickupItems(true);
                player.setFoodLevel(20);
                break;
            case CREATOR:
                player.setGameMode(GameMode.CREATIVE);
                player.getInventory().clear();
                GUI.ShowCreator.createPlayerInventory(this, player);
                player.setCanPickupItems(true);
                break;
            case WAIT:
                player.setGameMode(GameMode.SURVIVAL);
                player.getInventory().clear();
                GUI.ShowLobby.createPlayerInventory(this, player);
                player.teleport(getLobby().add(0.0D, 1.0D, 0.0D));
                player.setCanPickupItems(false);
                player.setExp(0);
                player.setLevel(0);
                player.setTotalExperience(0);
                player.setFoodLevel(20);
                break;
            case END:
                player.setCanPickupItems(false);
                player.setFoodLevel(20);
                break;
        }
    }

    @Override
    public void addMember(User user) {
        if (user == null) return;
        if (users.size() == 0) {
            users.add(user);
            scoreboard.addMember(user);
            scoreboard.onCreate();
            setUserState(user);
            scoreboard.onUpdate();
            scoreboardTask = Bukkit.getScheduler().runTaskTimer(bedWars, scoreboard::onUpdate, 30L, 20L);
        } else {
            users.add(user);
            scoreboard.addMember(user);
            setUserState(user);
        }
        if (hasEnoughMember() && gameState == GameState.WAIT) {
            Log.information(TAG, "Game starting progressbar is alive!");
            if (startProgressbar == null) {
                startProgressbar = new GameStartTask(this).runTaskTimer(bedWars, 20L, 20L);
            } else if (startProgressbar.isCancelled()) {
                startProgressbar = new GameStartTask(this).runTaskTimer(bedWars, 20L, 20L);
            }
        }
    }

    @Override
    public void removeMember(User user) {
        if (user == null) return;
        users.remove(user);
        scoreboard.removeMember(user);
        if (users.size() == 0) {
            scoreboardTask.cancel();
            scoreboard.onDestroy();
        }
    }

    @Override
    public Set<User> getMembers() {
        return Collections.unmodifiableSet(users);
    }

    @Override
    public void joinTeam(User user, Team team) {
        if (user == null || team == null) return;
        if (userTeamMap.containsKey(user)) {
            if (userTeamMap.get(user).equals(team)) quitTeam(user);
            else return;
        }
        team.setMember(user);
        userTeamMap.put(user, team);

    }

    @Override
    public void quitTeam(User user) {
        if (user == null) return;
        if (!userTeamMap.containsKey(user)) return;
        userTeamMap.get(user).removeMember(user);
        userTeamMap.remove(user);
    }

    @Override
    public Team findTeamByUser(User user) {
        if (user == null) return null;
        return userTeamMap.get(user);
    }

    @Override
    public Location getLobby() {
        return LocationUtil.getLocation(configUtil.getLocations().getFile().getConfigurationSection("lobby"));
    }

    @Override
    public int maxMember() {
        ConfigurationSection playerNumbers = bedWars.getConfig().getConfigurationSection("starting")
                .getConfigurationSection("player-numbers");
        return playerNumbers.getInt("maxnumber");
    }

    @Override
    public int minMember() {
        ConfigurationSection playerNumbers = bedWars.getConfig().getConfigurationSection("starting")
                .getConfigurationSection("player-numbers");
        return playerNumbers.getInt("minnumber");
    }

    @Override
    public boolean isOver() {
        return gameState == GameState.END;
    }

    @Override
    public boolean isFull() {
        return maxMember() == users.size();
    }

    @Override
    public boolean hasEnoughMember() {
        return users.size() == minMember();
    }

    @Override
    public boolean hasEnoughTeam() {
        Set<Team> teams = new HashSet<>();
        teamManager.getTeams().stream().filter(team -> team.getMembers().size() != 0).forEach(teams::add);
        return teams.size() >= 2;
    }

    public TeamManager getTeamManager() {
        return teamManager;
    }

    public SoundManager getSoundManager() {
        return soundManager;
    }

    public WorldManager getWorldManager() {
        return worldManager;
    }

    public BedWars getPlugin() {
        return bedWars;
    }

}

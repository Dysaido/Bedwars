package hu.dysaido.bedwars.scoreboard;

import hu.dysaido.bedwars.util.Format;
import hu.dysaido.bedwars.util.Log;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.*;

public class BasiScoreboard {

    private final static String TAG = "BasicScoreboard";
    private final DisplaySlot displaySlot;
    private final Map<org.bukkit.scoreboard.Scoreboard, List<String>> previousScoreboard;
    private final Set<Player> players;
    private String title;
    private List<String> lines;
    private org.bukkit.scoreboard.Scoreboard scoreboard;
    private ScoreboardManager scoreboardManager;
    private boolean controlSameScoreboard;

    public BasiScoreboard(DisplaySlot displaySlot) {
        this.displaySlot = displaySlot;
        players = new HashSet<>();
        previousScoreboard = new HashMap<>();
    }

    public void onCreate(boolean controlSameScoreboard) {
        Log.information(TAG, "onCreate");
        this.controlSameScoreboard = controlSameScoreboard;
        if (players.isEmpty()) {
            Log.error(TAG, "Players cannot be null");
            return;
        }
        this.scoreboardManager = Bukkit.getScoreboardManager();
        if (scoreboardManager == null) {
            Log.error(TAG, "ScoreboardManager cannot be null");
            return;
        }
        this.scoreboard = scoreboardManager.getNewScoreboard();
        players.forEach(player -> player.setScoreboard(scoreboard));
    }

    public void onReload() {
        Log.information(TAG, "onReload");
        if (scoreboardManager == null) {
            Log.error(TAG, "ScoreboardManager cannot be null");
            scoreboardManager = Bukkit.getScoreboardManager();
            Objects.requireNonNull(scoreboardManager, "ScoreboardManager cannot be null");
            scoreboard = scoreboardManager.getNewScoreboard();
        }
        if (scoreboard == null) {
            Log.error(TAG, "Scoreboard cannot be null");
            scoreboard = scoreboardManager.getNewScoreboard();
            Objects.requireNonNull(scoreboard, "Scoreboard cannot be null");
        }
        players.forEach(player -> player.setScoreboard(scoreboard));
    }

    public void onUpdate() {
        if (scoreboard == null || players.size() == 0) onReload();
        players.forEach(player -> onUpdate(scoreboard, lines));
    }

    private void onUpdate(org.bukkit.scoreboard.Scoreboard scoreboard, List<String> lines) {
        if (controlSameScoreboard) {
            if (previousScoreboard.containsKey(scoreboard)) {
                if (previousScoreboard.get(scoreboard).equals(lines)) {
                    if (scoreboard.getObjective("dummy").getDisplayName().equals(title)) return;
                }
                if (previousScoreboard.get(scoreboard).size() != lines.size()) {
                    scoreboard.clearSlot(displaySlot);
                    scoreboard.getEntries().forEach(scoreboard::resetScores);
                    scoreboard.getTeams().stream().filter(team -> team.getName().contains("line")).forEach(Team::unregister);
                }
            }
        } else {
            scoreboard.clearSlot(displaySlot);
            scoreboard.getEntries().forEach(scoreboard::resetScores);
            scoreboard.getTeams().stream().filter(team -> team.getName().contains("line")).forEach(Team::unregister);
        }
        previousScoreboard.put(scoreboard, lines);
        Objective objective;
        if (scoreboard.getObjective("dummy") == null) {
            objective = scoreboard.registerNewObjective("dummy", "dummy");
        } else {
            objective = scoreboard.getObjective("dummy");
        }
        objective.setDisplaySlot(displaySlot);
        objective.setDisplayName(Format.color(title));
        int score = 0;
        Team team;
        // One thing to note is that up until 1.13, the prefix length has to have a maximum of 16 characters (in 1.13+ it is 64)
        for (String entry : lines) {
            if (entry.length() > 64) throw new IllegalArgumentException();
            team = scoreboard.getTeam("line" + score);
            if (team != null) {
                team.getEntries().forEach(team::removeEntry);
                team.addEntry(Format.color(entry));
            } else {
                team = scoreboard.registerNewTeam("line" + score);
                team.addEntry(Format.color(entry));
                objective.getScore(lines.get(score)).setScore(score);
            }
            score += 1;
        }

    }

    public void onClear() {
        Log.information(TAG, "onClear");
        players.forEach(player -> player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard()));
        scoreboard.getEntries().forEach(scoreboard::resetScores);
        scoreboard.clearSlot(displaySlot);
        previousScoreboard.clear();
    }

    public void setLines(List<String> lines) {
        this.lines = lines;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<Player> getPlayers() {
        return Collections.unmodifiableSet(players);
    }

    public void addPlayer(Player player) {
        players.add(player);
        onReload();
    }

    public void removePlayer(Player player) {
        players.remove(player);
        onReload();
    }

}

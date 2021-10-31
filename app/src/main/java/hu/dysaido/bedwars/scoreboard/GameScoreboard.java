package hu.dysaido.bedwars.scoreboard;

import hu.dysaido.bedwars.BedWars;
import hu.dysaido.bedwars.api.User;
import hu.dysaido.bedwars.api.team.TeamColor;
import hu.dysaido.bedwars.game.Game;
import hu.dysaido.bedwars.team.Team;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.scoreboard.DisplaySlot;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import java.util.stream.Collectors;

public class GameScoreboard {

    private final static String TAG = GameScoreboard.class.getName();
    private final ConfigurationSection scoreboards;
    private final Game game;
    private final BedWars bedWars;
    private String title;
    private boolean showYourTeam;
    private List<String> lines;
    private BasiScoreboard basiScoreboard;

    public GameScoreboard(Game game, BedWars bedWars) {
        this.game = game;
        this.bedWars = bedWars;
        scoreboards = bedWars.getConfigUtil().getScoreboards().getFile().getConfigurationSection("scoreboards");
        basiScoreboard = new BasiScoreboard(DisplaySlot.SIDEBAR);

    }

    public void onCreate() {
        updateValues();
        basiScoreboard.onCreate(false);
        basiScoreboard.setLines(lines);
    }

    public void onUpdate() {
        updateValues();
        basiScoreboard.setTitle(title);
        basiScoreboard.setLines(lines);
        basiScoreboard.onUpdate();
    }

    public void onDestroy() {
        basiScoreboard.onClear();
        basiScoreboard = null;
        basiScoreboard = new BasiScoreboard(DisplaySlot.SIDEBAR);
    }

    public void addMember(User user) {
        basiScoreboard.addPlayer(user.getPlayer());
    }

    public void removeMember(User user) {
        basiScoreboard.removePlayer(user.getPlayer());
    }

    private void updateValues() {
        title = findSections(this::getTitle);
        lines = findSections(this::getLines);
        showYourTeam = isShowTeam();
    }

    private String formatLines(String raw) {
        for (TeamColor teamColor : TeamColor.values()) {
            Team team;
            String state;
            String symbol = "{" + teamColor.formattedName().toLowerCase(Locale.ROOT) + "team}";
            if (raw.contains(symbol)) {
                team = game.getTeamManager().findTeamByColor(teamColor);
                if (team.isBedPlaced()) state = team.alivePlayerCount() == 0 ? "〤" : "✔";
                else state = team.alivePlayerCount() == 0 ? "〤" : String.valueOf(team.alivePlayerCount());
                raw = raw.replace(symbol, team.getName()).replace("{state}", state);
            }
        }
        return raw.replaceAll("&", "§").replace("{gametime}", "String.valueOf(game.getGameTime().formattedTime())")
                .replace("{date}", LocalDate.now().toString())
                .replace("{players}", String.valueOf(basiScoreboard.getPlayers().size()));
    }

    private String getTitle(ConfigurationSection section) {
        String prefix = scoreboards.getString("prefix");
        LocalTime time = LocalTime.now();
        return section.getString("title").replace("{prefix}", prefix)
                .replace("{servertime}", String.valueOf(time).split("\\.")[0])
                .replace("{gametime}", "game.getGameTime()");
    }

    private List<String> getLines(ConfigurationSection section) {
        return section.getStringList("lines").stream().map(this::formatLines).collect(Collectors.toList());
    }

    private boolean isShowTeam() {
        return scoreboards.getBoolean("show-your-team");
    }

    private <R> R findSections(Function<ConfigurationSection, R> sectionConsumer) {
        for (String key : scoreboards.getKeys(false)) {
            ConfigurationSection section = scoreboards.getConfigurationSection(key);
            if (section == null) continue;
            if (section.getName().equals(game.getState().name().toLowerCase(Locale.ROOT)))
                return sectionConsumer.apply(section);
        }
        return null;
    }

}
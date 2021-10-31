package hu.dysaido.bedwars.util;

import hu.dysaido.bedwars.BedWars;
import org.bukkit.configuration.ConfigurationSection;

public class ConfigUtil {

    private static final String TAG = "ConfigUtil";
    private FileManager mGeneratorManager;
    private FileManager mScoreboardsManager;
    private FileManager mLocationsManager;
    private final BedWars bedWars;
    private String prefix;

    public ConfigUtil(BedWars bedWars) {
        this.bedWars = bedWars;
        initConfig();
        initConfigGenerators();
        initConfigLocations();
        initConfigScoreboard();
    }

    private void initConfig() {
        Log.information(TAG, "initConfig");
        bedWars.getConfig().options().copyDefaults(true);
        if (!bedWars.getConfig().isString("chat-prefix"))
            bedWars.getConfig().set("chat-prefix", "&7[&3Bed&8Wars&7]");

        if (!bedWars.getConfig().isConfigurationSection("generator")) {
            bedWars.getConfig().createSection("generator");
            if (!bedWars.getConfig().getConfigurationSection("generator").isString("itemname")) {
                bedWars.getConfig().set("itemname", "&7|&3Bed&8Wars&7|");
            }
        }

        if (!bedWars.getConfig().isConfigurationSection("starting")) {
            bedWars.getConfig().createSection("starting");
            ConfigurationSection starting = bedWars.getConfig().getConfigurationSection("starting");
            if (!starting.isInt("countdown"))
                starting.set("countdown", 30);
            if (!starting.isConfigurationSection("teams"))
                starting.createSection("teams");
            ConfigurationSection teams = starting.createSection("teams");
            if (!teams.isString("commonchat"))
                teams.set("commonchat", "%teamcolor%[%team%] &7%player% &6> &7%message%");
            if (!teams.isString("commontab"))
                teams.set("commontab", "%teamcolor%[%team%] &7%player%");
            if (!teams.isInt("maxnumber"))
                teams.set("maxnumber", 5);
            if (!starting.isConfigurationSection("player-numbers"))
                starting.createSection("player-numbers");
            ConfigurationSection player_numbers = starting.createSection("player-numbers");
            if (!player_numbers.isInt("maxnumber"))
                player_numbers.set("maxnumber", 10);
            if (!player_numbers.isInt("minnumber"))
                player_numbers.set("minnumber", 5);

        }
        if (!bedWars.getConfig().isConfigurationSection("won")) {
            ConfigurationSection won = bedWars.getConfig().getConfigurationSection("won");
            if (!won.isString("congratulation"))
                won.set("congratulation", "Congratulation %player%!");
            if (!won.isInt("leavetime"))
                won.set("leavetime", 10);
        }
        bedWars.saveConfig();
        prefix = Format.color(bedWars.getConfig().getString("chat-prefix"));
    }

    private void initConfigGenerators() {
        Log.information(TAG, "initGenerators");
        mGeneratorManager = new FileManager(bedWars, "generators");
        mGeneratorManager.getFile().options().copyDefaults(true);
        if (!mGeneratorManager.getFile().isConfigurationSection("generators"))
            mGeneratorManager.getFile().createSection("generators");
        if (!mGeneratorManager.getFile().isConfigurationSection("locations"))
            mGeneratorManager.getFile().createSection("locations");
        mGeneratorManager.saveFile();
    }

    private void initConfigLocations() {
        Log.information(TAG, "initLocations");
        mLocationsManager = new FileManager(bedWars, "locations");
        mLocationsManager.getFile().options().copyDefaults(true);
        if (!mLocationsManager.getFile().isConfigurationSection("lobby"))
            mLocationsManager.getFile().createSection("lobby");
        if (!mLocationsManager.getFile().isConfigurationSection("teams"))
            mLocationsManager.getFile().createSection("teams");
        if (!mLocationsManager.getFile().getConfigurationSection("teams").isConfigurationSection("beds"))
            mLocationsManager.getFile().getConfigurationSection("teams").createSection("beds");
        if (!mLocationsManager.getFile().getConfigurationSection("teams").isConfigurationSection("spawns"))
            mLocationsManager.getFile().getConfigurationSection("teams").createSection("spawns");
        mLocationsManager.saveFile();
    }

    private void initConfigScoreboard() {
        Log.information(TAG, "initScoreboard");
        mScoreboardsManager = new FileManager(bedWars, "scoreboards");
        mScoreboardsManager.getFile().options().copyDefaults(true);
        mScoreboardsManager.saveFile();
    }

    public FileManager getGenerator() {
        return mGeneratorManager;
    }

    public FileManager getLocations() {
        return mLocationsManager;
    }

    public FileManager getScoreboards() {
        return mScoreboardsManager;
    }

    public String getPrefix() {
        return prefix;
    }

}

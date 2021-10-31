package hu.dysaido.bedwars.world;

import hu.dysaido.bedwars.BedWars;
import hu.dysaido.bedwars.util.FileManager;
import hu.dysaido.bedwars.util.Log;
import hu.dysaido.bedwars.world.generators.Generator;
import hu.dysaido.bedwars.world.generators.GeneratorType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class WorldManager {
    private final static String TAG = "WorldService";
    public final HashMap<Location, Generator> generatorHashMap;
    private final BedWars bedWars;
    private final ConfigurationSection generatorName;
    private final List<ConfigurationSection> sections;
    private final FileManager generatorManager;
    private boolean WORKER = false;
    private Block removeBlock;

    public WorldManager(BedWars bedWars) {
        Log.information(TAG, "initialisation");
        this.bedWars = bedWars;
        generatorHashMap = new HashMap<>();
        generatorManager = bedWars.getConfigUtil().getGenerator();
        generatorName = this.bedWars.getConfig().getConfigurationSection("generator");
        sections = Arrays.asList(generatorManager.getFile().getConfigurationSection("generators"),
                generatorManager.getFile().getConfigurationSection("locations"));
    }


    public static String key(Location location) {
        return location.getWorld().getName() + "," + location.getBlockX() + "," + location.getBlockY() + "," + location.getBlockZ();
    }

    public void initGenerators() {
        Log.information(TAG, "InitGenerators");
        if (!generatorHashMap.isEmpty()) {
            generatorHashMap.clear();
            generatorManager.reloadFile();
        }
        findGeneratorByLocation(this::init);
        generatorHashMap.keySet().forEach(value -> Log.information(TAG, value.toString()));
    }

    public void saveGenerator(Block block, GeneratorType generatorType, double time) {
        Log.information(TAG, "SaveGenerator");
        String key = key(block.getLocation());
        UUID uuid = UUID.randomUUID();
        ConfigurationSection generatorsConfig = bedWars.getConfigUtil().getGenerator().getFile().getConfigurationSection("generators");
        if (generatorsConfig.contains(uuid.toString()) || sections.get(1).contains(key)) {
            Log.information(TAG, "The ID is used by someone else.");
            return;
        }
        generatorsConfig = generatorsConfig.createSection(uuid.toString());
        generatorsConfig.set("generatortype", generatorType.formattedName());
        generatorsConfig.set("item", generatorType.getMaterial().toString());
        generatorsConfig.set("block", generatorType.getBlockMaterial().toString());
        generatorsConfig.set("time", time);
        sections.get(1).set(key, uuid.toString());
        generatorManager.saveFile();
        killGenerators();
    }

    public void runGenerators() {
        Log.information(TAG, "RunGenerators");
        if (WORKER) return;
        WORKER = true;
        initGenerators();
        generatorHashMap.values().forEach(generator -> generator.runTaskTimer(bedWars, 0L, 1L));
    }

    public void killGenerators() {
        Log.information(TAG, "KillGenerators");
        if (!WORKER) return;
        WORKER = false;
        generatorHashMap.values().forEach(BukkitRunnable::cancel);
    }

    public void deleteGenerator(Block block) {
        Log.information(TAG, "DeleteGenerator");
        removeBlock = block;
        findGeneratorByLocation(this::delete);
        killGenerators();
    }

    private void findGeneratorByLocation(WorldSelection worldSelection) {
        for (String raw_location : sections.get(1).getKeys(false)) {
            World world = Bukkit.getServer().getWorld(raw_location.split(",")[0]);
            float x = Integer.parseInt(raw_location.split(",")[1]);
            float y = Integer.parseInt(raw_location.split(",")[2]);
            float z = Integer.parseInt(raw_location.split(",")[3]);
            worldSelection.apply(raw_location, world, x, y, z);
        }
    }

    private void init(String raw_location, World world, float x, float y, float z) {
        for (String raw_generator : sections.get(0).getKeys(false)) {
            if (raw_generator.equals(sections.get(1).get(raw_location))) {
                ConfigurationSection genValues = sections.get(0).getConfigurationSection(raw_generator);
                GeneratorType generatorType = GeneratorType.valueOf(genValues.getString("item"));
                double time = genValues.getDouble("time");
                Location location = new Location(world, x, y, z);
                if (location.getBlock().getType().equals(Material.AIR))
                    location.getBlock().setType(generatorType.getBlockMaterial());
                generatorHashMap.put(location, new Generator(location, generatorType, time, generatorName));
            }
        }
    }

    private void delete(String raw_location, @NotNull World world, float x, float y, float z) {
        Location target = removeBlock.getLocation();
        if (world.equals(target.getWorld()) && x == target.getBlockX() && y == target.getBlockY() && z == target.getBlockZ()) {
            String key = key(target);
            for (String raw_generator : sections.get(0).getKeys(false)) {
                if (raw_generator.equals(sections.get(1).get(key))) {
                    sections.get(1).set(key, null);
                    sections.get(0).set(raw_generator, null);
                    generatorManager.saveFile();
                    removeBlock.setType(Material.APPLE);
                    break;
                }
            }
        }
    }

    @FunctionalInterface
    private interface WorldSelection {
        void apply(String raw_location, World world, float x, float y, float z);
    }
}

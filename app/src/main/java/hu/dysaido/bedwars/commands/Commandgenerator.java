package hu.dysaido.bedwars.commands;

import hu.dysaido.bedwars.BedWars;
import hu.dysaido.bedwars.api.command.AbstractCommand;
import hu.dysaido.bedwars.world.WorldManager;
import hu.dysaido.bedwars.world.generators.GeneratorType;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Commandgenerator extends AbstractCommand {

    public Commandgenerator(BedWars bedWars) {
        super(bedWars,"generator");
    }

    @Override
    public void applyCommand(CommandSender sender, List<String> args) {
        //        args[0]   args[1] args[2]
        //bedwars generator set     5
        List<String> strings = Arrays.asList("set/remove", "repeat-time");
        WorldManager worldManager = bedWars.getGame().getWorldManager();
        if (args.size() == 1) setCommandHelper(sender, getName(), strings);
        if (args.get(1).equalsIgnoreCase("start")) {
            sender.sendMessage("Starting generators...");
            worldManager.runGenerators();
            sender.sendMessage("Successfully");
        } else if (args.get(1).equalsIgnoreCase("stop")) {
            sender.sendMessage("Stopping generators...");
            worldManager.killGenerators();
            sender.sendMessage("Successfully");
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage("You are not a player");
            return;
        }
        Player player = (Player) sender;
        if (args.get(1).equalsIgnoreCase("set")) {
            if (args.size() > 2) {
                Block block = player.getTargetBlock(null, 3);
                for (GeneratorType generatorType : GeneratorType.values()) {
                    if (block.getType().equals(generatorType.getBlockMaterial())) {
                        double time = Double.parseDouble(args.get(2));
                        worldManager.saveGenerator(block, generatorType, time);
                        return;
                    }
                }
                sender.sendMessage("That block is not supported.");
            } else {
                setCommandHelper(sender, getName(), strings);
            }
        } else if (args.get(1).equalsIgnoreCase("remove")) {
            if (args.size() == 2) {
                Block block = player.getTargetBlock(null, 3);
                worldManager.deleteGenerator(block);
                sender.sendMessage("successful remove");
            } else {
                setCommandHelper(sender, getName(), strings);
            }
        }
    }

    @Override
    public List<String> applyTab(CommandSender sender, List<String> args) {
        ArrayList<String> commands = new ArrayList<>();
        if (args.size() == 2) {
            commands.add("set");
            commands.add("remove");
            commands.add("start");
            commands.add("stop");
        } else if (args.size() > 2) {
            for (GeneratorType type : GeneratorType.values()) commands.add(type.toString());
        }
        return commands;
    }

}

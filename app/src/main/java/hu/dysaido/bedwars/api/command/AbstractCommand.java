package hu.dysaido.bedwars.api.command;

import hu.dysaido.bedwars.BedWars;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public abstract class AbstractCommand {

    protected final String TAG = "ExtensionCommand";
    protected final BedWars bedWars;
    private List<String> tabExecutor;

    private final String name;

    public AbstractCommand(BedWars bedWars, String name) {
        this.bedWars = bedWars;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void init(CommandSender sender, List<String> args, ExtensionType extensionType) {
        Objects.requireNonNull(bedWars, "PlotMe cannot be null!");
        Objects.requireNonNull(sender, "CommandSender arguments cannot be bull!");
        Objects.requireNonNull(args, "Command arguments cannot be null!");
        Objects.requireNonNull(extensionType, "Execute type cannot be bull!");
        switch (extensionType) {
            case COMMAND:
                applyCommand(sender, args);
                break;
            case TAB:
                tabExecutor = applyTab(sender, args);
                break;
        }
    }

    public abstract void applyCommand(CommandSender sender, List<String> args);

    public abstract List<String> applyTab(CommandSender sender, List<String> args);

    public List<String> getTabExecutor() {
        return tabExecutor;
    }

    protected void setCommandHelper(CommandSender sender, String command, List<String> args) {
        if (args == null) {
            sender.sendMessage("§a/Bedwars §7" + command);
            return;
        }
        sender.sendMessage("§a/Bedwars §7" + command + " " + Arrays.toString(args.toArray()));
    }

    public enum ExtensionType {
        COMMAND, TAB
    }
}

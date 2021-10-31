package hu.dysaido.bedwars.listeners;

import hu.dysaido.bedwars.BedWars;
import hu.dysaido.bedwars.gui.GUI;
import hu.dysaido.bedwars.world.WorldManager;
import hu.dysaido.bedwars.world.generators.GeneratorType;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

public class GameWorldListeners extends AbstractListener {

    public GameWorldListeners(BedWars bedWars) {
        super(bedWars);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        String key = WorldManager.key(event.getBlock().getLocation());
        ConfigurationSection locationsConfig = bedWars.getConfigUtil().getGenerator().getFile().getConfigurationSection("locations");
        if (locationsConfig.contains(key)) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.RED + "The block is a generator. You do not authorized that you break it");
            return;
        }
        blockHandler(player, event);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        blockHandler(player, event);
//        for (GeneratorType type : GeneratorType.values()) {
//            String name ="§e" + type.formattedName();
//            if (player.getInventory().getItemInMainHand().hasItemMeta()) {
//                ItemMeta itemMeta = player.getInventory().getItemInMainHand().getItemMeta();
//                if (itemMeta.hasDisplayName()) {
//                    if (itemMeta.getDisplayName().equals(name)) {
//
//                    }
//                }
//            }
//        }
    }

    @EventHandler
    public void onBlockBurn(BlockBurnEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event) {
        event.setCancelled(true);
    }

    private void blockHandler(Player player, Cancellable cancellable) {
        PlayerInventory inventory = player.getInventory();
        ItemStack itemInMainHand = inventory.getItemInMainHand();
        ItemStack itemInOffHand = inventory.getItemInOffHand();
        switch (getGame().getState()) {
            case ACTIVE:
                break;
            case CREATOR:
                if (itemInMainHand != null) creatorItems(itemInMainHand.getItemMeta(), cancellable);
                else if (itemInOffHand != null) creatorItems(itemInOffHand.getItemMeta(), cancellable);
                break;
            case END:
            case RESET:
            case WAIT:
                cancellable.setCancelled(true);
                break;
        }
    }

    private void creatorItems(ItemMeta itemMeta, Cancellable cancellable) {
        if (itemMeta == null) return;
        if (itemMeta.getDisplayName() == null) return;
        switch (itemMeta.getDisplayName()) {
            case GUI.ShowCreator.editorInHand:
                cancellable.setCancelled(true);
                break;
        }
    }

}

package hu.dysaido.bedwars.listeners;

import hu.dysaido.bedwars.BedWars;
import hu.dysaido.bedwars.gui.GUI;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;

public class GameInventoryListeners extends AbstractListener {

    public GameInventoryListeners(BedWars bedWars) {
        super(bedWars);
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        switch (getGame().getState()) {
            case ACTIVE:
                break;
            case CREATOR:
                ItemStack itemStack = event.getItemDrop().getItemStack();
                creatorItems(itemStack.getItemMeta(), event);
                break;
            case END:
            case RESET:
            case WAIT:
                event.setCancelled(true);
                break;
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        GUI.WindowHandler windowHandler = GUI.WindowHandler.getWindows().get(event.getInventory().getTitle());
        if (windowHandler != null) {
            if (windowHandler.getItems().containsKey(event.getSlot())) {
                GUI.ItemHandler itemHandler = windowHandler.getItems().get(event.getSlot());
                Objects.requireNonNull(itemHandler, "GUI::ItemHandler cannot be null.");
                itemHandler.clickEvent(event);
                event.setResult(Event.Result.DENY);
                event.setCancelled(true);
            }
        }

        switch (getGame().getState()) {
            case ACTIVE:
                break;
            case CREATOR:
                ItemStack itemStack = event.getCurrentItem();
                if (itemStack != null) creatorItems(itemStack.getItemMeta(), event);
                break;
            case END:
            case RESET:
            case WAIT:
                event.setCancelled(true);
                break;
        }
    }

    @EventHandler
    public void onInventoryInteract(InventoryInteractEvent event) {
        if (GUI.WindowHandler.getWindows().containsKey(event.getInventory().getTitle())) {
            event.setResult(Event.Result.DENY);
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.hasItem()) {
            if (event.getItem().hasItemMeta()) {
                String name = event.getItem().getItemMeta().getDisplayName();
                GUI.ItemHandler itemHandler = GUI.ItemHandler.getItems().get(name);
                if (itemHandler != null) {
                    itemHandler.interactEvent(event);
                    event.setCancelled(true);
                }
            }
        }

//        if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
//            if (!event.hasItem()) return;
//            ItemStack itemStack = event.getItem();
//            if (!itemStack.hasItemMeta()) return;
//            Player player = event.getPlayer();
//            switch (getGame().getGameState()) {
//                case ACTIVE:
//                    activeInteract(player, itemStack.getItemMeta());
//                    break;
//                case CREATOR:
//                    creatorInteract(player, itemStack.getItemMeta());
//                    break;
//                case WAITING:
//                    waitingInteract(player, itemStack.getItemMeta());
//                    break;
//                case END:
//                    endInteract(player, itemStack.getItemMeta());
//                    break;
//                case RESET:
//                    resetInteract(player, itemStack.getItemMeta());
//                    break;
//            }
//        }
    }

    private void creatorItems(ItemMeta itemMeta, Cancellable cancellable) {
        if (itemMeta == null) return;
        if (!itemMeta.hasDisplayName()) return;
        switch (itemMeta.getDisplayName()) {
            case GUI.ShowCreator.editorInHand:
                cancellable.setCancelled(true);
                break;
        }
    }

//    private void activeInteract(Player player, ItemMeta itemMeta) {
//        if (!itemMeta.hasDisplayName()) return;
//
//    }

    private void creatorInteract(Player player, ItemMeta itemMeta) {
        if (!itemMeta.hasDisplayName()) return;
        switch (itemMeta.getDisplayName()) {
            case GUI.ShowCreator.editorInHand:
                GUI.ShowCreator.show(getGame(), player);
                break;
        }
    }

//    private void waitingInteract(Player player, ItemMeta itemMeta) {
//        if (!itemMeta.hasDisplayName()) return;
//        switch (itemMeta.getDisplayName()) {
//            case GUI.ShowLobby.teamsInHand:
//                GUI.ShowLobby.show(bedWars.getGame(), player);
//                break;
//            case GUI.ShowLobby.quitInHand:
//                player.kickPlayer("§cYou quit this server.");
//                break;
//        }
//    }
//
//    private void endInteract(Player player, ItemMeta itemMeta) {
//        if (!itemMeta.hasDisplayName()) return;
//
//    }
//
//    private void resetInteract(Player player, ItemMeta itemMeta) {
//        if (!itemMeta.hasDisplayName()) return;
//
//    }

}

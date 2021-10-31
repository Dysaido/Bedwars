package hu.dysaido.bedwars.gui;


import hu.dysaido.bedwars.BedWars;
import hu.dysaido.bedwars.api.User;
import hu.dysaido.bedwars.game.Game;
import hu.dysaido.bedwars.team.Team;
import hu.dysaido.bedwars.api.team.TeamColor;
import hu.dysaido.bedwars.team.TeamManager;
import hu.dysaido.bedwars.util.Log;
import hu.dysaido.bedwars.world.generators.GeneratorType;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Wool;

import java.util.*;
import java.util.function.Consumer;

public class GUI {

    private static final String TAG = "GUI";

    private GUI() {
    }

    public static ItemStack speedCreateItem(Material material, int amount, String name) {
        return speedCreateItem(material, amount, name, null);
    }

    public static ItemStack speedCreateItemWool(DyeColor dyeColor, int amount, String name) {
        return speedCreateItemWool(dyeColor, amount, name, null);
    }

    public static ItemStack speedCreateItem(Material material, int amount, String name, List<String> lore) {
        ItemStack itemStack = new ItemStack(material, amount);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemStack speedCreateItemWool(DyeColor dyeColor, int amount, String name, List<String> lore) {
        Wool wool = new Wool(dyeColor);
        ItemStack itemStack = wool.toItemStack(amount);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    private static void requireWindow(String title) {
        if (WindowHandler.windows.containsKey(title)) {
            Log.information(TAG, "Require");
            WindowHandler.windows.get(title).clear();
        }
    }

    private static ItemHandler requireItemHandler(String title) {
        if (ItemHandler.items.containsKey(title)) return ItemHandler.items.get(title);
        return null;
    }

    public static class ShowCreator {

        public static final String mainInventoryTitle = "§dMain page";
        public static final String selectGeneratorsTitle = "§dGenerators";
        public static final String selectBedsOfTeamsTitle = "§dBeds of teams";
        public static final String selectSpawnsOfTeamsTitle = "§dSpawns of teams";
        public static final String setGeneratorTime = "§dTime set";

        public static final String bedsName = "§eBeds place";
        public static final String configName = "§eConfiguration";
        public static final String spawnsName = "§eTeams set spawns";
        public static final String lobbyName = "§eLobby place";
        public static final String npcShopName = "§eVillagers";
        public static final String npcEnchantName = "§eEnchanters";
        public static final String generatorsName = "§eGenerators";
        public static final String applyName = "§eApply";
        public static final String exitName = "§eExit";


        public static final String editorInHand = "§eEditor";


        public static void createPlayerInventory(Game game, Player player) {
            Inventory inventory = player.getInventory();
            inventory.clear();
            ItemHandler editor = new ItemHandler(speedCreateItem(Material.WORKBENCH, 1, editorInHand));
            editor.interactTask(event -> show(game, player));
            inventory.setItem(4, editor.getItemStack());
        }

        public static void show(Game game, Player player) {
            requireWindow(mainInventoryTitle);
            WindowHandler windowHandler = new WindowHandler(mainInventoryTitle, 4);
            ItemHandler bedsHandler = new ItemHandler(speedCreateItem(Material.BED, 1, bedsName));
            ItemHandler spawnsHandler = new ItemHandler(speedCreateItem(Material.ENDER_PEARL, 1, spawnsName));
            ItemHandler lobbyHandler = new ItemHandler(speedCreateItem(Material.EYE_OF_ENDER, 1, lobbyName));
            ItemHandler npcShopHandler = new ItemHandler(speedCreateItem(Material.MONSTER_EGG, 1, npcShopName));
            ItemHandler npcEnchantHandler = new ItemHandler(speedCreateItem(Material.EXP_BOTTLE, 1, npcEnchantName));
            ItemHandler generatorsHandler = new ItemHandler(speedCreateItem(Material.DROPPER, 1, generatorsName));
            ItemHandler configHandler = new ItemHandler(speedCreateItem(Material.BEDROCK, 1, configName));
            ItemHandler exitHandler = new ItemHandler(speedCreateItem(Material.BARRIER, 1, exitName));
            bedsHandler.clickTask(event -> {
                windowHandler.close();
                showPlaceTeamBed(game, player, windowHandler);
            });

            generatorsHandler.clickTask(event -> {
                windowHandler.close();
                showGenerators(game, player, windowHandler);
            });
            exitHandler.clickTask(event -> windowHandler.close());
            windowHandler.setItem(1, 1, lobbyHandler);
            windowHandler.setItem(2, 1, spawnsHandler);
            windowHandler.setItem(3, 1, bedsHandler);
            windowHandler.setItem(4, 1, npcShopHandler);
            windowHandler.setItem(5, 1, npcEnchantHandler);
            windowHandler.setItem(6, 1, generatorsHandler);
            windowHandler.setItem(7, 1, configHandler);
            windowHandler.setItem(4, 2, exitHandler);
            windowHandler.show(player);
        }


        private static void showPlaceTeamSpawn() {

        }

        private static void showPlaceTeamBed(Game game, Player player, WindowHandler mainWindow) {
            requireWindow(selectBedsOfTeamsTitle);
            WindowHandler windowHandler = new WindowHandler(selectBedsOfTeamsTitle, 3);
            int i = 0;
            for (TeamColor teamColor : TeamColor.values()) {
                Team team = game.getTeamManager().findTeamByColor(teamColor);
                ItemHandler handler = new ItemHandler(speedCreateItemWool(teamColor.getDyeColor(), 1, team.getName()));
//                handler.clickTask(event -> {
//                    windowHandler.close();
//                    game.joinTeam(player, team);
//                    player.sendTitle(team.getName(), "", 20, 20, 20);
//                });
                windowHandler.setItem(i, 1, handler);
                i++;
            }
            ItemHandler exit = new ItemHandler(speedCreateItem(Material.BARRIER, 1, exitName));
            exit.clickTask(event -> {
                windowHandler.close();
                mainWindow.show(player);
            });
            windowHandler.setItem(i, 1, exit);
            windowHandler.show(player);

        }

        private static void showGenerators(Game game, Player player, WindowHandler mainWindow) {
            requireWindow(selectGeneratorsTitle);
            WindowHandler windowHandler = new WindowHandler(selectGeneratorsTitle, 3);
            int i = 0;
            for (GeneratorType type : GeneratorType.values()) {
                String name = "§e" + type.formattedName();
                ItemHandler handler = new ItemHandler(speedCreateItem(type.getBlockMaterial(), 1, name));
                handler.clickTask(event -> {
                    windowHandler.close();
                    setGeneratorTime(game, player, type, mainWindow, name);
                });
                switch (i) {
                    case 0:
                        windowHandler.setItem(3, 1, handler);
                        break;
                    case 1:
                        windowHandler.setItem(4, 0, handler);
                        break;
                    case 2:
                        windowHandler.setItem(5, 1, handler);
                        break;
                    case 3:
                        windowHandler.setItem(4, 2, handler);
                        break;
                }
                i++;
            }

            ItemHandler exit = new ItemHandler(speedCreateItem(Material.BARRIER, 1, exitName));
            exit.clickTask(event -> {
                windowHandler.close();
                mainWindow.show(player);
            });
            windowHandler.setItem(4, 1, exit);
            windowHandler.show(player);
        }

        private static void setGeneratorTime(Game game, Player player, GeneratorType type, WindowHandler mainWindow, String name) {
            requireWindow(setGeneratorTime);
            WindowHandler windowHandler = new WindowHandler(setGeneratorTime, 5);
            ItemHandler generator = new ItemHandler(speedCreateItem(type.getBlockMaterial(), 1, name));
            ItemHandler addSecond = new ItemHandler(speedCreateItem(Material.REDSTONE_BLOCK, 1, "addSecond"));
            ItemHandler removeSecond = new ItemHandler(speedCreateItem(Material.REDSTONE_BLOCK, 1, "removeSecond"));
            ItemHandler addMinute = new ItemHandler(speedCreateItem(Material.REDSTONE_BLOCK, 1, "addMinute"));
            ItemHandler removeMinute = new ItemHandler(speedCreateItem(Material.REDSTONE_BLOCK, 1, "removeMinute"));
            ItemHandler apply = new ItemHandler(speedCreateItem(Material.EXP_BOTTLE, 1, exitName));
            apply.clickTask(event -> {
                Player player1 = (Player) event.getWhoClicked();
                windowHandler.close();
//                placeGenerator(game, player1, type);
            });
            ItemHandler exit = new ItemHandler(speedCreateItem(Material.BARRIER, 1, exitName));
            exit.clickTask(event -> {
                Player player1 = (Player) event.getWhoClicked();
                windowHandler.close();
                mainWindow.show(player1);
            });
            windowHandler.setItem(0, 2, removeMinute);
            windowHandler.setItem(2, 2, removeSecond);
            windowHandler.setItem(4,2, generator);
            windowHandler.setItem(6, 2, addSecond);
            windowHandler.setItem(8, 2, addMinute);
            windowHandler.setItem(8, 0, exit);
            windowHandler.show(player);

        }

        private static void placeGenerator(Game game, Player player, GeneratorType type) {
            PlayerInventory inventory = player.getInventory();
            ItemStack itemStack = speedCreateItem(type.getBlockMaterial(), 1, "§e" + type.formattedName() + " generator");
//            handler.interactTask(event -> {
//                Player eventPlayer = event.getPlayer();
//                if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
//                    Block block = eventPlayer.getTargetBlock(null, 3);
//                    double time = Double.parseDouble(args.get(2));
//                    game.getWorldManager().saveGenerator(block, type, time);
//                } else if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
//                    Block block = eventPlayer.getTargetBlock(null, 3);
//                    game.getWorldManager().deleteGenerator(block);
//                    eventPlayer.sendMessage("successful remove");
//                }
//            });

        }
    }

    @Deprecated
    public static class LobbyGUI {
        public static final HashMap<TeamColor, ItemStack> SELECTED_ITEMS = new HashMap<>();
        public static final String selectInventoryTitle = "§dBedWars lobby";
        public static final String teamsInHand = "§eSelect your team";
        public static final String quitInHand = "§eQuit this server";
        public static final String leaveInHand = "§eQuit your team";
        private static final ArrayList<ItemStack> ITEM_WOOLS = new ArrayList<>();

        public static void createPlayerInventory(Player player) {
            Inventory inventory = player.getInventory();
            ItemStack slimeBall = speedCreateItem(Material.SLIME_BALL, 1, quitInHand);
            ItemStack bedSelect = speedCreateItem(Material.BED, 1, teamsInHand);
            ItemStack quitTeam = speedCreateItem(Material.EYE_OF_ENDER, 1, leaveInHand);
            inventory.setItem(0, bedSelect);
            inventory.setItem(4, quitTeam);
            inventory.setItem(8, slimeBall);
        }

        public static void setItemWools(TeamManager teamManager) {
            for (Team team : teamManager.getTeams()) {
                if (ITEM_WOOLS.size() <= teamManager.getTeams().size()) {
                    ItemStack itemStack = speedCreateItemWool(team.getColor().getDyeColor(), 1, team.getColor().getChatColor()
                            + team.getName() + " §7(" + team.getMembers().size() + "/" + team.getMaxMembers() + ")");
                    ITEM_WOOLS.add(itemStack);
                    SELECTED_ITEMS.put(team.getColor(), itemStack);
                }
            }
        }

        public static void createTeamSelectInventory(Player player) {
            Inventory inventory = Bukkit.getServer().createInventory(null, 9, selectInventoryTitle);
            for (int i = 0; i < ITEM_WOOLS.size(); i++) inventory.setItem(i, ITEM_WOOLS.get(i));
            player.openInventory(inventory);
        }

    }

    public static class ShowLobby {
        public static final String mainInventoryTitle = "§dBedWars lobby";
        public static final String teamsInHand = "§eSelect your team";
        public static final String quitInHand = "§eQuit this server";
        private static final String leaveInHand = "§eQuit your team";

        public static void createPlayerInventory(Game game, Player player) {
            Inventory inventory = player.getInventory();
            ItemHandler bedItem = new ItemHandler(speedCreateItem(Material.BED, 1, teamsInHand));
            ItemHandler slimeItem = new ItemHandler(speedCreateItem(Material.SLIME_BALL, 1, quitInHand));

            bedItem.interactTask(event -> show(game, player));
            slimeItem.interactTask(event -> player.kickPlayer("§cYou quit this server."));
            inventory.setItem(0, bedItem.getItemStack());
            inventory.setItem(8, slimeItem.getItemStack());
        }

        public static void show(Game game, Player player) {
            requireWindow(mainInventoryTitle);
            User user = BedWars.getUser(player);
            WindowHandler windowHandler = new WindowHandler(mainInventoryTitle, 3);
            int i = 0;
            for (TeamColor teamColor : TeamColor.values()) {
                Team team = game.getTeamManager().findTeamByColor(teamColor);
                ItemHandler handler = new ItemHandler(speedCreateItemWool(teamColor.getDyeColor(), 1, team.getName()));
                handler.clickTask(event -> {
                    windowHandler.close();
                    game.joinTeam(user, team);
                    player.sendTitle(team.getName(), "", 20, 20, 20);
                });
                windowHandler.setItem(i, 1, handler);
                i++;
            }
            ItemHandler handler = new ItemHandler(speedCreateItem(Material.BARRIER, 1, leaveInHand));
            handler.clickTask(event -> {
                windowHandler.close();
                Team team = game.findTeamByUser(user);
                if (team == null) return;
                game.quitTeam(user);
                player.sendTitle("Quit team", team.getName(), 20, 20, 20);
            });
            windowHandler.setItem(i, 1, handler);
            windowHandler.show(player);
        }

    }

    public static class ShowVendor {

        public static void show(Player player) {

        }

    }

    public static class ShowEnchanter {

        public static void show(Player player) {

        }

    }

    public static class ItemHandler {

        private static final Map<String, ItemHandler> items = new HashMap<>();
        private final ItemStack itemStack;
        private Consumer<InventoryClickEvent> clickEventConsumer;
        private Consumer<PlayerInteractEvent> interactEventConsumer;

        public ItemHandler(ItemStack itemStack) {
            this.itemStack = itemStack;
            String name = itemStack.getItemMeta().getDisplayName();
            items.remove(name);
            items.put(name, this);
        }

        public static Map<String, ItemHandler> getItems() {
            return Collections.unmodifiableMap(items);
        }

        public void clickTask(Consumer<InventoryClickEvent> clickEventConsumer) {
            this.clickEventConsumer = clickEventConsumer;
        }

        public void clickEvent(InventoryClickEvent event) {
            if (clickEventConsumer == null) {
                Log.warning(TAG, "ClickTask cannot be null.");
                return;
            }
            this.clickEventConsumer.accept(event);
        }

        public void interactTask(Consumer<PlayerInteractEvent> interactEventConsumer) {
            this.interactEventConsumer = interactEventConsumer;
        }

        public void interactEvent(PlayerInteractEvent event) {
            if (interactEventConsumer == null) {
                Log.warning(TAG, "InteractTask cannot be null.");
                return;
            }
            this.interactEventConsumer.accept(event);
        }

        public ItemStack getItemStack() {
            return itemStack;
        }
    }

    public static class WindowHandler {

        private static final Map<String, WindowHandler> windows = new HashMap<>();
        private final Map<Integer, ItemHandler> items;
        private final Inventory inventory;
        private final String title;
        private Player player;

        public WindowHandler(String title, int rows) {
            this.title = title;
            this.items = new HashMap<>(rows * 9);
            this.inventory = Bukkit.createInventory(null, rows * 9, this.title);
            windows.put(title, this);
        }

        public static Map<String, WindowHandler> getWindows() {
            return Collections.unmodifiableMap(windows);
        }

        public void setItem(int x, int y, ItemHandler item) {
            int pos = x + y * 9;
            items.put(pos, item);
            inventory.setItem(pos, item.getItemStack());
        }

        public Inventory getInventory() {
            return inventory;
        }

        public Map<Integer, ItemHandler> getItems() {
            return Collections.unmodifiableMap(items);
        }

        public void show(Player player) {
            Inventory inventory = Bukkit.createInventory(player, getInventory().getSize(), getInventory().getTitle());
            inventory.setContents(getInventory().getContents());
            player.openInventory(inventory);
            this.player = player;
        }

        public void close() {
            player.closeInventory();
        }

        public void clear() {
            windows.remove(this.title);
        }
    }

}

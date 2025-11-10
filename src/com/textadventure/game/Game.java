package com.textadventure.game;

import com.textadventure.model.*;
import com.textadventure.engine.GameLoader;
import com.textadventure.engine.GameLoader.GameDataException;
import com.textadventure.utils.SaveState;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Game {
    private Map<String, Room> rooms;
    private Player player;
    private final GameLoader gameLoader;
    private static final String SAVE_FILE_NAME = "savegame.json";
    private Map<String, Item> allGameItems;

    public Game() {
        this.gameLoader = new GameLoader();
        System.out.println("Game object created. Ready for initialization.");
    }

    public void initialize(String dataFilePath)
            throws IOException, com.google.gson.JsonSyntaxException, GameDataException, IllegalArgumentException {
        System.out.println("----------------------------------------");
        System.out.println("Initializing game from data file: " + dataFilePath + "...");
        System.out.println("----------------------------------------");

        System.out.println("[Initialize] Calling GameLoader.loadGameData()...");
        gameLoader.loadGameData(dataFilePath);
        System.out.println("[Initialize] Game data loaded successfully by GameLoader.");
        System.out.println("----------------------------------------");

        System.out.println("[Initialize] Retrieving loaded rooms from GameLoader...");
        Map<String, Room> loadedRoomsMap = gameLoader.getLoadedRooms();

        if (loadedRoomsMap == null || loadedRoomsMap.isEmpty())
            throw new GameDataException(
                    "Initialization failed: GameLoader returned null or empty rooms map after successful load.");

        this.rooms = loadedRoomsMap;
        System.out
                .println("[Initialize] Rooms map populated in Game instance. Total rooms loaded: " + this.rooms.size());
        System.out.println("----------------------------------------");

        this.allGameItems = gameLoader.getLoadedItems();
        if (this.allGameItems == null) {
            throw new GameDataException("Initialization failed: Could not load item definitions.");
        }
        System.out.println("[Game.initialize] Rooms loaded: " + this.rooms.keySet());
        System.out.println("[Game.initialize] All game items loaded: " + this.allGameItems.keySet());

        System.out.println("[Initialize] Creating the Player object...");
        this.player = new Player();
        System.out.println("[Initialize] Player object created successfully.");

        System.out.println("[Initialize] Retrieving player start room name from GameLoader...");
        String startRoomName = gameLoader.getPlayerStartRoomName();

        if (startRoomName == null || !this.rooms.containsKey(startRoomName))
            throw new GameDataException("Initialization failed: Player starting room '" + startRoomName +
                    "' (specified in JSON) does not correspond to any loaded room. Check JSON data integrity.");
        System.out.println("[Initialize] Start room name '" + startRoomName + "' confirmed to exist in loaded rooms.");

        this.player.setCurrentRoomName(startRoomName);
        System.out.println("[Initialize] Player's current room set to: '" + this.player.getCurrentRoomName() + "'");

        System.out.println("----------------------------------------");
        System.out.println("Game initialization complete!");
        System.out.println("Player is ready at location: " + this.player.getCurrentRoomName());
        System.out.println("----------------------------------------");
    }

    public Room getCurrentRoom() {
        return rooms.get(player.getCurrentRoomName());
    }

    public void processCommand(String[] commandParts) {
        if (commandParts.length == 0) return;

        String commandVerb = commandParts[0].toLowerCase();

        switch (commandVerb) {
            case "go" -> processGoCommand(commandParts);
            case "take" -> processTakeCommand(commandParts);
            case "inventory", "inv" -> processInventoryCommand();
            case "examine", "x" -> processExamineCommand(commandParts);
            case "use" -> processUseCommand(commandParts);
            case "look" -> processLookCommand();
            case "save" -> processSaveCommand();
            case "load" -> processLoadCommand();
            case "quit", "exit" -> System.exit(0);
            default -> {
                System.out.println("Sorry, I don't understand the command '" + commandVerb + "'.");
                System.out.println("Try one of these: go, look, take, inventory (inv), examine (x), use, save, load, quit");
            }
        }
        System.out.println();
    }

    private void processGoCommand(String[] parts) {
        if (parts.length < 2) {
            System.out.println("Go where?");
            return;
        }
        String direction = parts[1].toLowerCase();
        Room current = getCurrentRoom();
        ExitData exitData = current.getExit().get(direction);

        if (exitData == null) {
            System.out.println("You can't go " + direction + " from here.");
            return;
        }

        if (!checkConditions(exitData.getConditions(), player)) {
            return;
        }

        String targetRoom = exitData.getTargetRoom();
        if (targetRoom == null || !rooms.containsKey(targetRoom)) {
            System.out.println("Error: Invalid exit destination.");
            return;
        }

        player.setCurrentRoomName(targetRoom);
        System.out.println("You move " + direction + " to " + getCurrentRoom().getName() + ".");
        processLookCommand();
    }

    private void processTakeCommand(String[] parts) {
        if (parts.length < 2) {
            System.out.println("Take what?");
            return;
        }
        String itemName = String.join(" ", Arrays.copyOfRange(parts, 1, parts.length));
        Room current = getCurrentRoom();
        Optional<Item> optItem = current.findItemByName(itemName);
        if (optItem.isEmpty()) {
            System.out.println("There is no '" + itemName + "' here.");
            return;
        }
        Item item = optItem.get();
        current.getItems().remove(item);
        player.takeItem(item);
        System.out.println("You take the " + item.getName() + ".");
    }

    private void processInventoryCommand() {
        List<Item> inv = player.getInventory();
        if (inv.isEmpty()) {
            System.out.println("Your inventory is empty.");
        } else {
            String items = inv.stream().map(Item::getName).collect(Collectors.joining(", "));
            System.out.println("You are carrying: " + items);
        }
    }

    private void processExamineCommand(String[] parts) {
        if (parts.length < 2) {
            System.out.println("Examine what?");
            return;
        }
        String target = String.join(" ", Arrays.copyOfRange(parts, 1, parts.length));
        Optional<Item> invItem = player.findItemInventory(target);
        if (invItem.isPresent()) {
            System.out.println(invItem.get().getDescription());
            return;
        }
        Optional<Item> roomItem = getCurrentRoom().findItemByName(target);
        if (roomItem.isPresent()) {
            System.out.println(roomItem.get().getDescription());
            return;
        }
        System.out.println("There is no '" + target + "' to examine.");
    }

    private void processUseCommand(String[] parts) {
        if (parts.length < 4 || !parts[2].equalsIgnoreCase("on")) {
            System.out.println("Usage: use <item> on <target>");
            return;
        }
        String itemName = parts[1];
        String targetName = String.join(" ", Arrays.copyOfRange(parts, 3, parts.length));

        Optional<Item> optItem = player.findItemInventory(itemName);
        if (optItem.isEmpty()) {
            System.out.println("You don't have a '" + itemName + "'.");
            return;
        }
        Item item = optItem.get();
        Item.Usability usability = item.getUsability();
        if (usability == null || !targetName.equalsIgnoreCase(usability.getTarget())) {
            System.out.println("You can't use the " + item.getName() + " on that.");
            return;
        }

        Room current = getCurrentRoom();
        Optional<Item> optTarget = current.findItemByName(targetName);
        if (optTarget.isEmpty()) {
            System.out.println("There is no '" + targetName + "' here.");
            return;
        }

        System.out.println(usability.getEffectDescription());

        // Apply effects
        if (usability.getRemovesTarget() != null) {
            current.findItemByName(usability.getRemovesTarget()).ifPresent(current::removeItem);
        }
        if (usability.getAddsTarget() != null) {
            Item newItem = allGameItems.get(usability.getAddsTarget());
            if (newItem != null) current.addItem(newItem);
        }
        if (usability.getChangesRoomDescriptionTo() != null) {
            current.setDescription(usability.getChangesRoomDescriptionTo());
        }
        if (usability.getAddsItemToInventory() != null) {
            Item addItem = allGameItems.get(usability.getAddsItemToInventory());
            if (addItem != null) player.takeItem(addItem);
        }
        if (usability.isConsumesItem()) {
            player.removeItem(item.getName());
        }
        if (usability.getModifiesExit() != null) {
            ExitModification mod = usability.getModifiesExit();
            ExitData ed = current.getExit().get(mod.getDirection());
            if (ed != null && ed.getConditions() != null && mod.isClearRequiresItem()) {
                ed.getConditions().setRequiresItem(null);
                System.out.println("The exit to the " + mod.getDirection() + " is now open.");
            }
        }
    }

    private void processLookCommand() {
        Room room = getCurrentRoom();
        System.out.println("\n" + room.getName());
        System.out.println(room.getDescription());

        List<Item> items = room.getItems();
        if (!items.isEmpty()) {
            String list = items.stream().map(Item::getName).collect(Collectors.joining(", "));
            System.out.println("You see: " + list);
        }

        Map<String, ExitData> exits = room.getExit();
        if (!exits.isEmpty()) {
            String dirs = exits.keySet().stream().sorted().collect(Collectors.joining(" "));
            System.out.println("Exits: " + dirs);
        }
    }

    private void processSaveCommand() {
        SaveState state = new SaveState(
            player.getCurrentRoomName(),
            player.getInventory().stream().map(Item::getName).toList(),
            new HashMap<>()
        );
        for (Map.Entry<String, Room> entry : rooms.entrySet()) {
            List<String> roomItems = entry.getValue().getItems().stream().map(Item::getName).toList();
            state.getRoomItemStates().put(entry.getKey(), roomItems);
        }
        try (FileWriter writer = new FileWriter(SAVE_FILE_NAME)) {
            new GsonBuilder().setPrettyPrinting().create().toJson(state, writer);
            System.out.println("Game saved to " + SAVE_FILE_NAME);
        } catch (IOException e) {
            System.out.println("Failed to save game: " + e.getMessage());
        }
    }

    private void processLoadCommand() {
        try (FileReader reader = new FileReader(SAVE_FILE_NAME)) {
            SaveState loadedState = new Gson().fromJson(reader, SaveState.class);
            if (loadedState == null) {
                System.out.println("No saved game found.");
                return;
            }

            if (player == null) {
                player = new Player();
                System.out.println("[Debug] Player object was null, created a new one for loading.");
            }

            if (rooms == null) {
                System.err.println("ERROR: Cannot apply loaded state, game rooms are not initialized.");
                return;
            }

            String loadedLocation = loadedState.getPlayerLocation();
            if (loadedLocation != null && rooms.containsKey(loadedLocation)) {
                player.setCurrentRoomName(loadedLocation);
                System.out.println("[Debug] Player location set to: " + loadedLocation);
            } else {
                System.err.println("WARNING: Loaded location '" + loadedLocation + "' is invalid. Player location not updated.");
            }

            List<String> loadedInvNames = loadedState.getPlayerInventory();
            if (loadedInvNames != null) {
                player.getInventory().clear();
                System.out.println("[Debug] Player inventory cleared.");
                for (String itemName : loadedInvNames) {
                    Item item = allGameItems.get(itemName);
                    if (item != null) {
                        player.takeItem(item);
                        System.out.println("[Debug] Added '" + itemName + "' to player inventory.");
                    } else {
                        System.err.println("WARNING: Unknown item '" + itemName + "' in saved inventory. Skipping.");
                    }
                }
            } else {
                System.err.println("WARNING: Saved inventory data is missing.");
                player.getInventory().clear();
            }

            Map<String, List<String>> loadedRoomStates = loadedState.getRoomItemStates();
            if (loadedRoomStates != null) {
                for (Map.Entry<String, Room> roomEntry : rooms.entrySet()) {
                    String roomName = roomEntry.getKey();
                    Room room = roomEntry.getValue();
                    if (room == null) continue;

                    List<String> itemNamesForThisRoom = loadedRoomStates.get(roomName);
                    room.getItems().clear();
                    System.out.println("[Debug] Cleared items for room: " + roomName);

                    if (itemNamesForThisRoom != null) {
                        for (String itemName : itemNamesForThisRoom) {
                            Item item = allGameItems.get(itemName);
                            if (item != null) {
                                room.addItem(item);
                                System.out.println("[Debug] Added '" + itemName + "' back to room '" + roomName + "'.");
                            } else {
                                System.err.println("WARNING: Unknown item '" + itemName + "' in saved state for room '" + roomName + "'. Skipping.");
                            }
                        }
                    }
                }
            } else {
                System.err.println("WARNING: Saved room item state data is missing.");
            }

            System.out.println("Game loaded successfully!");
        } catch (IOException e) {
            System.out.println("Failed to load game: " + e.getMessage());
        }
    }

    private boolean checkConditions(Conditions conditions, Player player) {
        if (conditions == null) {
            return true;
        }

        Object requiredItemsObj = conditions.getRequiresItem();

        if (requiredItemsObj == null) {
            return true;
        }

        if (requiredItemsObj instanceof String) {
            String requiredItemName = (String) requiredItemsObj;
            if (player.findItemInventory(requiredItemName).isEmpty()) {
                if (conditions.getFailMessage() != null) {
                    System.out.println(conditions.getFailMessage());
                }
                return false;
            }
        } else if (requiredItemsObj instanceof List) {
            @SuppressWarnings("unchecked")
            List<String> requiredItems = (List<String>) requiredItemsObj;
            for (String itemName : requiredItems) {
                if (player.findItemInventory(itemName).isEmpty()) {
                    if (conditions.getFailMessage() != null) {
                        System.out.println(conditions.getFailMessage());
                    }
                    return false;
                }
            }
        }
        return true;
    }
}
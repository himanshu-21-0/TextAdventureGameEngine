package com.textadventure.game;

import com.textadventure.model.Room;
import com.textadventure.model.Player;
import com.textadventure.model.Item;
import com.textadventure.engine.GameLoader;
import com.textadventure.engine.GameLoader.GameDataException;

import com.google.gson.JsonSyntaxException;
import java.io.IOException;

import java.util.Map;
import java.util.List;
import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.Collectors;

public class Game {
    private Map<String, Room> rooms;
    private Player player;
    private final GameLoader gameLoader;

    public Game() {
        this.gameLoader = new GameLoader();
        System.out.println("Game object created. Ready for initialization.");
    }

    public void initialize(String dataFilePath)
            throws IOException, JsonSyntaxException, GameDataException, IllegalArgumentException {
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

    public Room getRoom(String roomName) {
        if (this.rooms == null) {
            System.err.println(
                    "Warning: Attempted to getRoom('" + roomName + "') but the game rooms map is not initialized.");
            return null;
        }
        return this.rooms.get(roomName);
    }

    public Room getCurrentRoom() {
        if (this.player == null) {
            System.err.println("Warning: Attempted to getCurrentRoom but the player object is not initialized.");
            return null;
        }
        String currentRoomName = this.player.getCurrentRoomName();
        if (currentRoomName == null) {
            System.err.println("Warning: Attempted to getCurrentRoom but the player's current room name is null.");
            return null;
        }
        return getRoom(currentRoomName);
    }

    public void processCommand(String[] commandParts) {
        if (commandParts == null || commandParts.length == 0) {
            System.out.println("Huh? Please enter a command.");
            return;
        }

        String commandVerb = commandParts[0];
        System.out.println("[Game.processCommand] Processing verb: '" + commandVerb + "'");

        switch (commandVerb) {
            case "go":
                if (commandParts.length < 2)
                    System.out.println("Go where? Please specify a direction (e.g., 'go north').");
                else {
                    String direction = commandParts[1];
                    System.out.println("[Game.processCommand] Extracted direction: '" + direction + "'");

                    Room currentRoom = getCurrentRoom();

                    if (currentRoom == null) {
                        System.err.println(
                                "[Game.processCommand] CRITICAL ERROR: Cannot determine current location to process 'go' command.");
                        break;
                    }
                    System.out.println("[Game.processCommand] Current room is: '" + currentRoom.getName() + "'");

                    Map<String, String> exits = currentRoom.getExits();

                    if (exits == null) {
                        System.err.println("[Game.processCommand] Error: Room '" + currentRoom.getName()
                                + "' has a null exits map!");
                        System.out.println("There seem to be no exit from here.");
                        break;
                    }

                    if (exits.containsKey(direction)) {
                        System.out.println("[Game.processCommand] Valid exit direction '" + direction + "' found.");

                        String nextRoomName = exits.get(direction);

                        if (nextRoomName == null || nextRoomName.trim().isEmpty()) {
                            System.err.println("[Game.processCommand] ERROR: Exit '" + direction + "' in room '"
                                    + currentRoom.getName()
                                    + "' leads to an invalid room name (null or empty). Check JSON data.");
                            System.out.println("Hmm, the way " + direction + " seems blocked or leads nowhere.");
                            break;
                        }

                        System.out.println("[Debug] Next room name retrieved: '" + nextRoomName + "'");

                        player.setCurrentRoomName(nextRoomName);

                        System.out.println("[Debug] Player location updated to: '" + player.getCurrentRoomName() + "'");

                        System.out.println("You move " + direction + ".");

                    } else {
                        System.out.println("You can't go " + direction + " from here.");

                    }
                }
                break;
            case "look":
                System.out.println("You look around");
                break;
            case "inventory":
            case "inv":
                List<Item> inventory = player.getInventory();

                if (inventory == null || inventory.isEmpty()) {
                    System.out.println("Your inventory is empty.");
                } else {
                    System.out.println("You are carrying: ");
                    for (Item item : inventory) {
                        System.out.println("-" + item.getName());
                    }
                }
                break;
            case "take":
                if (commandParts.length < 2) {
                    System.out.println("Take what? Please specify an item (e.g., 'take key').");
                    break;
                }

                String targetItemName = String.join(" ", Arrays.copyOfRange(commandParts, 1, commandParts.length));
                System.out.println("[Debug] Trying to take item: '" + targetItemName + "'");

                Room currentRoom = getCurrentRoom();
                if (currentRoom == null) {
                    System.err.println(
                            "[Game.processCommand] CRITICAL ERROR: Cannot determine current location to take item.");
                    break;
                }

                List<Item> itemInRoom = currentRoom.getItems();
                if (itemInRoom == null) {
                    System.err.println(
                            "[Game.processCommand] ERROR: Room '" + currentRoom.getName() + "' has a null items list!");
                    System.out.println("There appears to be nothing to take here.");
                    break;
                }

                Item itemToTake = null;
                boolean itemFound = false;

                Iterator<Item> iterator = itemInRoom.iterator();
                while (iterator.hasNext()) {
                    Item roomItem = iterator.next();

                    if (roomItem.getName().equalsIgnoreCase(targetItemName)) {
                        itemToTake = roomItem;
                        itemFound = true;

                        iterator.remove();
                        System.out.println("[Debug] Removed '" + itemToTake.getName() + "' from room '"
                                + currentRoom.getName() + "'.");
                        break;
                    }
                }
                if (itemFound && itemToTake != null) {
                    player.takeItem(itemToTake);
                    System.out.println("[Debug] Added '" + itemToTake.getName() + "' to player inventory.");

                    System.out.println("You take the " + itemToTake.getName() + ".");
                } else {
                    System.out.println("There is no '" + targetItemName + "' here to take.");
                }
                break;
            case "examine":
            case "x":
                if (commandParts.length < 2) {
                    System.out.println("Examine what? Please specify an item (e.g., 'examine key')");
                    break;
                }

                String targetExamineItemName = String.join(" ",
                        Arrays.copyOfRange(commandParts, 1, commandParts.length));
                System.out.println("[Debug] Player wants to examine: '" + targetExamineItemName + "'");

                boolean itemExamined = false;

                List<Item> inventory = player.getInventory();
                if (inventory != null && !inventory.isEmpty()) {
                    for (Item item : inventory) {
                        if (item.getName().equalsIgnoreCase(targetExamineItemName)) {
                            System.out.println(item.getDescription());
                            itemExamined = true;
                            break;
                        }
                    }
                }
                if (!itemExamined) {
                    Room currentRoom = getCurrentRoom();
                    if (currentRoom != null) {
                        List<Item> itemsInRoom = currentRoom.getItems();
                        if (itemsInRoom != null && !itemsInRoom.isEmpty()) {
                            for (Item item : itemsInRoom) {
                                if (item.getName().equalsIgnoreCase(targetExamineItemName)) {
                                    System.out.println(item.getDescription());
                                    itemExamined = true;
                                    break;
                                }
                            }
                        }
                    } else {
                        System.err.println(
                                "[Game.processCommand] ERROR: Cannot examine room items, current room is null.");
                        itemExamined = true;
                    }
                }
                if (!itemExamined) {
                    System.out.println(
                            "You don't see any '" + targetExamineItemName + "' here or in your inventory to examine.");
                }
                break;
            default:
                System.out.println(
                        "Sorry, I don't know how to '" + commandVerb + "'. Try 'go', 'look', 'inventory', or 'quit'.");
                break;
        }
    }
}
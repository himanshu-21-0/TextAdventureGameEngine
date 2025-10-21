package com.textadventure.engine;

import com.textadventure.model.Room;
import com.textadventure.model.Item;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class GameLoader {

    private static class ItemData {
        String name;
        String description;
    }

    private static class RoomData {
        String name;
        String description;
        Map<String, String> exits;
        List<String> items;
    }

    private static class GameData {
        String playerStart;
        List<ItemData> items;
        List<RoomData> rooms;
    }

    private Map<String, Room> loadedRooms;
    private Map<String, Item> loadedItems;
    private String playerStartRoomName;

    private final Gson gson;

    public GameLoader() {
        this.loadedRooms = new HashMap<>();
        this.loadedItems = new HashMap<>();
        this.gson = new Gson();
        System.out.println("GameLoader initialized. Gson parser ready.");
    }

    public void loadGameData(String filePath)
            throws IOException, JsonSyntaxException, IllegalArgumentException, GameDataException {
        if (filePath == null || filePath.trim().isEmpty())
            throw new IllegalArgumentException("File path cannot be null or empty");

        System.out.println("Attempting to load game data from: " + filePath);
        Path path = Paths.get(filePath);
        String jsonContent;

        try {
            jsonContent = Files.readString(path);
            System.out.println("Successfully read content from file : " + filePath);
        } catch (IOException e) {
            System.err.println("ERROR: Failed to read game data file at path: " + filePath);
            System.err.println("Reason: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            throw new IOException("Failed to read game data file: '" + filePath + "'. Check file existence and permissions.", e);
        }

        GameData gameData;
        try {
            gameData = gson.fromJson(jsonContent, GameData.class);
            if (gameData == null)
                throw new GameDataException(
                        "Parsed game data is null. JSON might be empty or fundamentally incorrect.");

            System.out.println("JSON content successfully parsed into intermediate GameData object.");
        } catch (JsonSyntaxException e) {
            System.err.println("Error parsing JSON file: " + filePath + ". Invalid JSON syntax.");
            throw e;
        } catch (Exception e) {
            System.err.println("An unexpected error occured during JSON parsing: " + e.getMessage());
            throw new GameDataException("Unexpected error during JSON parsing.", e);
        }

        try {
            processIntermediateData(gameData);
            System.out.println("Intermediate GameData successfully processed into Room and Item objects.");
        } catch (GameDataException e) {
            System.err.println("Error processing game data logic: " + e.getMessage());
            throw e;
        }
    }

    private void processIntermediateData(GameData gameData) throws GameDataException {
        if (gameData.items == null) {
            System.out.println("Warning: 'items' array not found or null in JSON. No items will be loaded.");
            gameData.items = new ArrayList<>();
        }
        if (gameData.rooms == null)
            throw new GameDataException("'rooms' array not found or null in JSON. Cannot load game world.");
        if (gameData.playerStart == null || gameData.playerStart.trim().isEmpty())
            throw new GameDataException(
                    "'playerStart' field not found, null, or empty in JSON. Cannot determine starting room.");

        for (ItemData itemData : gameData.items) {
            if (itemData == null || itemData.name == null || itemData.name.trim().isEmpty()) {
                System.err.println("Warning: Skipping invalid item data (null or missing name).");
                continue;
            }
            String itemName = itemData.name.trim();
            if (loadedItems.containsKey(itemName)) {
                throw new GameDataException("Duplicate item name found in JSON: '" + itemName + "'");
            }
            String itemDesc = (itemData.description != null) ? itemData.description : "An item.";
            Item newItem = new Item(itemName, itemDesc);
            loadedItems.put(itemName, newItem);
            System.out.println("Created Item: " + itemName);
        }
        for (RoomData roomData : gameData.rooms) {
            if (roomData == null || roomData.name == null || roomData.name.trim().isEmpty()) {
                System.err.println("Warning: Skipping invalid room data (null or missing name).");
                continue;
            }
            String roomName = roomData.name.trim();
            if (loadedRooms.containsKey(roomName)) {
                throw new GameDataException("Duplicate room name found in JSON: '" + roomName + "'");
            }
            String roomDesc = (roomData.description != null) ? roomData.description : "A non-descript location";
            Room newRoom = new Room(roomName, roomDesc);
            loadedRooms.put(roomName, newRoom);
            System.out.println("Created Room: " + roomName);
        }

        for (RoomData roomData : gameData.rooms) {
            if (roomData == null || roomData.name == null || roomData.name.trim().isEmpty()) {
                continue;
            }
            String currentRoomName = roomData.name.trim();
            Room currentRoom = loadedRooms.get(currentRoomName);
            if (roomData.exits != null) {
                for (Map.Entry<String, String> exitEntry : roomData.exits.entrySet()) {
                    String direction = exitEntry.getKey();
                    String destinationRoomName = exitEntry.getValue();

                    if (direction == null || direction.trim().isEmpty() ||
                            destinationRoomName == null || destinationRoomName.trim().isEmpty()) {
                        System.err.println("Warning: Skipping invalid exit data in room '" + currentRoomName
                                + "' (null/empty direction or destination).");
                        continue;
                    }
                    if (!loadedRooms.containsKey(destinationRoomName.trim())) {
                        throw new GameDataException("Broken exit link in room '" + currentRoomName
                                + "': Destination room '" + destinationRoomName + "' not found.");
                    }
                    currentRoom.addExit(direction.toLowerCase().trim(), destinationRoomName.trim());
                    System.out.println("  Added exit from '" + currentRoomName + "' [" + direction.toLowerCase().trim()
                            + "] to '" + destinationRoomName.trim() + "'");
                }
            } else {
                System.out.println("  Room '" + currentRoomName + "' has no exits defined.");
            }
            if (roomData.items != null) {
                for (String itemNameFromJson : roomData.items) {
                    if (itemNameFromJson == null || itemNameFromJson.trim().isEmpty()) {
                        System.err.println("Warning: Skipping invalid item name (null/empty) listed in room '"
                                + currentRoomName + "'.");
                        continue;
                    }
                    String itemName = itemNameFromJson.trim();
                    Item itemToAdd = loadedItems.get(itemName);
                    if (itemToAdd == null) {
                        throw new GameDataException("Item '" + itemName + "' listed in room '" + currentRoomName
                                + "' but not defined in the top-level 'items' array.");
                    }
                    currentRoom.addItem(itemToAdd);
                    System.out.println("  Added item '" + itemName + "' to room '" + currentRoomName + "'");
                }
            } else {
                System.out.println("  Room '" + currentRoomName + "' has no items defined.");
            }
        }
        String startRoomName = gameData.playerStart.trim();
        if (!loadedRooms.containsKey(startRoomName)) {
            throw new GameDataException("Player starting room '" + startRoomName
                    + "' specified in 'playerStart' does not exist in the loaded rooms.");
        }
        this.playerStartRoomName = startRoomName;
        System.out.println("Player starting room set to: " + this.playerStartRoomName);
    }

    public Map<String, Room> getLoadedRooms() {
        return this.loadedRooms;
    }

    public Map<String, Item> getLoadedItems() {
        return this.loadedItems;
    }

    public String getPlayerStartRoomName() {
        return this.playerStartRoomName;
    }

    public static class GameDataException extends Exception {
        public GameDataException(String message) {
            super(message);
        }

        public GameDataException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
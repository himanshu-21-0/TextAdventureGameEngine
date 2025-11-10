package com.textadventure.engine;

import com.textadventure.model.*;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class GameLoader {

    private static class ItemData {
        String name;
        String description;
        Item.Usability usability;
    }

    private static class ExitDataJson {
        String targetRoom;
        Conditions conditions;
    }

    private static class RoomData {
        String name;
        String description;
        Map<String, String> exits;
        List<String> items;
        Map<String, ExitDataJson> exitData;
        // List<ConditionalDescription> conditionalDescriptions;
    }

    private static class GameData {
        String playerStart;
        List<ItemData> items;
        List<RoomData> rooms;
    }

    private Map<String, Room> loadedRooms = new HashMap<>();
    private Map<String, Item> loadedItems = new HashMap<>();
    private String playerStartRoomName;
    private final Gson gson = new Gson();

    public GameLoader() {
        System.out.println("GameLoader initialized. Gson parser ready.");
    }

    public void loadGameData(String filePath) throws IOException, JsonSyntaxException, GameDataException {
        if (filePath == null || filePath.trim().isEmpty())
            throw new IllegalArgumentException("File path cannot be null or empty");

        Path path = Paths.get(filePath);
        String jsonContent = Files.readString(path);
        GameData gameData = gson.fromJson(jsonContent, GameData.class);
        if (gameData == null)
            throw new GameDataException("Parsed game data is null.");

        if (gameData.items == null) gameData.items = new ArrayList<>();
        if (gameData.rooms == null)
            throw new GameDataException("'rooms' array not found or null in JSON.");

        for (ItemData id : gameData.items) {
            if (id.name == null || id.name.trim().isEmpty()) continue;
            Item item = new Item(id.name.trim(), id.description != null ? id.description : "");
            if (id.usability != null) {
                item.setUsability(id.usability);
            }
            loadedItems.put(id.name.trim(), item);
        }

        for (RoomData rd : gameData.rooms) {
            if (rd.name == null || rd.name.trim().isEmpty()) continue;
            String roomName = rd.name.trim();
            Room room = new Room(roomName, rd.description != null ? rd.description : "");
            loadedRooms.put(roomName, room);

            if (rd.exits != null) {
                for (Map.Entry<String, String> e : rd.exits.entrySet()) {
                    room.addExit(e.getKey(), e.getValue());
                }
            }

            if (rd.exitData != null) {
                for (Map.Entry<String, ExitDataJson> e : rd.exitData.entrySet()) {
                    String dir = e.getKey().toLowerCase().trim();
                    ExitData ed = new ExitData();
                    ed.setTargetRoom(e.getValue().targetRoom);
                    ed.setConditions(e.getValue().conditions);
                    room.getExit().put(dir, ed);
                }
            }

            if (rd.items != null) {
                for (String itemName : rd.items) {
                    Item item = loadedItems.get(itemName.trim());
                    if (item != null) room.addItem(item);
                }
            }
        }

        this.playerStartRoomName = gameData.playerStart != null ? gameData.playerStart.trim() : null;
        if (!loadedRooms.containsKey(playerStartRoomName))
            throw new GameDataException("Start room not found.");
    }

    public Map<String, Room> getLoadedRooms() { return loadedRooms; }
    public Map<String, Item> getLoadedItems() { return loadedItems; }
    public String getPlayerStartRoomName() { return playerStartRoomName; }

    public static class GameDataException extends Exception {
        public GameDataException(String message) { super(message); }
        public GameDataException(String message, Throwable cause) { super(message, cause); }
    }
}
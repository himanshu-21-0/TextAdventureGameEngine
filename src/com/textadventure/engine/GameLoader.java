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

    public void loadGameData(String filePath) throws IOException {
        if (filePath == null || filePath.trim().isEmpty())
            throw new IllegalArgumentException("File path cannot be null or empty");

        Path path = Paths.get(filePath);
        String jsonContent;

        try {
            jsonContent = Files.readString(path);
            System.out.println("Successfully read content from file : " + filePath);
        } catch (IOException e) {
            System.err.println("Error reading game data file at path:" + filePath);
            throw new IOException("Failed to read game data file:" + filePath, e);
        }

        GameData gameData;
        try {
            gameData = gson.fromJson(jsonContent, GameData.class);
            System.out.println("JSON content successfully parsed into intermediate GameData object.");

            if (gameData != null && gameData.playerStart != null)
                System.out.println("Player starting room found in JSON: " + gameData.playerStart);
        } catch (JsonSyntaxException e) {
            System.err.println("Error parsing JSON file: " + filePath + ". Invalid JSON syntax.");
            throw e;
        }
        System.out.println("Intermediate parsing complete. Next: Processing GameData into game objects.");
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
}
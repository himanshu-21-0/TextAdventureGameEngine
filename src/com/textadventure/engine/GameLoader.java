package com.textadventure.engine;

import com.textadventure.model.Room;
import com.textadventure.model.Item;

import java.util.Map;
import java.util.HashMap;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class GameLoader {
    private Map<String, Room> loadedRooms;
    private Map<String, Item> loadedItems;
    private String playerStartRoomName;

    public GameLoader() {
        this.loadedRooms = new HashMap<>();
        this.loadedItems = new HashMap<>();
        System.out.println("GameLoader initialized. Ready to load data.");
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
        System.out.println("JSON content loaded. Ready for parsing (implementaion pending).");
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
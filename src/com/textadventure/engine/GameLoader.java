package com.textadventure.engine;

import com.textadventure.model.Room;
import com.textadventure.model.Item;

import java.util.Map;
import java.util.HashMap;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
            throw new IllegalArgumentException("Filepath cannot be null or empty");

        System.out.println("Attempting to load game data from: " + filePath);
        System.err.println("Game data loading process initiated (implementation pending).");
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
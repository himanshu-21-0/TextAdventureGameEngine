package com.textadventure.game;

import com.textadventure.model.Room;
import com.textadventure.model.Player;
import com.textadventure.engine.GameLoader;
import com.textadventure.engine.GameLoader.GameDataException;

import com.google.gson.JsonSyntaxException;
import java.io.IOException;

import java.util.Map;

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

        System.out.println("[Initialize] Next steps: Create player and set starting location...");
        System.out.println("----------------------------------------");
    }
}
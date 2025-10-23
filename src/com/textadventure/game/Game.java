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

    public void initialize(String dataFilePath) throws IOException, JsonSyntaxException, GameDataException, IllegalArgumentException {
        System.out.println("----------------------------------------");
        System.out.println("Initializing game from data file: " + dataFilePath + "...");
        System.out.println("----------------------------------------");

        gameLoader.loadGameData(dataFilePath);
        System.out.println("[Initialize] Game data loaded successfully by GameLoader.");

        this.rooms = gameLoader.getLoadedRooms();

        if (this.rooms == null || this.rooms.isEmpty())
            throw new GameDataException("Initialization failed: No rooms were loaded or rooms map is null.");
        System.out
                .println("[Initialize] Rooms map populated in Game instance. Total rooms loaded: " + this.rooms.size());

        String startRoomName = gameLoader.getPlayerStartRoomName();

        if (startRoomName == null || this.rooms.containsKey(startRoomName))
            throw new GameDataException("Initialization failed: Starting room name '" + startRoomName
                    + "' provided by loader, but not found in loaded rooms map");

        this.player = new Player(startRoomName);
        this.player.setCurrentRoomName(startRoomName);

        System.out.println("[Initialize] Player object created.");
        System.out.println("[Initialize] Player starting room set to: '" + startRoomName + "'.");
        System.out.println("----------------------------------------");
        System.out.println("Game initialization complete!");
        System.out.println("----------------------------------------");
    }
}
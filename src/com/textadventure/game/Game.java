package com.textadventure.game;

import com.textadventure.model.Room;
import com.textadventure.model.Player;
import com.textadventure.engine.GameLoader;

import java.util.Map;

public class Game {
    private Map<String, Room> rooms;
    private Player player;
    private GameLoader gameLoader;

    public Game() {
        System.out.println("Game object created. Ready for initialization.");
    }
}
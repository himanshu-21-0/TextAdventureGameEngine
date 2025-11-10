package com.textadventure.utils;

import java.util.List;
import java.util.Map;

public class SaveState {
    private String playerLocation;
    private List<String> playerInventory;
    private Map<String, List<String>> roomItemStates;

    public SaveState(String playerLocation, List<String> playerInventory, Map<String, List<String>> roomItemStates) {
        this.playerLocation = playerLocation;
        this.playerInventory = playerInventory;
        this.roomItemStates = roomItemStates;
    }

    public String getPlayerLocation() { return playerLocation; }
    public List<String> getPlayerInventory() { return playerInventory; }
    public Map<String, List<String>> getRoomItemStates() { return roomItemStates; }

    public SaveState() {}
}
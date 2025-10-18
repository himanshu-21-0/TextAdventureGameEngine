package com.textadventure.model;

import java.util.List;
import java.util.ArrayList;

public class Player {
    private String currentRoomName;
    private List<Item> inventory;

    public Player(String startingRoomName) {
        if (startingRoomName == null || startingRoomName.trim().isEmpty())
            throw new IllegalArgumentException("Player starting room cannot be null or empty");

        this.currentRoomName = startingRoomName.trim();
        this.inventory = new ArrayList<>();
    }

    public void takeItem(Item item) {
        inventory.add(item);
    }

    public void dropItem(Item item) {
        inventory.remove(item);
    }
}
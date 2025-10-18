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
        if (item == null)
            throw new IllegalArgumentException("Cannot add a null item to player inventory.");

        this.inventory.add(item);
    }

    public boolean dropItem(Item item) {
        if (item == null)
            throw new IllegalArgumentException("Cannot drop null item from player inventory.");

        boolean removed = this.inventory.remove(item);

        return removed;
    }
}
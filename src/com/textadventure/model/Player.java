package com.textadventure.model;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import java.util.Iterator;

public class Player {
    private String currentRoomName;
    private List<Item> inventory;

    public Player(/* String startingRoomName */) {
        // if (startingRoomName == null || startingRoomName.trim().isEmpty())
        // throw new IllegalArgumentException("Player starting room cannot be null or
        // empty");

        // this.currentRoomName = startingRoomName.trim();
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

    public List<Item> getInventory() {
        return this.inventory;
    }

    public String getCurrentRoomName() {
        return this.currentRoomName;
    }

    public void setCurrentRoomName(String newRoomName) {
        if (newRoomName == null || newRoomName.trim().isEmpty())
            throw new IllegalArgumentException("Cannot set current room name to null or empty.");

        this.currentRoomName = newRoomName.trim();
    }

    public Optional<Item> findItemInventory(String itemName) {
        for (Item item : inventory) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                return Optional.of(item);
            }
        }
        return Optional.empty();
    }

    public boolean dropItem(String itemName) {
        Iterator<Item> iterator = inventory.iterator();
        while (iterator.hasNext()) {
            Item item = iterator.next();
            if (item.getName().equalsIgnoreCase(itemName)) {
                iterator.remove();
                System.out.println("[Player Debug] Dropped/Removed '" + itemName + "' from inventory.");
                return true;
            }
        }
        return false;
    }
}
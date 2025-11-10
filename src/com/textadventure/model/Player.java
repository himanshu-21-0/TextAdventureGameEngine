package com.textadventure.model;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import java.util.Iterator;

public class Player {
    private String currentRoomName;
    private List<Item> inventory;

    public Player() {
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

    public boolean removeItem(String itemName) {
        if (itemName == null || itemName.isBlank() || inventory == null) {
            return false;
        }
        Iterator<Item> iterator = inventory.iterator();
        while (iterator.hasNext()) {
            Item item = iterator.next();
            if (item.getName() != null && item.getName().equalsIgnoreCase(itemName)) {
                iterator.remove();
                System.out.println("[Player Debug] Removed '" + itemName + "' from inventory."); 
                return true;
            }
        }
        System.out.println("[Player Debug] Item '" + itemName + "' not found in inventory to remove."); 
        return false; 
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
        if (itemName == null || itemName.isBlank()) {
            return Optional.empty();
        }
        for (Item item : inventory) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                return Optional.of(item);
            }
        }
        return Optional.empty();
    }
}
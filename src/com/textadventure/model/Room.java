package com.textadventure.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Room {
    private List<ConditionalDescription> conditionalDescriptions;
    private Map<String, ExitData> exit = new HashMap<>();
    private String name;
    private String description;
    private Map<String, String> exits = new HashMap<>();
    private List<Item> items;

    public Room(String name, String description) {
        if (name == null || name.trim().isEmpty())
            throw new IllegalArgumentException("Room name cannot be null or empty");
        if (description == null)
            throw new IllegalArgumentException("Room description cannot be null");

        this.name = name.trim();
        this.description = description;
        this.exits = new HashMap<>();
        this.items = new ArrayList<>();
        this.conditionalDescriptions = new ArrayList<>();
    }

    public String getName() { return this.name; }
    public String getDescription() { return this.description; }
    public List<ConditionalDescription> getConditionalDescriptions() { return conditionalDescriptions; }

    public void setDescription(String newDescription) {
        this.description = newDescription;
        System.out.println("[Room Debug] Description for room '" + this.name + "' changed.");
    }

    public Map<String, String> getExits() {
        Map<String, String> simple = new HashMap<>();
        for (Map.Entry<String, ExitData> e : exit.entrySet()) {
            if (e.getValue().getTargetRoom() != null) {
                simple.put(e.getKey(), e.getValue().getTargetRoom());
            }
        }
        return simple;
    }

    public Map<String, ExitData> getExit() { return exit; }

    public List<Item> getItems() { return this.items; }

    public void addItem(Item item) {
        if (item == null)
            throw new IllegalArgumentException("Cannot add a null item to the room.");
        this.items.add(item);
    }

    public void removeItem(Item item) {
        if (item == null)
            throw new IllegalArgumentException("Cannot remove a null item from the room.");
        this.items.remove(item);
    }

    public void addExit(String direction, String destinationRoomName) {
        if (direction == null || direction.trim().isEmpty())
            throw new IllegalArgumentException("Exit direction cannot be null or empty");
        if (destinationRoomName == null || destinationRoomName.trim().isEmpty())
            throw new IllegalArgumentException("Exit destination room name cannot be null or empty");

        String normalizedDirection = direction.trim().toLowerCase();
        String trimmedDestination = destinationRoomName.trim();

        this.exits.put(normalizedDirection, trimmedDestination);
        ExitData ed = new ExitData();
        ed.setTargetRoom(trimmedDestination);
        this.exit.put(normalizedDirection, ed);
    }

    public Optional<Item> findItemByName(String itemName) {
        if (itemName == null || itemName.isBlank()) {
            return Optional.empty();
        }
        for (Item item : this.items) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                return Optional.of(item);
            }
        }
        return Optional.empty();
    }

    public Item removeItem(String targetToRemove) {
        Optional<Item> opt = findItemByName(targetToRemove);
        if (opt.isPresent()) {
            Item item = opt.get();
            this.items.remove(item);
            return item;
        }
        return null;
    }
}
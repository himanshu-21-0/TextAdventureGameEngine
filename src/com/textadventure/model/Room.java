package com.textadventure.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.textadventure.model.Item;

public class Room {
    private String name;
    private String description;
    private Map<String, String> exits;
    private List<Item> items;

    public Room(String name, String description) {
        if (name == null || name.trim().isEmpty())
            throw new IllegalArgumentException("Room name cannot be null or empty");
        if (description == null)
            throw new IllegalArgumentException("Room description cannot be null");
        this.name = name;
        this.description = description;
        this.exits = new HashMap<>();
        this.items = new ArrayList<>();
    }
}
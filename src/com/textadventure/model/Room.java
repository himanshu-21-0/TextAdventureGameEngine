package com.textadventure.model;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class Room {
    private String name;
    private String description;
    private Map<String, String> exits;
    private List<Item> items;

    public Room(String name, String description) {
        this.name = name;
        this.description = description;
        this.exits = new HashMap<>();
    }
}
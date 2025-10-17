package com.textadventure.model;

public class Item {
    private String name;
    private String description;

    public Item(String name, String description) {
        if (name == null || name.trim().isEmpty())
            throw new IllegalArgumentException("Item name cannot be null or empty.");

        if (description == null)
            throw new IllegalArgumentException("Item description cannot be null.");
        
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }
}
package com.textadventure.engine;

public class CommandParser {
    public CommandParser() {
        System.out.println("CommandParser initialized");
    }

    public String[] parse(String userInput) {
        if (userInput == null) return new String[0];
        String trimmedInput = userInput.toLowerCase().trim();
        if (trimmedInput.isEmpty()) return new String[0];
        return trimmedInput.split("\\s+");
    }
}
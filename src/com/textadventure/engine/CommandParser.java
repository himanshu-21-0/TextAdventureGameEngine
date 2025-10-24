package com.textadventure.engine;

public class CommandParser {

    public CommandParser() {
        System.out.println("CommandParser initialized");
    }

    public String[] parse(String userInput) {
        if (userInput == null)
            return new String[0];

        String lowerCaseInput = userInput.toLowerCase();
        String trimmedInput = lowerCaseInput.trim();

        if (trimmedInput.isEmpty())
            return new String[0];

        String[] commandParts = trimmedInput.split("\\s+");
        return commandParts;
    }
}

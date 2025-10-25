package com.textadventure;

import com.textadventure.game.Game;
import com.textadventure.model.Room;
import com.textadventure.model.Item;
import com.textadventure.engine.CommandParser;
import com.textadventure.engine.GameLoader.GameDataException;
import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Scanner;
import java.util.Arrays;

public class Main {

    private static final String ADVENTURE_DATA_PATH = "data/adventure.json";

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println(" Welcome to the Text Adventure Engine! ");
        System.out.println("========================================");
        System.out.println("Starting application...");

        System.out.println("[Main] Creating Game instance...");
        Game game = new Game();
        System.out.println("[Main] Game instance created.");

        System.out.println("[Main] Attempting to initialize game data from: " + ADVENTURE_DATA_PATH);
        try {
            game.initialize(ADVENTURE_DATA_PATH);
            System.out.println("[Main] Game initialization successful!");
        } catch (IOException e) {
            System.err.println("[Main] FATAL ERROR during initialization: Cannot read game data file.");
            System.err.println("       File Path Attempted: " + ADVENTURE_DATA_PATH);
            System.err.println("       Error: " + e.getMessage());
            e.printStackTrace();
            return;

        } catch (JsonSyntaxException e) {
            System.err.println("[Main] FATAL ERROR during initialization: Invalid JSON syntax in game data file.");
            System.err.println("       File Path: " + ADVENTURE_DATA_PATH);
            System.err.println("       Error: " + e.getMessage());
            e.printStackTrace();
            return;

        } catch (GameDataException e) {
            System.err.println("[Main] FATAL ERROR during initialization: Invalid game data structure.");
            System.err.println("       File Path: " + ADVENTURE_DATA_PATH);
            System.err.println("       Error: " + e.getMessage());
            e.printStackTrace();
            return;

        } catch (IllegalArgumentException e) {
            System.err.println("[Main] FATAL ERROR during initialization: Invalid argument provided.");
            System.err.println("       Error: " + e.getMessage());
            e.printStackTrace();
            return;

        } catch (Exception e) {
            System.err.println("[Main] FATAL ERROR during initialization: An unexpected error occurred.");
            System.err.println("       Error: " + e.getMessage());
            e.printStackTrace();

            return;
        }

        CommandParser commandParser = new CommandParser();
        System.out.println("[Main] CommandParser instance created.");

        Scanner scanner = new Scanner(System.in);
        System.out.println("[Main] Input Scanner instance created.");

        System.out.println("[Main] Entring main game loop...");
        boolean gameRunning = true;

        while (gameRunning) {
            Room currentRoom = game.getCurrentRoom();
            if (currentRoom == null) {
                System.err.println(
                        "\n[Main] FATAL ERROR: Cannot determine player's current location. Player or Room data is invalid.");
                System.err.println("       Check JSON data integrity and Game/Player class logic.");
                gameRunning = false;
                break;
            }

            System.out.println("\n========================================");
            System.out.println("Location: " + currentRoom.getName());
            System.out.println("----------------------------------------");
            System.out.println(currentRoom.getDescription());

            List<Item> itemsInRoom = currentRoom.getItems();

            if (itemsInRoom != null && !itemsInRoom.isEmpty()) {
                String itemNames = itemsInRoom.stream().map(Item::getName).collect(Collectors.joining(", "));

                System.out.println("----------------------------------------");
                System.out.println("You see: " + itemNames + ".");
            }

            Map<String, String> exits = currentRoom.getExits();

            if (exits != null && !exits.isEmpty()) {
                Set<String> directions = exits.keySet();

                String exitDirections = directions.stream().sorted().collect(Collectors.joining(" "));

                System.out.println("----------------------------------------");
                System.out.println("Exits: " + exitDirections);
            }

            System.out.println("========================================");

            System.out.print("> ");

            String userInput = scanner.nextLine();

            System.out.println("[Debug] User entered: '" + userInput + "'");

            String[] commandParts = commandParser.parse(userInput);
            System.out.println("[Debug] Parsed command: " + Arrays.toString(commandParts));

            if (commandParts.length > 0) {
                String commandVerb = commandParts[0];
                if (commandVerb.equals("quit") || commandVerb.equals("exit")) {
                    gameRunning = false;
                    System.out.println("Quitting game. Goodbye!");
                } else
                    System.out.println("[Game Loop] Command '" + commandVerb + "' received, processing TBD.");
            } else
                System.out.println("Please enter a command.");
        }

        System.out.println("\n[Main] Exited game loop.");

        scanner.close();
        System.out.println("[Main] Input scanner closed");

        System.out.println("\n=========================================");
        System.out.println("      Thank you for playing!      ");
        System.out.println("=========================================");
    }
}

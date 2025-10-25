package com.textadventure;

import com.textadventure.game.Game;
import com.textadventure.engine.GameLoader.GameDataException;

import com.google.gson.JsonSyntaxException;
import java.io.IOException;

import java.util.Scanner;

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

        System.out.println("[Main] Entring main game loop...");
        boolean gameRunning = true;

        while (gameRunning) {
            System.out.println("\n[Game Loop] Processing turn... (Display/Input/Parse/Process coming soon!)");

            System.out.println("[Game Loop] Temp break, exiting loop");
            gameRunning = false;
        }

        System.out.println("\n[Main] Exited game loop.");

        System.out.println("\n=========================================");
        System.out.println("      Thank you for playing!      ");
        System.out.println("=========================================");
    }
}

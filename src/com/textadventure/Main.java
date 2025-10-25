package com.textadventure;

import com.textadventure.game.Game;

public class Main {
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println(" Welcome to the Text Adventure Engine! ");
        System.out.println("========================================");
        System.out.println("Starting application...");

        System.out.println("[Main] Creating Game instance...");
        Game game = new Game();
        System.out.println("[Main] Game instance created.");

        System.out.println("\n...Application setup complete (game loop not yet implemented).");
        System.out.println("========================================");
    }
}

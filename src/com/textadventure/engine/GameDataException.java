package com.textadventure.engine;

public class GameDataException extends Exception {
    public GameDataException(String message) {
        super(message);
    }
    public GameDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
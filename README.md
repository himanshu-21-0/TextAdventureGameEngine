# Text-Adventure Game Engine

[![Java](https://img.shields.io/badge/Java-17%2B-blue.svg)](https://www.oracle.com/java/) [![Gson](https://img.shields.io/badge/Gson-2.13.2-green.svg)](https://github.com/google/gson) [![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

A **flexible and extensible text-based adventure game engine** built in **Java**, designed using **strong Object-Oriented Programming (OOP) principles**. The engine dynamically loads game worlds—including rooms, items, and puzzles—from external JSON configuration files, allowing developers to easily design and expand custom adventure experiences without recompiling the core code. Whether you're crafting a classic escape-room mystery, a branching narrative quest, or a procedural dungeon crawler, this engine provides the modular foundation to bring your vision to life.

## 🎮 Overview

This engine powers **interactive text adventures** where players navigate rooms, collect and use items, solve conditional puzzles, and manage inventory—all driven by a clean, extensible architecture. At its heart is a **JSON-based world builder** that separates content from code, enabling rapid iteration and community contributions.

### Key Design Philosophy
- **OOP Encapsulation**: Core models (`Room`, `Item`, `Player`) are self-contained POJOs with clear interfaces for usability effects, exit conditions, and state management.
- **Dynamic Loading**: `GameLoader` uses Gson to parse JSON into runtime objects, supporting advanced features like multi-item requirements and effect chains.
- **Extensibility**: Add new mechanics (e.g., NPCs, combat) by extending model classes—no core rewrites needed.
- **State Persistence**: Full save/load via `SaveState` captures player position, inventory, and world state in `savegame.json`.

### Demo Adventure: "Abandoned Research Facility: Escape Protocol Alpha"
Included in `data/adventure.json`: An 11-room escape puzzle with 14 items and 6 interconnected challenges. Players must break walls, reroute power, mix chemicals, and bypass security to reach the surface. **Playtime: 45-60 minutes**. **Win Condition**: Activate the elevator in the Surface Lab.

**Sample Gameplay Flow**:
```
> take sledgehammer
You take the Sledgehammer.

> go north
You move north to Maintenance Tunnel.

> use sledgehammer on weak wall
CRASH! The wall shatters, revealing rubble and a new path south.

> go east
The door panel flashes red. You need the Key Card AND the crank signal.

> save
Game saved to savegame.json.
```

## 🚀 Features

| Category | Feature | Details |
|----------|---------|---------|
| **World Building** | JSON-Driven Loading | Define rooms, exits, items, and puzzles in `adventure.json`. Supports simple exits (`exits`) and conditional ones (`exitData`). |
| **Puzzles & Interactions** | Item Usability | `use ITEM on TARGET` triggers effects: `removesTarget`, `addsTarget`, `consumesItem`, `changesRoomDescriptionTo`, `modifiesExit` (e.g., clear conditions). |
| **Navigation** | Conditional Exits | `requiresItem: "Key"` or `["Key", "Crank"]` with custom `failMessage`. Directions: north/south/east/west/up/down. |
| **Player Management** | Inventory System | `take ITEM`, `drop ITEM`, `inventory` (or `inv`). Case-insensitive searches. |
| **Exploration** | Room Descriptions | Dynamic updates via usability effects. `look` shows items and exits. `examine ITEM` reveals details. |
| **Persistence** | Save/Load | `save`/`load` serializes full state (position, inventory, room contents) to JSON. |
| **Commands** | Parser Support | Built-in: `go DIR`, `take ITEM`, `use ITEM on TARGET`, `examine ITEM`, `look`, `inv`, `save`, `load`, `quit`. Extensible via `CommandParser`. |
| **Debugging** | Logging | Console output for initialization, effects, and errors. |

## 🛠️ Project Structure

```
TEXTADVENTUREGAMEENGINE/
├── .vscode/                 # VS Code settings
│   └── settings.json
├── bin/                     # Compiled .class files
│   └── com/textadventure/
│       ├── engine/          # Compiled engine classes
│       ├── game/            # Compiled Game.class
│       ├── model/           # Compiled model classes
│       ├── utils/           # Compiled SaveState.class
│       ├── App.class        # Simple test entry
│       └── Main.class       # Main game entry
├── data/                    # Game world data
│   └── adventure.json       # Demo adventure (editable!)
├── lib/                     # Dependencies
│   └── gson-2.13.2.jar      # JSON parsing library
├── src/                     # Source code
│   └── com/textadventure/
│       ├── engine/          # Core utilities
│       │   ├── CommandParser.java
│       │   ├── GameDataException.java
│       │   └── GameLoader.java
│       ├── game/            # Runtime logic
│       │   └── Game.java
│       ├── model/           # OOP Domain Models (POJOs)
│       │   ├── ConditionalDescription.java
│       │   ├── Conditions.java
│       │   ├── ExitData.java
│       │   ├── ExitModification.java
│       │   ├── Item.java
│       │   ├── Player.java
│       │   └── Room.java
│       ├── utils/           # Helper classes
│       │   └── SaveState.java
│       ├── App.java         # Test entry point
│       └── Main.java        # Main game launcher
├── README.md                # This file
└── savegame.json            # Auto-generated save file
```

## 🏗️ Architecture

The engine follows a **layered OOP design** for maintainability:

```
com.textadventure/
├── engine/          # Core utilities
│   ├── GameLoader.java      # JSON → Objects (Gson-powered)
│   ├── CommandParser.java   # Input splitting & validation
│   └── GameDataException.java # Custom errors
├── game/            # Runtime logic
│   └── Game.java           # Command processing, state management
├── model/           # Domain entities (POJOs)
│   ├── Room.java           # Exits (simple/conditional), items, descriptions
│   ├── Item.java           # Usability nested class for effects
│   ├── Player.java         # Inventory, position
│   ├── ExitData.java       # Target room + conditions
│   ├── Conditions.java     # requiresItem (String/List), failMessage
│   ├── ExitModification.java # Exit tweaks (clear requiresItem)
│   └── ConditionalDescription.java # Future: State-based descs
└── utils/           # Helpers
    └── SaveState.java      # Serializable game snapshot
```

- **Entry Point**: `Main.java` initializes `Game`, runs the loop, and handles I/O.
- **Dependencies**: Gson 2.13.2 (JSON parsing/serialization). No other externals.

## ⚙️ Setup & Installation

### Prerequisites
- **Java 17+** (tested on OpenJDK 21).
- **Maven/Gradle** (optional; manual build supported).
- Download [Gson 2.13.2 JAR](https://repo1.maven.org/maven2/com/google/code/gson/gson/2.13.2/gson-2.13.2.jar) → `lib/gson-2.13.2.jar`.

### Quick Start (Manual Build)
1. Clone or download the project.
2. Place `adventure.json` in `data/`.
3. Compile:
   ```bash
   # Linux/macOS
   javac -cp "lib/gson-2.13.2.jar" -d bin src/com/textadventure/**/*.java

   # Windows
   javac -cp "lib\gson-2.13.2.jar" -d bin src\com\textadventure\**\*.java
   ```
4. Run:
   ```bash
   # Linux/macOS
   java -cp "bin:lib/gson-2.13.2.jar" com.textadventure.Main

   # Windows
   java -cp "bin;lib\gson-2.13.2.jar" com.textadventure.Main
   ```

### With Maven (Recommended for Extensions)
Add to `pom.xml`:
```xml
<dependencies>
    <dependency>
        <groupId>com.google.code.gson</groupId>
        <artifactId>gson</artifactId>
        <version>2.13.2</version>
    </dependency>
</dependencies>
```
Build: `mvn clean compile exec:java -Dexec.mainClass="com.textadventure.Main"`.

## 🎯 Usage

### Playing the Demo
1. Run the engine.
2. Navigate with `go north` (or `n`—extend parser for shortcuts).
3. Interact: `take sledgehammer`, `examine log`, `use crank on socket`.
4. Save progress: `save` (creates `savegame.json`).
5. Quit: `quit`.

### Command Reference
| Command | Example | Effect |
|---------|---------|--------|
| `go <dir>` | `go east` | Move if conditions met. |
| `take <item>` | `take key card` | Add to inventory (removes from room). |
| `use <item> on <target>` | `use acid vial on rusted door` | Trigger usability effects. |
| `examine <target>` / `x <target>` | `x maintenance log` | Show description (inventory or room). |
| `inventory` / `inv` | `inv` | List carried items. |
| `look` | `look` | Redescribe current room. |
| `save` / `load` | `save` | Persist/restore state. |
| `quit` / `exit` | `quit` | End game. |

### Error Handling
- Invalid JSON: `JsonSyntaxException` with line details.
- Missing items/exits: Graceful warnings (e.g., "Item not found").
- Debug Mode: Console logs for loading/effects (toggle via flags in future).

## 🔧 Customization & Extension

### Creating New Adventures
1. **Edit `data/adventure.json`**:
   - Add rooms: `{ "name": "NewRoom", "description": "...", "exits": { "south": "OldRoom" } }`.
   - Define items: Include `usability` for puzzles.
   - Set conditions: `exitData: { "east": { "targetRoom": "Secret", "conditions": { "requiresItem": ["Key", "Code Note"] } } }`.
2. **Validate**: Use JSON Schema (see below) or run the loader.
3. **Reload**: Restart the game.

### JSON Schema (For IDE Validation)
Save as `adventure-schema.json` and reference in your editor:
```json
{
  "$schema": "http://json-schema.org/draft-07/schema#",
  "type": "object",
  "properties": {
    "playerStart": { "type": "string" },
    "items": {
      "type": "array",
      "items": {
        "type": "object",
        "properties": {
          "name": { "type": "string" },
          "description": { "type": "string" },
          "usability": {
            "type": "object",
            "properties": {
              "target": { "type": "string" },
              "effectDescription": { "type": "string" },
              "consumesItem": { "type": "boolean" },
              "removesTarget": { "type": "string" },
              "addsTarget": { "type": "string" },
              "changesRoomDescriptionTo": { "type": "string" },
              "modifiesExit": {
                "type": "object",
                "properties": {
                  "direction": { "type": "string" },
                  "clearRequiresItem": { "type": "boolean" }
                }
              }
            }
          }
        }
      }
    },
    "rooms": {
      "type": "array",
      "items": {
        "type": "object",
        "properties": {
          "name": { "type": "string" },
          "description": { "type": "string" },
          "exits": { "type": "object", "additionalProperties": { "type": "string" } },
          "exitData": {
            "type": "object",
            "additionalProperties": {
              "type": "object",
              "properties": {
                "targetRoom": { "type": "string" },
                "conditions": {
                  "type": "object",
                  "properties": {
                    "requiresItem": { "oneOf": [ { "type": "string" }, { "type": "array", "items": { "type": "string" } } ] },
                    "failMessage": { "type": "string" }
                  }
                }
              }
            }
          },
          "items": { "type": "array", "items": { "type": "string" } }
        }
      }
    }
  },
  "required": ["playerStart", "items", "rooms"]
}
```

### Extending the Engine
- **New Commands**: Override `processCommand` in a subclass of `Game`.
- **NPCs**: Add `Npc` class to `model/`, integrate into `Room.items` (as special items).
- **Combat/Timing**: Extend `Player` with health/timer; add `turn-based` loop in `Game`.
- **GUI**: Integrate with JavaFX/Swing—replace `Main`'s Scanner with UI events.
- **Multiplayer**: Socket-based `Player` sync via external lib (e.g., Netty).

## 🐛 Troubleshooting

| Issue | Cause | Solution |
|-------|-------|----------|
| `ClassNotFoundException: Gson` | Missing JAR | Download to `lib/`. |
| `GameDataException: No rooms` | Invalid JSON | Validate syntax; check room names match `playerStart`. |
| `use` has no effect | Mismatched target | Ensure `usability.target` matches room item name (case-insensitive). |
| Exits always blocked | Conditions not cleared | Use items to `modifiesExit.clearRequiresItem: true`. |
| Save fails | Permissions | Run in writable dir; check console for IO errors. |
| Debug spam | Verbose logs | Comment out `System.out.println` in models (e.g., `Conditions.setRequiresItem`). |

## 📈 Performance & Scalability
- **Memory**: Handles 100+ rooms/items efficiently (HashMaps for O(1) lookups).
- **Limits**: No hard caps; JSON size < 1MB recommended for quick loads.
- **Testing**: Unit tests via JUnit (add to `test/`); manual playthroughs for puzzles.

## 🤝 Contributing

1. **Fork & Clone**: `git clone <your-fork>`.
2. **Branch**: `git checkout -b feature/new-puzzle`.
3. **Develop**: Add JSON worlds or code extensions.
4. **Test**: Run demo; ensure no regressions.
5. **PR**: Describe changes + new adventure walkthrough.

Guidelines:
- Follow OOP: Single responsibility, immutability where possible.
- JSON: Semantic versioning for schemas.
- Docs: Update README for new features.

## 📄 License

MIT License – Free to use, modify, distribute. See [LICENSE](LICENSE) for details.

## Developer  

![Himanshu Singh](https://avatars.githubusercontent.com/u/188773709?s=400&u=6015f332d2cc8673ae1f31fda674eb7d1d89a559&v=4)  
**Himanshu Singh** – *Creator & Maintainer*  

[![GitHub](https://img.shields.io/badge/GitHub-himanshu--21--0-blue?logo=github)](https://github.com/himanshu-21-0) 

##

**Ready to embark on your own adventure?** Dive into `data/adventure.json` and start building worlds today. Questions? Open an issue! 🎲
